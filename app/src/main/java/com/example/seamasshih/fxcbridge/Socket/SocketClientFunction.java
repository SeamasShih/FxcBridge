package com.example.seamasshih.fxcbridge.Socket;

import android.content.Context;
import android.widget.Toast;

import com.example.seamasshih.fxcbridge.Bid;

/**
 * Created by Rex on 2018/4/2.
 */

public class SocketClientFunction {
    private static SocketClientFunction socketClientFunctionInstance;

    public static SocketClientFunction getInstance(){
        if (socketClientFunctionInstance == null)
            socketClientFunctionInstance = new SocketClientFunction();
        return socketClientFunctionInstance;
    }

    public void deliverBidValueToServer(int inputBidValue){
        ThreadList.getServerThread().sendMessage("SetBidValue#" + inputBidValue + "#");
    }

    public void deliverBidEndToServer(){
        ThreadList.getServerThread().sendMessage("SetBidEnd#");
    }

    public void bidResultMessage(Context context, Bid funcBid, int bidWinner){
        String player;
        if (bidWinner == 0)
            player = "Server";
        else
            player = "Player" + bidWinner;
        Toast.makeText(context, player+" win the bid! The bid result is"+funcBid.bidText.getText().toString(),Toast.LENGTH_LONG).show();
    }



}
