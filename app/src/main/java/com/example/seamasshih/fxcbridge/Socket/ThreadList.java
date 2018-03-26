package com.example.seamasshih.fxcbridge.Socket;

import java.util.ArrayList;

/**
 * Created by Rex on 2018/3/22.
 */

public class ThreadList {

    private static ArrayList<ServerReceiveSend> clientThread = new ArrayList<>();
    private static ArrayList<ClientReceiveSend> serverThread = new ArrayList<>();

    public static void setClientThread(ServerReceiveSend t){
        clientThread.add(t);
    }

    public static void setServerThread(ClientReceiveSend t){
        serverThread.add(t);
    }

    public static ServerReceiveSend getClientThread(int playerIndex){
        return clientThread.get(playerIndexToSavePosition(playerIndex));
    }

    public static ClientReceiveSend getServerThread(){
        return serverThread.get(0);
    }

    private static int playerIndexToSavePosition(int playerIndex){
        return playerIndex - 1;
    }

}
