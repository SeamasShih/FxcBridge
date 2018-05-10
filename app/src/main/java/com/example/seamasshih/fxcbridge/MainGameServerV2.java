package com.example.seamasshih.fxcbridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.seamasshih.fxcbridge.Socket.Server;
import com.example.seamasshih.fxcbridge.Socket.ServerCreate;
import com.example.seamasshih.fxcbridge.Socket.SocketServerFunction;
import com.example.seamasshih.fxcbridge.Socket.SocketServerFunctionV2;
import com.example.seamasshih.fxcbridge.Socket.ThreadList;
import com.example.seamasshih.fxcbridge.View.MyCardSector;

import java.util.StringTokenizer;

public class MainGameServerV2 extends AppCompatActivity {

    //  Rex 2018/03/22 add
    public static Context context;
    private int playerIndex = 0, receiveCardCount = 0, nextTurnFirstPlayer = 0, playerPosition = 0;
    private static final int myself = 0;
    private static final int SET_PLAYER_CARD = 1001, SET_BID_VALUE = 1002, SET_BID_END = 1003, BID_PASS = -1;
    private String idSign = "#";
    private boolean isFirstRound = true, isServerTurn = true;
    private String receiveCardIndex = null;
    ServerBroadcast serverBroadcast = new ServerBroadcast();
    ServerHandler serverHandler = new ServerHandler();
    SocketServerFunctionV2 serverFunc = SocketServerFunctionV2.getInstance();
    //  Rex

    GameBoard MyGameBoard = GameBoard.getInstance();
    BidV2 myBid = new BidV2();
    PokerCardResource MyResource = new PokerCardResource();

