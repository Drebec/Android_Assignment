package com.example.drew.ttsapplication;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Drew on 4/2/2018.
 */

public class Server implements Runnable {
    ServerSocket server;
    int port;
    BufferedReader input;
    TTS tts;

    public Server(int port, TTS tts) {
        this.port = port;
        this.tts = tts;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            while(true) {
                Socket client = server.accept();
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String rec = input.readLine();
                input.close();
                client.close();

                if(rec != null) {
                    Message msg = tts.handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("TT", "15:10:" + rec);
                    msg.setData(b);
                    tts.handler.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
