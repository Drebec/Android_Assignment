package com.example.drew.ttsapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Drew on 4/3/2018.
 */

public class Network implements Runnable {

    Receive receiver;
    Send sender;
    ServerSocket serverSocket;
    Socket socket;
    TTS tts;
    Handler handler;


    public Network(TTS tts) {
        this.tts = tts;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiver = new Receive(socket, tts);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        sender = new Send(socket);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        Looper.prepare();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                String sendMessage = msg.getData().getString("N");
                Message send = sender.handler.obtainMessage();
                Bundle s = new Bundle();
                s.putString("S", sendMessage);
                send.setData(s);
                sender.handler.sendMessage(send);
            }
        };

        Looper.loop();

    }
}
