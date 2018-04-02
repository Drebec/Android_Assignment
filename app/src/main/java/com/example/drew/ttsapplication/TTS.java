package com.example.drew.ttsapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by Drew on 3/25/2018.
 */

public class TTS extends Thread implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Context con;
    public Handler handler;
    private String last;

    TTS(Context con){
        this.con = con;
        tts = new TextToSpeech(con, this);
        last = "";
    }

    public void onInit(int status){
        int result = TextToSpeech.LANG_MISSING_DATA;
        if(status == TextToSpeech.SUCCESS){
            result = tts.setLanguage(Locale.US);
            tts.setPitch((float)0);
            tts.setSpeechRate((float)0);
        }

        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            Toast.makeText(con, "Language or Data not working", Toast.LENGTH_LONG).show();



    }

    @SuppressLint("HandlerLeak")
    public void run(){
        Looper.prepare();

            handler = new Handler(){
                public void handleMessage(Message msg){
                    String response = msg.getData().getString("TT");
                    // do something with the message
                    // parse message for pitch and speed
                    String[] vals = response.split(":");
                    float pitch = Float.parseFloat(vals[0])/(float)10.0;
                    float speed = Float.parseFloat(vals[1])/(float)10.0;
                    String out = vals[2];
                    tts.setPitch(pitch);
                    tts.setSpeechRate(speed);
                    speakOut(out);
                }
            };

        Looper.loop();

    }

    public void speakOut(String text){
        if(last != text){
            last = text;

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

            while(tts.isSpeaking()) try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }


}
