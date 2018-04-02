package com.example.drew.ttsapplication;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by Drew on 3/25/2018.
 */

public class TestTalkActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    EditText talkText;
    SeekBar pitch, speed;
    String pitch_S, speed_S;
    TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_talk);

        talkText = (EditText) this.findViewById(R.id.talkText);
        Button talkButton = (Button) this.findViewById(R.id.makeTalkButton);
        talkButton.setOnClickListener(this);

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
    }

    public void onClick(View v){
        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
        switch(v.getId()){
            case R.id.makeTalkButton:
                String input = talkText.getText().toString();

                String msg = pitch_S + speed_S + input;

                Message sendMsg = tts.handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("TT", msg);
                sendMsg.setData(b);
                tts.handler.sendMessage(sendMsg);
                break;
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
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
