package com.example.seamasshih.fxcbridge;

import android.animation.Animator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;


import com.example.seamasshih.fxcbridge.Socket.Server;
import com.example.seamasshih.fxcbridge.Socket.SocketServerFunction;

import java.util.StringTokenizer;


public class MainGameServer extends AppCompatActivity {

    //  Rex 2018/03/22 add
    public static Context context;
    private static final int SET_PLAYER_CARD = 1001, SET_BID_VALUE = 1002, SET_BID_END = 1003, BID_PASS = -1;
    private String idSign = "#";
    ServerBroadcast serverBroadcast = new ServerBroadcast();
    ServerHandler serverHandler = new ServerHandler();
    SocketServerFunction serverFunc = SocketServerFunction.getInstance();
    //  Rex

    GameBoard MyGameBoard = GameBoard.getInstance();
    Bid myBid = new Bid();
    PokerCardResource MyResource = new PokerCardResource();
    Button buttonSelect,buttonSurrender,buttonSlidingHandler;
    SlidingDrawer sd;


//    int[] playerIndexArray = {0 , 1 , 2 , 3}; //先留著，怕你會用到
    int[] idMyCardList = {R.id.poker1, R.id.poker2, R.id.poker3, R.id.poker4, R.id.poker5, R.id.poker6, R.id.poker7, R.id.poker8, R.id.poker9, R.id.poker10, R.id.poker11, R.id.poker12, R.id.poker13};
    int[] idMyCardHatList = {R.id.poker1hat, R.id.poker2hat, R.id.poker3hat, R.id.poker4hat, R.id.poker5hat, R.id.poker6hat, R.id.poker7hat, R.id.poker8hat, R.id.poker9hat, R.id.poker10hat, R.id.poker11hat, R.id.poker12hat, R.id.poker13hat};
    int nowMyCardSelected;
    boolean isDealOut = false;
    boolean isPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  Rex 2018/03/22 add
        context = this;
        Intent intent = this.getIntent();
        serverFunc.serverCreate(intent);
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
            for (int i = 0; i < MyGameBoard.MyCard.length; i++) {
                MyGameBoard.MyCard[i].setCardIndex(MyGameBoard.getCardWaitForDrawingWithIndex(i));
                MyGameBoard.MyCard[i].getCardSite().setResourceToAnimatorDealCard(MyResource.cardTable[MyGameBoard.getCardWaitForDrawingWithIndex(i)]);
            }

            setOnListener();

