package com.example.seamasshih.fxcbridge.Socket;

import android.content.Intent;
import android.util.Log;

import com.example.seamasshih.fxcbridge.MainGameClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Rex on 2018/3/22.
 */

public class ClientReceiveSend extends Thread {

    private Socket socket;
    private String receiveMessage = null;
    private boolean isRun = true;
    private DataInputStream dataInputStream = null;
    private PrintWriter printWriter;

    public ClientReceiveSend(Socket s){
        this.socket = s;
    }

    @Override
    public void run() {
        initialDataStream();
        byte [] buff = new byte[4096];
        int receiveLength;

        while (isRun){
            try {
                receiveLength = dataInputStream.read(buff);
                receiveMessage = new String(buff, 0, receiveLength, "utf-8");
                Log.v("TAG","ClientReceiveSend:39 "+receiveMessage);
                Intent intent = new Intent("ServerMessage");
                intent.putExtra("ServerMessage",receiveMessage);
                MainGameClient.context.sendBroadcast(intent);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                printWriter.print(message);
                printWriter.flush();
            }
        }).start();
    }

    private void initialDataStream(){
        try {
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
