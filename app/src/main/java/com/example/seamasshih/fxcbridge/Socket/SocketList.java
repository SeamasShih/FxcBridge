package com.example.seamasshih.fxcbridge.Socket;

import java.net.Socket;

/**
 * Created by Rex on 2018/3/22.
 */

public class SocketList {

    private static Socket serverSocket[] = new Socket[1];
    private static Socket clientSocket[] = new Socket[Server.CLIENT_LIMITATION];

    public static void setServerSocket(Socket s){
        serverSocket[0] = s;
    }

    public static void setClientSocket(int playerIndex, Socket s){
        clientSocket[playerIndexToSavePosition(playerIndex)] = s;
    }

    public static Socket getServerSocket(){
        return serverSocket[0];
    }

    public static Socket getClientSocket(int playerIndex){
        return clientSocket[playerIndexToSavePosition(playerIndex)];
    }

    private static int playerIndexToSavePosition(int playerIndex){
        return playerIndex - 1;
    }

}
