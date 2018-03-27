package com.example.seamasshih.fxcbridge.Socket;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Rex on 2018/3/22.
 */

public class ServerCreate implements Runnable{

    private int isConnectedPlayer = 0, playerIndex = 1;
    private int port = 1234;
    private int time = 5000;

    public ServerCreate(int port){
        this.port = port;
    }

    @Override
    public void run() {

        Log.d("TAG","port:"+port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(time);
            while (isConnectedPlayer < Server.CLIENT_LIMITATION){
                Socket socket = getSocket(serverSocket);
                if (socket != null){
                    SocketList.setClientSocket(playerIndex,socket);
                    ThreadList.setClientThread(new ServerReceiveSend(SocketList.getClientSocket(playerIndex), playerIndex));
                    ThreadList.getClientThread(playerIndex).start();
                    increaseConnectedPlayer();
                    increasePlayerIndex();
                }
            }

            for (int playerIndex = 1; playerIndex <= Server.CLIENT_LIMITATION; playerIndex++){
                ServerReceiveSend.sendPlayerIndexToClient(playerIndex);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    private Socket getSocket(ServerSocket serverSocket){
        try {
            Log.w("TAG","getSocket:52, isConnected Player"+isConnectedPlayer);
            return serverSocket.accept();
        }catch (IOException e){
            Log.w("TAG","getSocket:55");
            return null;
        }
    }

    private void increaseConnectedPlayer(){
        isConnectedPlayer++;
    }

    private void increasePlayerIndex(){
        playerIndex++;
    }

}
