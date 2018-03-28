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
import com.example.seamasshih.fxcbridge.Socket.ServerCreate;

import com.example.seamasshih.fxcbridge.Socket.ThreadList;

public class MainGameServer extends AppCompatActivity {

    //  Rex 2018/03/22 add
    public static Context context;
    private int playerIndex, receiveCardCount = 0, nextTurnFirstPlayer = 0, playerPosition;
    private static final int SET_PLAYER_CARD = 1001;
    private boolean isFirstRound = true;
    ServerBroadcast serverBroadcast = new ServerBroadcast();
    ServerHandler serverHandler = new ServerHandler();
    //  Rex

    GameBoard MyGameBoard = GameBoard.getInstance();
    PokerCardResource MyResource = new PokerCardResource();
    Button buttonSelect,buttonSurrender,buttonSlidingHandler;
    SlidingDrawer sd;


    int[] playerIndexArray = {0 , 1 , 2 , 3}; //先留著，怕你會用到
    int[] idMyCardList = {R.id.poker1, R.id.poker2, R.id.poker3, R.id.poker4, R.id.poker5, R.id.poker6, R.id.poker7, R.id.poker8, R.id.poker9, R.id.poker10, R.id.poker11, R.id.poker12, R.id.poker13};
    int[] idMyCardHatList = {R.id.poker1hat, R.id.poker2hat, R.id.poker3hat, R.id.poker4hat, R.id.poker5hat, R.id.poker6hat, R.id.poker7hat, R.id.poker8hat, R.id.poker9hat, R.id.poker10hat, R.id.poker11hat, R.id.poker12hat, R.id.poker13hat};
    int nowMyCardSelected;
    boolean isDealOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game);
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
        buttonSurrender = findViewById(R.id.buttonSurrender);
        buttonSlidingHandler = findViewById(R.id.handle);

        sd = findViewById(R.id.sd);

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
    }

    void setOnListener() {
        for (int i = 0; i < MyGameBoard.MyCard.length; i++)
            MyGameBoard.MyCard[i].getCardSite().setOnClickListener(clickMyCard);
        buttonSelect.setOnClickListener(selectMyPlayingCard);
        buttonSurrender.setOnClickListener(surrenderThisGame);
        sd.setOnDrawerOpenListener(slidingOpen);
        sd.setOnDrawerCloseListener(slidingClose);
    }
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
            MyGameBoard.PlayedCard[0].setCardIndex(MyGameBoard.getMyPlayingCardIndex());
            MyGameBoard.PlayedCard[0].getCardSite().setImageResource(MyResource.cardTable[MyGameBoard.PlayedCard[0].getCardIndex()]);

            MyGameBoard.MyCardHat[nowMyCardSelected].getCardSite().setImageResource(R.drawable.available);
            MyGameBoard.MyCard[nowMyCardSelected].getCardSite().setImageResource(MyResource.cardHatTable[MyGameBoard.MyCard[nowMyCardSelected].getCardIndex()]);
            MyGameBoard.MyCard[nowMyCardSelected].setPlayed(true);

            sd.animateClose();
            buttonSelect.setEnabled(false);
            MyGameBoard.removeHatAllMyCard();

            //  Rex 2018/03/22
            if (isFirstRound){
                MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[0].getCardColor());
                isFirstRound = false;
            }
            else
                MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());

            MyGameBoard.unableAllMyCard();
            deliverCardToAllClient(String.valueOf(MyGameBoard.PlayedCard[0].getCardIndex()));
            calculateCountAndPosition();

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
            String receiveMessage = null;
            Message message = Message.obtain();
            Bundle bundle = intent.getExtras();
            switch (mAction){
                case "ClientMessage":
                    playerIndex = bundle.getInt("PlayerIndex");
                    receiveMessage = bundle.getString("ClientMessage");
//                    deliverPlayRight(calculateNextPlayer(playerIndex));
                    message.what = SET_PLAYER_CARD;
                    message.obj = receiveMessage;
                    serverHandler.sendMessage(message);
                    break;
                case "playerIndex":
                    Log.v("TAG","MainGameServer:221_playerIndex:"+intent.getIntExtra("playerIndex",0));
                    deliverFirstCardToClient(intent.getIntExtra("playerIndex",0));
                    break;
            }
        }
    }

    private class ServerHandler extends Handler {
        public void handleMessage(Message message){
            switch (message.what) {
                case SET_PLAYER_CARD:
                    if (receiveCardCount == 0){
                        playerPosition = calculateNextTurnFirstPlayerPosition(nextTurnFirstPlayer);
                        Log.v("TAG","MainGameServer:234_playerPosition:"+playerPosition);
                    }
                    Log.v("TAG","MainGameServer:236_playerPosition:"+playerPosition);
                    MyGameBoard.PlayedCard[playerPosition].setCardIndex(Integer.valueOf(message.obj.toString().trim()));
                    MyGameBoard.PlayedCard[playerPosition].getCardSite().setImageResource(MyResource.cardTable[Integer.valueOf(message.obj.toString().trim())]);
                    deliverCardToAllClient(message.obj.toString().trim());
                    calculateCountAndPosition();
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
                Log.i("TAG","MainGameServer:254, i:" + i + " cardList:" + MyGameBoard.getCardWaitForDrawingWithIndex(i));
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
            buttonSelect.setEnabled(true);
            MyGameBoard.judgeMyCardEnable(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
        }else{
            ThreadList.getClientThread(nextPlayerIndex).sendMessage("SetSendCardRight#");
        }
    }

    private void deliverCardToAllClient(String sendToClientCardIndex){
        for (int playerIndex = 1; playerIndex <= Server.CLIENT_LIMITATION; playerIndex++){
            ThreadList.getClientThread(playerIndex).sendMessage("OtherPlayerCard#"+sendToClientCardIndex+"#");
        }
    }

    private int calculateNextTurnFirstPlayerPosition(int nextTurnFirstPlayer){
        for (int position = 0; position < playerIndexArray.length; position++){
            if (nextTurnFirstPlayer == playerIndexArray[position]){
                Log.v("TAG","calculateNextTurnFirstPlayerPosition:"+position);
                return position;
            }
        }
        return 0;
    }

    private int getNextTurnFirstPlayer(){
        MyGameBoard.judgeWhoAreBridgeWinner();
        Log.v("TAG","getNextTurnFirstPlayer:299_getNextTurnFirstPlayer:"+MyGameBoard.getBridgeWinner());
        return MyGameBoard.getBridgeWinner();
    }

    private void endThisTurn(){
        Log.v("TAG","endThisTurn:304");
        MyGameBoard.addWinBridge(MyGameBoard.getBridgeWinner());
        MyGameBoard.animationCloseBridge();
    }

    private void calculateCountAndPosition(){
        receiveCardCount++;
        Log.v("TAG","MainGameServer:307_receiveCardCount:"+receiveCardCount);
        if (receiveCardCount == 4){
            nextTurnFirstPlayer = getNextTurnFirstPlayer();

            if (nextTurnFirstPlayer == 0){
                buttonSelect.setEnabled(true);
                MyGameBoard.enableAllMyCard();
            }
            else
                deliverPlayRight(nextTurnFirstPlayer);

            endThisTurn();
            receiveCardCount = 0;
            Log.v("TAG","MainGameServer:311_nextTurnFirstPlayer:"+nextTurnFirstPlayer+" receiveCardCount:"+receiveCardCount);
        }else {
            deliverPlayRight(calculateNextPlayer(playerIndex));
            Log.v("TAG","MainGameServer:314_playerIndex:"+playerIndex);
        }

        playerPosition = (playerPosition + 1) % 4;
        Log.v("TAG","MainGameServer:318_playerPosition:"+playerPosition);

    }

}