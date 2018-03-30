package com.example.seamasshih.fxcbridge;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;

import com.example.seamasshih.fxcbridge.Socket.ClientConnect;
import com.example.seamasshih.fxcbridge.Socket.ThreadList;

import java.util.StringTokenizer;

public class MainGameClient extends AppCompatActivity {

    //  Rex 2018/03/22 add
    public static Context context;
    private boolean isFirstRound = true, isNowMyTurn = false;
    private final static int SET_PLAYER_CARD = 1010, SET_FIRST_CARD = 1020, SET_DELIVER_RIGHT = 1030;
    private int setFirstCardIndex = 0, playerIndex, receiveCardCount = 0, nextTurnFirstPlayer = 0, playerPosition;
    private static final int myself = 0, serverPosition = 0;
    private String idSign = "#";
    ClientBroadcast clientBroadcast = new ClientBroadcast();
    ClientHandler clientHandler = new ClientHandler();
    //  Rex

    GameBoard MyGameBoard = GameBoard.getInstance();
    PokerCardResource MyResource = new PokerCardResource();
    Button buttonSelect,buttonSurrender,buttonSlidingHandler,btnBidClubs,btnBidDiamonds,btnBidHearts,btnBidSpades,btnBidNoTrumps,btnBidPass;
    Player MyPlayer = new Player();
    TextView textBid;
    SlidingDrawer sd;