            MyGameBoard.initialDealCardAnimator();
            MyGameBoard.getDealCard().addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    sd.open();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    sd.close();
                }

                @Override
                public void onAnimationCancel(Animator animation) {                }
                @Override
                public void onAnimationRepeat(Animator animation) {                }
            });
            MyGameBoard.playDealCard();
            isDealOut = true;
        }

    }

    void initial() {

        buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setEnabled(false);
        buttonSurrender = findViewById(R.id.buttonSurrender);
        buttonSlidingHandler = findViewById(R.id.handle);

        sd = findViewById(R.id.sd);

        myBid.bidButtonArray[0] = findViewById(R.id.btnClubs);
        myBid.bidButtonArray[1] = findViewById(R.id.btnDiamonds);
        myBid.bidButtonArray[2] = findViewById(R.id.btnHearts);
        myBid.bidButtonArray[3] = findViewById(R.id.btnSpades);
        myBid.bidButtonArray[4] = findViewById(R.id.btnNoTrump);
        myBid.bidButtonArray[5] = findViewById(R.id.btnPass);
        myBid.bidText = findViewById(R.id.bidText);

        for (int i = 0; i < MyGameBoard.MyCard.length; i++) {
            MyGameBoard.MyCard[i].setCardSite((MyCardImageView) findViewById(idMyCardList[i]));
            MyGameBoard.MyCard[i].getCardSite().initialDealAnimator();
        }

        for (int i = 0; i < MyGameBoard.MyCardHat.length; i++)
            MyGameBoard.MyCardHat[i].setCardSite( (ImageView) findViewById(idMyCardHatList[i]) );

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

        MyGameBoard.unableAllMyCard();
    }

    void setOnListener() {
        for (int i = 0; i < MyGameBoard.MyCard.length; i++)
            MyGameBoard.MyCard[i].getCardSite().setOnClickListener(clickMyCard);

        myBid.bidButtonArray[0].setOnClickListener(bid);
        myBid.bidButtonArray[1].setOnClickListener(bid);
        myBid.bidButtonArray[2].setOnClickListener(bid);
        myBid.bidButtonArray[3].setOnClickListener(bid);
        myBid.bidButtonArray[4].setOnClickListener(bid);
        myBid.bidButtonArray[5].setOnClickListener(bid);
        buttonSelect.setOnClickListener(selectMyPlayingCard);
        buttonSurrender.setOnClickListener(surrenderThisGame);
        sd.setOnDrawerOpenListener(slidingOpen);
        sd.setOnDrawerCloseListener(slidingClose);
    }
    Button.OnClickListener bid = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int bidValue = 0;
            if (!isPress){
                isPress = true;
                switch (v.getId()){
                    case R.id.btnClubs:
                        bidValue = (Integer.valueOf(myBid.bidButtonArray[0].getText().toString().substring(1,2)) * 10);
                        break;
                    case R.id.btnDiamonds:
                        bidValue = (Integer.valueOf(myBid.bidButtonArray[1].getText().toString().substring(1,2)) * 10) + 1;
                        break;
                    case R.id.btnHearts:
                        bidValue = (Integer.valueOf(myBid.bidButtonArray[2].getText().toString().substring(1,2)) * 10) + 2;
                        break;
                    case R.id.btnSpades:
                        bidValue = (Integer.valueOf(myBid.bidButtonArray[3].getText().toString().substring(1,2)) * 10) + 3;
                        break;
                    case R.id.btnNoTrump:
                        bidValue = (Integer.valueOf(myBid.bidButtonArray[4].getText().toString().substring(2,3)) * 10) + 4;
                        break;
                    case R.id.btnPass:
                        bidValue = BID_PASS;
                        break;
                }
                isPress = false;
                if (bidValue != BID_PASS){
                    serverFunc.deliverBidValueToAllClient(bidValue);
                    myBid.setNowBid(bidValue);
                    myBid.setString();
                }
                serverFunc.deliverBidRight(myBid, MainGameServer.this, MyGameBoard, buttonSelect);
                myBid.setButtonDisable();
                myBid.myBid = bidValue;
                Log.d("TAG","myBid.myBid:"+myBid.myBid);
            }
        }
    };
    SlidingDrawer.OnDrawerOpenListener slidingOpen = new SlidingDrawer.OnDrawerOpenListener() {
        @Override
        public void onDrawerOpened() {
            buttonSlidingHandler.setText("﹀");
            buttonSlidingHandler.setTextColor(Color.BLACK);
        }
    };
    SlidingDrawer.OnDrawerCloseListener slidingClose = new SlidingDrawer.OnDrawerCloseListener() {
        @Override
        public void onDrawerClosed() {
            buttonSlidingHandler.setText("︿");
            buttonSlidingHandler.setTextColor(Color.WHITE);
        }
    };
    Button.OnClickListener surrenderThisGame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
    Button.OnClickListener selectMyPlayingCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nowMyCardSelected != -1) {
                MyGameBoard.PlayedCard[0].setCardIndex(MyGameBoard.getMyPlayingCardIndex());
                MyGameBoard.PlayedCard[0].getCardSite().setImageResource(MyResource.cardTable[MyGameBoard.PlayedCard[0].getCardIndex()]);

                MyGameBoard.MyCardHat[nowMyCardSelected].getCardSite().setImageResource(R.drawable.available);
                MyGameBoard.MyCard[nowMyCardSelected].getCardSite().setImageResource(MyResource.cardHatTable[MyGameBoard.MyCard[nowMyCardSelected].getCardIndex()]);
                MyGameBoard.MyCard[nowMyCardSelected].setPlayed(true);

                //  Rex 2018/03/22
                serverFunc.setIsServerTurn(true);
                buttonSelect.setEnabled(false);     //出牌鍵關閉
                MyGameBoard.removeHatAllMyCard();   //移除灰帽
                MyGameBoard.unableAllMyCard();      //不能選牌
                serverFunc.setThisTurnMajorCardColor(MyGameBoard);
                serverFunc.calculateReceiveCountAndPlayerPosition(MyGameBoard, buttonSelect);
                sd.animateClose();
                nowMyCardSelected = -1;
            }

        }
    };
    ImageView.OnClickListener clickMyCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < MyGameBoard.MyCard.length; i++) {
                if (MyGameBoard.MyCard[i].isPlayed()) continue;
                else if (!MyGameBoard.MyCard[i].isEnable()) continue;
                else if (v.getId() == idMyCardList[i]) {
                    MyGameBoard.setMyPlayingCardIndex(MyGameBoard.MyCard[i].getCardIndex());
                    MyGameBoard.MyCardHat[i].getCardSite().setImageResource(R.drawable.selecting);
                    nowMyCardSelected = i;
                }
                else MyGameBoard.MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
            }
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

    private  class ServerHandler extends Handler {
        public void handleMessage(Message message){
            switch (message.what) {
                case SET_PLAYER_CARD:
                    serverFunc.setReceiveCardIndex(message.obj.toString().trim());
                    MyGameBoard.PlayedCard[serverFunc.playerPosition].setCardIndex(Integer.valueOf(serverFunc.getReceiveCardIndex()));
                    MyGameBoard.PlayedCard[serverFunc.playerPosition].getCardSite().setImageResource(MyResource.cardTable[Integer.valueOf(message.obj.toString().trim())]);
                    serverFunc.calculateReceiveCountAndPlayerPosition(MyGameBoard, buttonSelect);
                    break;
                case SET_BID_VALUE:
                    if (Integer.valueOf(message.obj.toString().trim()) != BID_PASS){
                        serverFunc.deliverBidValueToAllClient(Integer.valueOf(message.obj.toString().trim()));
                        myBid.setNowBid(Integer.valueOf(message.obj.toString().trim()));
                        myBid.setString();
                    }
                    serverFunc.deliverBidRight(myBid, MainGameServer.this, MyGameBoard, buttonSelect);
                    break;
                case SET_BID_END:
                    int firstRoundPlayer = serverFunc.bidRight + 1;
                    if (firstRoundPlayer > Server.CLIENT_LIMITATION)
                        firstRoundPlayer = 0;
                    serverFunc.deliverBidEndToClient();
                    serverFunc.bidResultMessage(MainGameServer.this, myBid);
                    myBid.setBidUnitsInvisible();
                    MyGameBoard.setPriorColor(myBid.nowBid % 10);
                    serverFunc.deliverPlayRight(serverFunc.bidRight + 1, MyGameBoard, buttonSelect);
                    serverFunc.playerPosition = firstRoundPlayer;
                    MyGameBoard.setBridgeWinner(firstRoundPlayer);
                    break;

            }
        }
    }


    //  Rex 2018/03/22 add Functions
    private void registerServerBroadcast(){
        registerReceiver(serverBroadcast, new IntentFilter("ClientMessage"));
        registerReceiver(serverBroadcast, new IntentFilter("playerIndex"));
    }

}