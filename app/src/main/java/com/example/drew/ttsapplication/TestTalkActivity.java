package com.example.drew.ttsapplication;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ArrayList;

/**
 * Created by Drew on 3/25/2018.
 */

public class TestTalkActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    EditText talkText;
    public TextView statusText;
    static TextView recText;
    TextView ipText;
    SeekBar pitch, speed;
    String pitch_S, speed_S;
    TTS tts;
    Network networkManager;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_talk);

        talkText = (EditText) this.findViewById(R.id.talkText);
        Button talkButton = (Button) this.findViewById(R.id.makeTalkButton);
        talkButton.setOnClickListener(this);

        Button listenButton = (Button) this.findViewById(R.id.listenButton);
        listenButton.setOnClickListener(this);

        statusText = (TextView) this.findViewById(R.id.statusText);
        recText = (TextView) this.findViewById(R.id.recText);
        ipText = (TextView) this.findViewById(R.id.ipText);
        ipText.setText(getIpAddress());


        pitch = (SeekBar) findViewById(R.id.pitchBar);
        pitch.setMax(20);
        pitch.setProgress(1);
        pitch.setOnSeekBarChangeListener(this);

        speed = (SeekBar) findViewById(R.id.speedBar);
        speed.setMax(20);
        speed.setProgress(1);
        speed.setOnSeekBarChangeListener(this);

        tts = new TTS(this);
        tts.start();

        networkManager = new Network(tts);
        Thread networkThread = new Thread(networkManager);
        networkThread.start();

        //startListening();

        //Looper.prepare();
            //@SuppressLint("HandlerLeak")
                handler = new Handler() {

            public void handleMessage(Message msg) {
                String aResponse = msg.getData().getString("main");
                startListening();
            }
        };
        //Looper.loop();

    }

    //@SuppressLint("HandlerLeak")
    //handler = new Handler() {

        //public void handleMessage(Message msg) {
        //  String aResponse = msg.getData().getString("main");
        //    startListening();
        //}
    //};

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getIpAddress() {
        String ipAddress = "Unable to Fetch IP..";
        try {
            Enumeration en;
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address)
                        return inetAddress.getHostAddress();
                }
            }
        } catch (SocketException ex) {
            //ex.printStackTrace();
        }
        return ipAddress;
    }

    public void sendToNetwork(String msg) {
        Message toClient = networkManager.handler.obtainMessage();
        Bundle n = new Bundle();
        n.putString("N", msg);
        toClient.setData(n);
        networkManager.handler.sendMessage(toClient);
    }

    public void sendToTTS(String msg) {
        Message sendMsg = tts.handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("TT", "10:10:" + msg);
        sendMsg.setData(b);
        tts.handler.sendMessage(sendMsg);
    }

    public void onClick(View v){
        //Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
        switch(v.getId()){
            case R.id.makeTalkButton:
                String input = talkText.getText().toString();

                String msg = pitch_S + speed_S + input;

                Message sendMsg = tts.handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("TT", msg);
                sendMsg.setData(b);
                tts.handler.sendMessage(sendMsg);
                Message toClient = networkManager.handler.obtainMessage();
                Bundle n = new Bundle();
                n.putString("N", msg);
                toClient.setData(n);
                networkManager.handler.sendMessage(toClient);
                break;
            case R.id.listenButton:
                startListening();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //Toast.makeText(this,"" + seekBar.getProgress() + ":", Toast.LENGTH_LONG).show();
        switch(seekBar.getId()) {
            case R.id.pitchBar:
                if(seekBar.getProgress() != 0) {
                    pitch_S = "" + seekBar.getProgress() + ":";
                }
            break;
            case R.id.speedBar:
                if(seekBar.getProgress() != 0) {
                    speed_S = "" + seekBar.getProgress() + ":";
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String message = result.get(0);
                    System.out.println(message);
                    //if(message.toLowerCase().equals("start")) {
                        //System.out.println(message);
                        Message toClient = networkManager.handler.obtainMessage();
                        Bundle n = new Bundle();
                        n.putString("N", message);
                        toClient.setData(n);
                        networkManager.handler.sendMessage(toClient);
                    //} else {
                      //  startListening();
                    //}
                    recText.setText(message);
                    //sendToNetwork(message);
                }
                break;
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