    int[] playerIndexArray = {0 , 1 , 2 , 3};
    int[] idMyCardList = {R.id.poker1, R.id.poker2, R.id.poker3, R.id.poker4, R.id.poker5, R.id.poker6, R.id.poker7, R.id.poker8, R.id.poker9, R.id.poker10, R.id.poker11, R.id.poker12, R.id.poker13};
    int[] idMyCardHatList = {R.id.poker1hat, R.id.poker2hat, R.id.poker3hat, R.id.poker4hat, R.id.poker5hat, R.id.poker6hat, R.id.poker7hat, R.id.poker8hat, R.id.poker9hat, R.id.poker10hat, R.id.poker11hat, R.id.poker12hat, R.id.poker13hat};
    int nowMyCardSelected;
    boolean isDealOut = false;
    boolean isSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  Rex 2018/03/26 add
        context = this;
        connectServer();
        registerClientBroadcastReceiver();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isDealOut) {
            initial();
            setOnListener();
            isDealOut = true;
        }
    }

    void initial() {

        buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setEnabled(false);
        buttonSurrender = findViewById(R.id.buttonSurrender);
        buttonSlidingHandler = findViewById(R.id.handle);
        btnBidClubs = findViewById(R.id.bidClub);
        btnBidDiamonds = findViewById(R.id.bidDiamonds);
        btnBidHearts = findViewById(R.id.bidHearts);
        btnBidSpades = findViewById(R.id.bidSpades);
        btnBidNoTrumps = findViewById(R.id.bidNoTrumps);
        btnBidPass = findViewById(R.id.bidPass);

        textBid = findViewById(R.id.bidText);

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
        btnBidClubs.setOnClickListener(bid);
        btnBidDiamonds.setOnClickListener(bid);
        btnBidHearts.setOnClickListener(bid);
        btnBidSpades.setOnClickListener(bid);
        btnBidNoTrumps.setOnClickListener(bid);
        btnBidPass.setOnClickListener(bid);
        sd.setOnDrawerOpenListener(slidingOpen);
        sd.setOnDrawerCloseListener(slidingClose);
    }
    Button.OnClickListener bid = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!isSend){
                isSend = true;
                switch (v.getId()){
                    case R.id.bidClub:
                        break;
                    case R.id.bidDiamonds:
                        break;
                    case R.id.bidHearts:
                        break;
                    case R.id.bidSpades:
                        break;
                    case R.id.bidNoTrumps:
                        break;
                    case R.id.bidPass:
                        break;
                }
                isSend = false;
            }
        }
    };
    SlidingDrawer.OnDrawerOpenListener slidingOpen = new SlidingDrawer.OnDrawerOpenListener() {
        @Override
        public void onDrawerOpened() {
            buttonSlidingHandler.setText("﹀");
        }
    };
    SlidingDrawer.OnDrawerCloseListener slidingClose = new SlidingDrawer.OnDrawerCloseListener() {
        @Override
        public void onDrawerClosed() {
            buttonSlidingHandler.setText("︿");
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
                MyGameBoard.PlayedCard[MyPlayer.getNowPlayer()].setCardIndex(MyGameBoard.getMyPlayingCardIndex());
                MyGameBoard.PlayedCard[MyPlayer.getNowPlayer()].getCardSite().setImageResource(MyResource.cardTable[MyGameBoard.PlayedCard[0].getCardIndex()]);

                MyGameBoard.MyCardHat[nowMyCardSelected].getCardSite().setImageResource(R.drawable.available);
                MyGameBoard.MyCard[nowMyCardSelected].getCardSite().setImageResource(MyResource.cardHatTable[MyGameBoard.MyCard[nowMyCardSelected].getCardIndex()]);
                MyGameBoard.MyCard[nowMyCardSelected].setPlayed(true);

                //  Rex 2018/03/22
                isNowMyTurn = false;
                buttonSelect.setEnabled(false);
                MyGameBoard.removeHatAllMyCard();
                MyGameBoard.unableAllMyCard();
                setUpThisTurnMajorCardColor();
                deliverCardToServer(MyGameBoard.PlayedCard[0].getCardIndex());
                sd.animateClose();
            }
        }
    };
    ImageView.OnClickListener clickMyCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < MyGameBoard.MyCard.length; i++) {
                if (MyGameBoard.MyCard[i].isPlayed()) continue;
                if (!MyGameBoard.MyCard[i].isEnable()) continue;
                if (v.getId() == idMyCardList[i]) {
                    MyGameBoard.setMyPlayingCardIndex(MyGameBoard.MyCard[i].getCardIndex());
                    MyGameBoard.MyCardHat[i].getCardSite().setImageResource(R.drawable.selecting);
                    nowMyCardSelected = i;
                }
                else MyGameBoard.MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
            }
        }
    };


    //  Rex  2018/03/22 add Classes
    private class ClientBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            String receiveMessage = null, idText = null, content = null;
            Message message = Message.obtain(), firstCardMsg, otherCardMsg;
            StringTokenizer tokenizer;
            switch (mAction){
                case "ServerMessage":
                    receiveMessage = intent.getStringExtra("ServerMessage");
                    tokenizer = new StringTokenizer(receiveMessage,idSign);
                    idText = tokenizer.nextToken();
                    switch (idText){
                        case "SetFirstCard":
                            while (tokenizer.hasMoreTokens()){
                                firstCardMsg = Message.obtain();
                                content = tokenizer.nextToken();
                                if (content.equals("SetPlayerIndex")){
                                    playerIndex = Integer.valueOf(tokenizer.nextToken().trim());
                                }
                                else if (!content.equals("SetFirstCard")){
                                    firstCardMsg.what = SET_FIRST_CARD;
                                    firstCardMsg.obj = content;
                                    clientHandler.sendMessage(firstCardMsg);
                                }
                            }
                            break;
                        case "SetPlayerIndex":
                            playerIndex = Integer.valueOf(tokenizer.nextToken().trim());
                            while (tokenizer.hasMoreTokens()){
                                firstCardMsg = Message.obtain();
                                content = tokenizer.nextToken();
                                if (!content.equals("SetFirstCard")){
                                    firstCardMsg.what = SET_FIRST_CARD;
                                    firstCardMsg.obj = content;
                                    clientHandler.sendMessage(firstCardMsg);
                                }
                            }
                            setPlayerIndexArray(playerIndex);
                            break;
                        case "SetSendCardRight":
                            message.what = SET_DELIVER_RIGHT;
                            message.obj = "";
                            while (tokenizer.hasMoreTokens()){
                                String cardRightContent = tokenizer.nextToken();
                                if (!cardRightContent.equals("OtherPlayerCard")){
                                    message.obj = cardRightContent;
                                }
                            }
                            clientHandler.sendMessage(message);
                            break;
                        case "OtherPlayerCard":
                            message.what = SET_PLAYER_CARD;
                            message.obj = tokenizer.nextToken().trim();
                            clientHandler.sendMessage(message);
                            while (tokenizer.hasMoreTokens()){
                                otherCardMsg = Message.obtain();
                                if (tokenizer.nextToken().equals("SetSendCardRight")){
                                    otherCardMsg.what = SET_DELIVER_RIGHT;
                                    otherCardMsg.obj = "";
                                    clientHandler.sendMessage(otherCardMsg);
                                }
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private class ClientHandler extends Handler {
        public void handleMessage(Message message) {
            switch (message.what) {
                case SET_PLAYER_CARD:
                    setOtherPlayerCard(Integer.valueOf(message.obj.toString().trim()));
                    break;
                case SET_FIRST_CARD:
                    try{
                        MyGameBoard.MyCard[setFirstCardIndex].setCardIndex(Integer.valueOf(message.obj.toString().trim()));
                    }catch (NumberFormatException n){
                        n.printStackTrace();
                    }

                    setFirstCardIndex++;
                    if (setFirstCardIndex == 13){
                        setFirstCardIndex = 0;
                        MyGameBoard.arrangeMyCard();

                        for (int i  = 0; i < MyGameBoard.MyCard.length; i++){
                            MyGameBoard.MyCard[i].getCardSite().setResourceToAnimatorDealCard(MyResource.cardTable[MyGameBoard.MyCard[i].getCardIndex()]);
                        }

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
                            public void onAnimationCancel(Animator animation) {

                            }
                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        MyGameBoard.playDealCard();

                    }
                    break;
                case SET_DELIVER_RIGHT:
                    buttonSelect.setEnabled(true);
                    isNowMyTurn = true;
                    if (!message.obj.toString().equals("")){
                        setOtherPlayerCard(Integer.valueOf(message.obj.toString().trim()));
                    }
                    break;
            }
        }

    }

    //  Rex  2018/03/22  add Functions
    private void connectServer(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        new Thread(new ClientConnect(bundle.getString("ip"),bundle.getInt("port"))).start();
    }

    private void registerClientBroadcastReceiver(){
        registerReceiver(clientBroadcast,new IntentFilter("ServerMessage"));
    }

    private void deliverCardToServer(int cardIndex){
        ThreadList.getServerThread().sendMessage(String.valueOf(cardIndex));
    }

    private void setOtherPlayerCard(int cardIndex){
        MyGameBoard.PlayedCard[playerPosition].setCardIndex(cardIndex);
        MyGameBoard.PlayedCard[playerPosition].getCardSite().setImageResource(MyResource.cardTable[cardIndex]);
        calculateReceiveCountAndPlayerPosition();
        if (isNowMyTurn){
            limitCanUseCardColor();
        }
    }

    private void limitCanUseCardColor(){
        if (isFirstRound){
            MyGameBoard.judgeMyCardEnable(MyGameBoard.PlayedCard[4-playerIndex].getCardColor());
        }else{
            if (MyGameBoard.getBridgeWinner() != 0){
                MyGameBoard.judgeMyCardEnable(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
            }
        }
    }

    private void setPlayerIndexArray(int playerIndex){
        for (int i = 0; i < playerIndexArray.length; i++){
            playerIndexArray[i] = (playerIndexArray[i] + playerIndex) % 4;
        }
        playerPosition = calculatePlayerPosition(serverPosition);
    }

    private int calculatePlayerPosition(int nextTurnFirstPlayer){
        for (int position = 0; position < playerIndexArray.length; position++){
            if (nextTurnFirstPlayer == playerIndexArray[position]){
                return position;
            }
        }
        return 0;
    }

    private void setUpThisTurnMajorCardColor(){
        if (isFirstRound){
            MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[4-playerIndex].getCardColor());
        }else
            MyGameBoard.setMajorColor(MyGameBoard.PlayedCard[MyGameBoard.getBridgeWinner()].getCardColor());
    }

    private int getNextTurnFirstPlayer(){
        MyGameBoard.judgeWhoAreBridgeWinner();
        return MyGameBoard.getBridgeWinner();
    }

    private void endThisTurn(){
        MyGameBoard.addWinBridge(MyGameBoard.getBridgeWinner());
        MyGameBoard.animationCloseBridge();
    }

    private void calculateReceiveCountAndPlayerPosition(){
        receiveCardCount++;
        if (receiveCardCount == 4){
            isFirstRound = false;
            nextTurnFirstPlayer = getNextTurnFirstPlayer();
            if (nextTurnFirstPlayer == myself){
                MyGameBoard.enableAllMyCard();
            }
            endThisTurn();
            receiveCardCount = 0;
        }

        if (receiveCardCount == 0)
            playerPosition = nextTurnFirstPlayer;
        else
            playerPosition = (playerPosition + 1) % 4;
    }


}