package com.example.seamasshih.fxcbridge.Socket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.seamasshih.fxcbridge.MainGameServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Rex on 2018/3/22.
 */

public class ServerReceiveSend extends Thread {

    private Socket socket;
    private boolean isRun = true;
    private String receiveMessage = null;
    private int playerIndex;
    private InputStream inputStream = null;
    private PrintWriter printWriter;

    public ServerReceiveSend(Socket s, int inputPlayerIndex){
        this.socket = s;
        this.playerIndex = inputPlayerIndex;
        Log.v("TAG","PlayerIndex:"+playerIndex+" "+socket);
    }

    @Override
    public void run() {
        initialDataStream();
        sendPlayerIndexToServer(playerIndex);

        int receiveLength;
        byte [] buff = new byte[4096];
        while (isRun){
            try {
                if ((receiveLength = inputStream.read(buff)) != -1){
                    receiveMessage = new String(buff, 0, receiveLength);
                    Intent intent = new Intent("ClientMessage");
                    Bundle bundle = new Bundle();
                    bundle.putInt("PlayerIndex",playerIndex);
                    bundle.putString("ClientMessage",receiveMessage);
                    intent.putExtras(bundle);
                    MainGameServer.context.sendBroadcast(intent);
                }
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
                Log.d("TAG","Use for Temp Save");
            }
        }).start();
    }

    private void initialDataStream(){
        try {
            inputStream = socket.getInputStream();
            printWriter = new PrintWriter(socket.getOutputStream(),true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendPlayerIndexToServer(int playerIndex){
        MainGameServer.context.sendBroadcast(new Intent("playerIndex").putExtra("playerIndex",playerIndex));
    }

    public static void sendPlayerIndexToClient(int playerIndex){
        ThreadList.getClientThread(playerIndex).sendMessage("SetPlayerIndex#"+playerIndex+"#");
    }

}
