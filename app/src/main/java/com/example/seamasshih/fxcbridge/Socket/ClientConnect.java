package com.example.seamasshih.fxcbridge.Socket;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rex on 2018/3/22.
 */

public class ClientConnect implements Runnable {

    private String ip = null;
    private int port, time = 5000;

    public ClientConnect(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Log.d("TAG","ip:"+ip+" port:"+port);
            Socket socket = new Socket(ip,port);
            socket.setSoTimeout(time);
            SocketList.setServerSocket(socket);
            ThreadList.setServerThread( new ClientReceiveSend(SocketList.getServerSocket()));
            ThreadList.getServerThread().start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
