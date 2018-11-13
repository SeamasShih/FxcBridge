package com.example.seamasshih.fxcbridge.Socket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.seamasshih.fxcbridge.Bid;
import com.example.seamasshih.fxcbridge.GameBoard;

/**
 * Created by Rex on 2018/3/31.
 */

public class SocketServerFunction {
    private static SocketServerFunction socketServerFunctionInstance;

    public int playerIndex = 0, receiveCardCount = 0, nextTurnFirstPlayer = 0, playerPosition = 0, bidRight = 0;
    private static final int myself = 0;
    private boolean isFirstRound = true, isServerTurn = false;
    private String receiveCardIndex = null, sendToClientCardIndex = null;


    public static SocketServerFunction getInstance(){
        if (socketServerFunctionInstance == null)
            socketServerFunctionInstance = new SocketServerFunction();
        return socketServerFunctionInstance;
    }

    public void setPlayerIndex(int inputPlayerIndex){
        playerIndex = inputPlayerIndex;
    }


    public int getPlayerPosition(){
        return playerPosition;
    }


    public void setReceiveCardIndex(String inputReceiveCardIndex){
        receiveCardIndex = inputReceiveCardIndex;
    }

    public String getReceiveCardIndex(){
        return receiveCardIndex;
    }


    public void setIsServerTurn(boolean inputIsServerTurn){
        isServerTurn = inputIsServerTurn;
    }



//    Function

    public void serverCreate(Intent intent){
        new Thread(new ServerCreate(intent.getIntExtra("port",0))).start();
    }

    public int calculateNextPlayer(int playerIndex){
        return playerIndex + 1;
    }

    public void deliverFirstCardToClient(int playerIndex, GameBoard MyGameBoard)  {
        if (playerIndex != 0 && playerIndex <= Server.CLIENT_LIMITATION){
            Log.v("TAG","playerIndex:" + playerIndex);
            for (int i = 13 * playerIndex; i <= 13 * playerIndex + 12; i++){
                Log.w("TAG","i:"+i);
                ThreadList.getClientThread(playerIndex).sendMessage("SetFirstCard#"+String.valueOf(MyGameBoard.getCardWaitForDrawingWithIndex(i))+"#");
            }
        }
    }

    public void deliverBidRight(Bid funcBid, Context context, GameBoard MyGameBoard, Button buttonSelect){
        bidRight--;
        Log.i("TAG","bidRight:"+bidRight);
        if (bidRight < 0){
            bidRight = Server.CLIENT_LIMITATION;
            ThreadList.getClientThread(bidRight).sendMessage("SetBidRight#");
        }else if(bidRight == 0){
            if (funcBid.nowBid == funcBid.myBid){
                funcBid.setBidUnitsInvisible();
                deliverBidEndToClient();
                bidResultMessage(context, funcBid);
                MyGameBoard.setPriorColor(funcBid.nowBid % 10);
                Log.d("TAG","SET_BID_END_bidRight:" + bidRight);
                deliverPlayRight(bidRight + 1, MyGameBoard, buttonSelect);
                playerPosition = bidRight + 1;
                MyGameBoard.setBridgeWinner(bidRight + 1);
            }
            funcBid.setButtonEnable();
        }else {
            ThreadList.getClientThread(bidRight).sendMessage("SetBidRight#");
        }
    }

    public void deliverBidValueToAllClient(int bidValue){
        for (int loopPlayerIndex = 1; loopPlayerIndex <= Server.CLIENT_LIMITATION; loopPlayerIndex++)
            ThreadList.getClientThread(loopPlayerIndex).sendMessage("SetBidValue#"+bidValue+"#");
    }

    public void deliverBidEndToClient(){
        for (int loopPlayerIndex = 1; loopPlayerIndex <= Server.CLIENT_LIMITATION; loopPlayerIndex++)
            ThreadList.getClientThread(loopPlayerIndex).sendMessage("SetBidEnd#"+bidRight+"#");
    }

    public void bidResultMessage(Context context, Bid funcBid){
        String player;
        if (bidRight == 0)
            player = "Server";
        else
            player = "Player" + bidRight;
        Toast.makeText(context, player+" win the bid! The bid result is"+funcBid.bidText.getText().toString(),Toast.LENGTH_LONG).show();
    }

    public void deliverPlayRight(int nextPlayerIndex, GameBoard MyGameBoard, Button buttonSelect){
        Log.d("TAG","deliverPlayRight_nextPlayerIndex:"+nextPlayerIndex);
        if (nextPlayerIndex > Server.CLIENT_LIMITATION){
            playerIndex = 0;
            buttonSelect.setEnabled(true);
            MyGameBoard.enableAllMyCard();
            if (MyGameBoard.getBridgeWinner() != 0)
                MyGameBoard.judgeMyCardEnable(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
        }else{
            ThreadList.getClientThread(nextPlayerIndex).sendMessage("SetSendCardRight#");
        }
    }

    public void deliverCardToAllClient(GameBoard MyGameBoard){
        if (isServerTurn){
            sendToClientCardIndex = String.valueOf(MyGameBoard.PlayedCard[0].getCardIndex());
            isServerTurn = false;
        }
        else
            sendToClientCardIndex = receiveCardIndex;

        for (int loopPlayerIndex = 1; loopPlayerIndex <= Server.CLIENT_LIMITATION; loopPlayerIndex++)
            ThreadList.getClientThread(loopPlayerIndex).sendMessage("OtherPlayerCard#"+sendToClientCardIndex+"#");
    }

    public void setThisTurnMajorCardColor(GameBoard MyGameBoard){
//        if (isFirstRound) {
//            MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[].getCardColor());
//            isFirstRound = false;
//        } else
        MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
    }

    public void getNextTurnFirstPlayerFunc(GameBoard MyGameBoard, Button buttonSelect){
        MyGameBoard.judgeWhoAreBridgeWinner();
        nextTurnFirstPlayer = MyGameBoard.getBridgeWinner();
        if (nextTurnFirstPlayer == myself){
            buttonSelect.setEnabled(true);
            MyGameBoard.enableAllMyCard();
        }
        else
            deliverPlayRight(nextTurnFirstPlayer, MyGameBoard, buttonSelect);
    }

    public void endThisTurn(GameBoard MyGameBoard){
        MyGameBoard.addWinBridge(MyGameBoard.getBridgeWinner());
        MyGameBoard.animationCloseBridge();
    }

    public void calculateReceiveCountAndPlayerPosition(GameBoard MyGameBoard, Button buttonSelect){
        receiveCardCount++;
        if (receiveCardCount == 4){
            getNextTurnFirstPlayerFunc(MyGameBoard, buttonSelect);
            endThisTurn(MyGameBoard);
            receiveCardCount = 0;
        }else {
            deliverPlayRight(calculateNextPlayer(playerIndex), MyGameBoard, buttonSelect);
        }

        deliverCardToAllClient(MyGameBoard);

        if (receiveCardCount == 0){
            playerIndex = 0;
            playerPosition = nextTurnFirstPlayer;
        }
        else
            playerPosition = (playerPosition + 1) % 4;
    }



}
