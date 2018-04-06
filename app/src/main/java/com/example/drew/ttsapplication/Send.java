package com.example.drew.ttsapplication;

import android.os.Looper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Drew on 4/3/2018.
 */

public class Send implements Runnable {
    Socket socket;
    OutputStream data;
    PrintWriter printWriter;
    Handler handler;

    public Send(Socket socket) {
        this.socket = socket;
        try {
            data = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Looper.prepare();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                String send = msg.getData().getString("S");
                printWriter.print(send);
                printWriter.flush();
                System.out.println("Sending");
            }
        };
        Looper.loop();
    }
}
