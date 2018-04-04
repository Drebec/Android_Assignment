package com.example.drew.ttsapplication;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.drew.ttsapplication.TestTalkActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Drew on 4/1/2018.
 */

public class SocketServer extends Thread {

    public static SocketServer inst;
    public ServerSocket serverSocket;
    static final int SOCKETPORT = 9999;
    public Socket socket;
    public BufferedReader input;
    private TestTalkActivity parent;
    PrintWriter printWriter;

    public static SocketServer getInstance() {
        if(inst == null)
            inst = new SocketServer();
        return inst;
    }

    public void setParent(TestTalkActivity c) { parent = c; };

    public void run() {
        //Looper.prepare();
        //while(true) {
            if(serverSocket == null) {
                try {
                    serverSocket = new ServerSocket(SOCKETPORT);
                    Log.v("LOGGING", "Receive Listening");

                    socket = serverSocket.accept();
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parent.statusText.setText("Connected");
                        }
                    });
                } catch (Exception evt) {
                    Log.v("LOGGING", "evt error**********");
                    evt.printStackTrace();
                }
            }
            if (input == null && serverSocket != null) {
                try {
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parent.statusText.setText("Starting to send");
                        }
                    });
                    printWriter = new PrintWriter(socket.getOutputStream(), true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                String command = null;
                if(input != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parent.statusText.setText("Waiting to read");
                        }
                    });;

                    command = input.readLine();

                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parent.statusText.setText("Received");
                        }
                    });

                    printWriter.print("hello world");
                    printWriter.flush();

                    Message msg = parent.handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("client", command);
                    msg.setData(b);
                    parent.handler.sendMessage(msg);
                } else {
                    parent.runOnUiThread(new Runnable () {
                        @Override
                        public void run() {
                            parent.statusText.setText("Disconnected");
                            input = null;
                            serverSocket = null;
                        }
                    });
                }
            } catch (IOException e) {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parent.statusText.setText("Disconnected");
                    }
                });
            }
        //}

        //Looper.loop();
    }

    public class ServerSend extends Thread {
        public ServerSend inst;

        public ServerSend getInstance() {
            if (inst == null)
                inst = new ServerSend();
            return inst;
        }

        public void run() {

        }
    }
}