    int[] idMyCardList = {R.id.poker1, R.id.poker2, R.id.poker3, R.id.poker4, R.id.poker5, R.id.poker6, R.id.poker7, R.id.poker8, R.id.poker9, R.id.poker10, R.id.poker11, R.id.poker12, R.id.poker13};
    int[] idMyCardHatList = {R.id.poker1hat, R.id.poker2hat, R.id.poker3hat, R.id.poker4hat, R.id.poker5hat, R.id.poker6hat, R.id.poker7hat, R.id.poker8hat, R.id.poker9hat, R.id.poker10hat, R.id.poker11hat, R.id.poker12hat, R.id.poker13hat};
    int nowMyCardSelected;
    boolean isDealOut = false;
    boolean isPress = false;
    MyCardSector myCardSector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game_v2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  Rex 2018/03/22 add
        context = this;
        serverCreate();
        registerServerBroadcast();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!isDealOut){
            initial();

            MyGameBoard.initialCardWaitForDrawing();
            MyGameBoard.randomCardWaitForDrawing();
            MyGameBoard.arrangeCardWaitForDrawingWithIndex();

            for (int i = 0;i < MyGameBoard.MyCard.length; i++) {
                myCardSector.setMyCardList(i, MyGameBoard.getCardWaitForDrawingWithIndex(i));
                myCardSector.setMyCardResourceList(i , MyResource.cardTable[MyGameBoard.getCardWaitForDrawingWithIndex(i)]);
                myCardSector.setMyCardImage(i , MyResource.cardTable[MyGameBoard.getCardWaitForDrawingWithIndex(i)]);
            }
            myCardSector.setCanPlay(false);
            setOnListener();
            isDealOut = true;
        }

    }

    void initial() {

        myBid.bidButtonArray[0] = findViewById(R.id.btnClubs);
        myBid.bidButtonArray[1] = findViewById(R.id.btnDiamonds);
        myBid.bidButtonArray[2] = findViewById(R.id.btnHearts);
        myBid.bidButtonArray[3] = findViewById(R.id.btnSpades);
        myBid.bidButtonArray[4] = findViewById(R.id.btnNoTrump);
        myBid.bidButtonArray[0].setCenterText("♣");
        myBid.bidButtonArray[1].setCenterText("♦");
        myBid.bidButtonArray[2].setCenterText("♥");
        myBid.bidButtonArray[3].setCenterText("♠");
        myBid.bidButtonArray[4].setCenterText("NT");
        myBid.btnPass = findViewById(R.id.btnPass);
        myBid.bidText = findViewById(R.id.bidText);

        MyGameBoard.PlayedCard[1].setCardSite((PlayingCardImageView) findViewById(R.id.leftPlayingCard));
        MyGameBoard.PlayedCard[2].setCardSite((PlayingCardImageView) findViewById(R.id.partnerPlayingCard));
        MyGameBoard.PlayedCard[3].setCardSite((PlayingCardImageView) findViewById(R.id.rightPlayingCard));
        MyGameBoard.PlayedCard[0].setCardSite((PlayingCardImageView) findViewById(R.id.myPlayingCard));

        MyGameBoard.PlayedCard[1].getCardSite().setThisCardSite(1);
        MyGameBoard.PlayedCard[2].getCardSite().setThisCardSite(2);
        MyGameBoard.PlayedCard[3].getCardSite().setThisCardSite(3);
        MyGameBoard.PlayedCard[0].getCardSite().setThisCardSite(0);

        MyGameBoard.WinBridge[0] = findViewById(R.id.myWinBridge);
        MyGameBoard.WinBridge[1] = findViewById(R.id.leftWinBridge);
        MyGameBoard.WinBridge[2] = findViewById(R.id.partnerWinBridge);
        MyGameBoard.WinBridge[3] = findViewById(R.id.rightWinBridge);
        for (int i = 0; i < MyGameBoard.WinBridge.length; i++)
            MyGameBoard.WinBridge[i].setImageLevel(0);

        myCardSector = findViewById(R.id.MyCard);
        myCardSector.setCanPlay(false);
    }

    void setOnListener() {
        myCardSector.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("TAG","LongClick");
                return false;
            }
        });
        myCardSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("TAG","我進到監聽器啦!!!!!");
                if (myCardSector.getReadToPlay()) {
                    Log.w("TAG","我進到傳牌啦~~~~~~~");
                    //  Rex 2018/03/22
                    serverFunc.setIsServerTurn(true);
                    serverFunc.setThisTurnMajorCardColor(MyGameBoard);
                    serverFunc.calculateReceiveCountAndPlayerPosition(MyGameBoard, myCardSector);
                    nowMyCardSelected = -1;
                    myCardSector.setReadToPlay(false);
                }
            }
        });
        myBid.bidButtonArray[0].setOnClickListener(bid);
        myBid.bidButtonArray[1].setOnClickListener(bid);
        myBid.bidButtonArray[2].setOnClickListener(bid);
        myBid.bidButtonArray[3].setOnClickListener(bid);
        myBid.bidButtonArray[4].setOnClickListener(bid);
        myBid.btnPass.setOnClickListener(bid);

    }
    Button.OnClickListener bid = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myBid.setMyBid();
            int bidValue = 0;
            switch (v.getId()){
                case R.id.btnClubs:
                    bidValue = myBid.getMyBid() * 10;
                    break;
                case R.id.btnDiamonds:
                    bidValue = myBid.getMyBid() * 10 + 1;
                    break;
                case R.id.btnHearts:
                    bidValue = myBid.getMyBid() * 10 + 2;
                    break;
                case R.id.btnSpades:
                    bidValue = myBid.getMyBid() * 10 + 3;
                    break;
                case R.id.btnNoTrump:
                    bidValue = myBid.getMyBid() * 10 + 4;
                    break;
                case R.id.btnPass:
                    bidValue = BID_PASS;
                    break;
            }
            if (!myBid.readyToBid() && bidValue != BID_PASS) return;
            if (bidValue != BID_PASS){
                serverFunc.deliverBidValueToAllClient(bidValue);
                myBid.setNowBid(bidValue);
                myBid.setString();
            }
            serverFunc.deliverBidRight(myBid, MainGameServerV2.this, MyGameBoard, myCardSector);
            myBid.myBid = bidValue;
            myBid.resetButtonBid();
            myBid.setButtonDisable();
            Log.d("Seamas","bidValue = " + bidValue);
        }
    };

    //  Rex 2018/03/22 add Classes
    private class ServerBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            String receiveMessage, idText;
            Message message = Message.obtain();
            Bundle bundle = intent.getExtras();
            StringTokenizer tokenizer;
            switch (mAction){
                case "ClientMessage":
                    serverFunc.setPlayerIndex(bundle.getInt("PlayerIndex"));
                    receiveMessage = bundle.getString("ClientMessage");
                    tokenizer = new StringTokenizer(receiveMessage, idSign);
                    idText = tokenizer.nextToken();
                    switch (idText){
                        case "SetPlayerCard":
                            message.what = SET_PLAYER_CARD;
                            break;
                        case "SetBidValue":
                            message.what = SET_BID_VALUE;
                            break;
                        case "SetBidEnd":
                            message.what = SET_BID_END;
                            break;
                    }
                    if (tokenizer.hasMoreTokens())
                        message.obj = tokenizer.nextToken();
                    serverHandler.sendMessage(message);
                    break;
                case "playerIndex":
                    serverFunc.deliverFirstCardToClient(intent.getIntExtra("playerIndex",0),MyGameBoard);
                    break;
            }
        }
    }

    private class ServerHandler extends Handler {
        public void handleMessage(Message message){
            switch (message.what) {
                case SET_PLAYER_CARD:
                    serverFunc.setReceiveCardIndex(message.obj.toString().trim());
                    MyGameBoard.PlayedCard[serverFunc.playerPosition].setCardIndex(Integer.valueOf(serverFunc.getReceiveCardIndex()));
                    MyGameBoard.PlayedCard[serverFunc.playerPosition].getCardSite().setImageResource(MyResource.cardTable[Integer.valueOf(message.obj.toString().trim())]);
                    serverFunc.calculateReceiveCountAndPlayerPosition(MyGameBoard, myCardSector);
                    break;
                case SET_BID_VALUE:
                    if (Integer.valueOf(message.obj.toString().trim()) != BID_PASS){
                        serverFunc.deliverBidValueToAllClient(Integer.valueOf(message.obj.toString().trim()));
                        myBid.setNowBid(Integer.valueOf(message.obj.toString().trim()));
                        myBid.setString();
                    }
                    serverFunc.deliverBidRight(myBid, MainGameServerV2.this, MyGameBoard, myCardSector);
                    break;
                case SET_BID_END:
                    int firstRoundPlayer = serverFunc.bidRight + 1;
                    if (firstRoundPlayer > Server.CLIENT_LIMITATION)
                        firstRoundPlayer = 0;
                    serverFunc.deliverBidEndToClient();
                    serverFunc.bidResultMessage(MainGameServerV2.this, myBid);
                    myBid.setBidUnitsInvisible();
                    MyGameBoard.setPriorColor(myBid.nowBid % 10);
                    serverFunc.deliverPlayRight(serverFunc.bidRight + 1, MyGameBoard,myCardSector);
                    serverFunc.playerPosition = firstRoundPlayer;
                    MyGameBoard.setBridgeWinner(firstRoundPlayer);
                    break;

            }
        }
    }



    //  Rex 2018/03/22 add Functions
    private void serverCreate(){
        Intent intent = this.getIntent();
        new Thread(new ServerCreate(intent.getIntExtra("port",0))).start();
    }

    private void registerServerBroadcast(){
        registerReceiver(serverBroadcast, new IntentFilter("ClientMessage"));
        registerReceiver(serverBroadcast, new IntentFilter("playerIndex"));
    }

    private void deliverFirstCardToClient(int playerIndex)  {
        if (playerIndex != 0 && playerIndex <= Server.CLIENT_LIMITATION){
            for (int i = 13 * playerIndex; i <= 13 * playerIndex + 12; i++){
                ThreadList.getClientThread(playerIndex).sendMessage("SetFirstCard#"+String.valueOf(MyGameBoard.getCardWaitForDrawingWithIndex(i))+"#");
            }
        }
    }

    private int calculateNextPlayer(int playerIndex){
        return playerIndex + 1;
    }

    private void deliverPlayRight(int nextPlayerIndex){
        if (nextPlayerIndex > Server.CLIENT_LIMITATION){
            playerIndex = 0;
            myCardSector.setCanPlay(true);
            myCardSector.judgeMyCardEnable(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
        }else{
            ThreadList.getClientThread(nextPlayerIndex).sendMessage("SetSendCardRight#");
        }
    }

    private void deliverCardToAllClient(){
        String sendToClientCardIndex = null;
        if (isServerTurn){
            sendToClientCardIndex = String.valueOf(MyGameBoard.PlayedCard[0].getCardIndex());
            isServerTurn = false;
        }
        else{
            sendToClientCardIndex = receiveCardIndex;
        }
        for (int playerIndex = 1; playerIndex <= Server.CLIENT_LIMITATION; playerIndex++){
            ThreadList.getClientThread(playerIndex).sendMessage("OtherPlayerCard#"+sendToClientCardIndex+"#");
        }
    }

    private void setUpThisTurnMajorCardColor(){
        if (isFirstRound) {
            MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[0].getCardColor());
            isFirstRound = false;
        } else
            MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
    }

    private void getNextTurnFirstPlayer(){
        MyGameBoard.judgeWhoAreBridgeWinner();
        nextTurnFirstPlayer = MyGameBoard.getBridgeWinner();
        if (nextTurnFirstPlayer == myself){
            myCardSector.setCanPlay(true);
            myCardSector.enableAllMyCard();
        }
        else{
            deliverPlayRight(nextTurnFirstPlayer);
        }
    }

    private void endThisTurn(){
        MyGameBoard.addWinBridge(MyGameBoard.getBridgeWinner());
        MyGameBoard.animationCloseBridge();
    }


    private void calculateReceiveCountAndPlayerPosition(){
        receiveCardCount++;
        if (receiveCardCount == 4){
            getNextTurnFirstPlayer();
            endThisTurn();
            receiveCardCount = 0;
        }else {
            deliverPlayRight(calculateNextPlayer(playerIndex));
        }

        deliverCardToAllClient();

        if (receiveCardCount == 0){
            playerIndex = 0;
            playerPosition = nextTurnFirstPlayer;
        }
        else
            playerPosition = (playerPosition + 1) % 4;
    }

}