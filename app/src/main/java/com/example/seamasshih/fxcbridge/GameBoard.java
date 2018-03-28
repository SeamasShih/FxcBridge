package com.example.seamasshih.fxcbridge;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by SeamasShih on 2018/3/19.
 */

public class GameBoard {

    private static GameBoard uniqueInstance;
    private GameBoard(){
        for (int i = 0; i < MyCard.length; i++)
            MyCard[i] = new Card();
        for (int i = 0; i < MyCardHat.length; i++)
            MyCardHat[i] = new CardHat();
        for (int i = 0; i < PlayedCard.length; i++)
            PlayedCard[i] = new PlayingCard();
        for (int i = 0; i <winBridgeCount.length; i++)
            winBridgeCount[i] = 0;
    }
    public static GameBoard getInstance(){
        if (uniqueInstance == null)
            uniqueInstance = new GameBoard();
        return uniqueInstance;
    }

    public ImageView[] WinBridge = new ImageView[4];
    public Card[] MyCard = new Card[13];
    public CardHat[] MyCardHat = new CardHat[13];
    public PlayingCard[] PlayedCard = new PlayingCard[4];
    private int winBridgeCount[] = new int[4];
    private int myTeamBridgeNeedToWin;
    private int otherTeamBridgeNeedToWin;
    private int myPlayingCardIndex;
    private int playedCount;
    private int[] cardWaitForDrawing = new int[52];
    private int bridgeWinner;
    private int priorColor;
    /* clubs 0 diamonds 1 hearts 2 spades 3 small 4 little 5 seven_down 6 medium_down 7 medium_up 8 seven_up 9 large 10 no_king 11 */

    private int majorColor;
    private AnimatorSet dealCard = new AnimatorSet();
    @SuppressLint("ObjectAnimatorBinding")
    private ObjectAnimator sleep = ObjectAnimator.ofFloat(this,"translationX" , 0, 0).setDuration(500);
    private AnimatorSet closeBridge = new AnimatorSet();

    public void addPlayedCount(){ playedCount = ++playedCount % 4 ; }
    public int getPlayedCount(){ return playedCount; }
    public boolean isCleanPlayingCard(){ return playedCount == 0; }

    public void setMyPlayingCardIndex(int myPlayingCardIndex){this.myPlayingCardIndex = myPlayingCardIndex;}
    public int getMyPlayingCardIndex(){return myPlayingCardIndex;}

    public void initialCardWaitForDrawing(){
        for (int i = 0; i < cardWaitForDrawing.length; i++){
            cardWaitForDrawing[i] = i;
        }
    }
    public void randomCardWaitForDrawing(){
        int swap ;
        int k ;
        int times = 6;
        while(true){
            for(int i = 0; i < 52; i++){
                k = (int)(Math.random()*52);
                swap = cardWaitForDrawing[i];
                cardWaitForDrawing[i] = cardWaitForDrawing[k];
                cardWaitForDrawing[k] = swap;
            }
            if(--times == 0) break;
        }
    }
    public int getCardWaitForDrawingWithIndex(int index){ return cardWaitForDrawing[index]; }
    public void arrangeCardWaitForDrawingWithIndex(){
        int swap;
        for(int i = 1 ; i < 13 ; i++){
            for(int j = i ; j > 0 ; j--){
                if (cardWaitForDrawing[j] < cardWaitForDrawing[j-1]){
                    swap = cardWaitForDrawing[j];
                    cardWaitForDrawing[j] = cardWaitForDrawing[j-1];
                    cardWaitForDrawing[j-1] = swap;
                }
            }
        }
    }
    public void arrangeMyCard(){
        for(int i = 1 ; i < MyCard.length ; i++){
            for(int j = i ; j > 0 ; j--){
                int card1 = MyCard[j].getCardIndex();
                int card2 = MyCard[j-1].getCardIndex();
                if (card1 < card2){
                    MyCard[j].setCardIndex(card2);
                    MyCard[j-1].setCardIndex(card1);
                }
            }
        }
    }

    public void initialDealCardAnimator(){
        dealCard.playSequentially(
                MyCard[0].getCardSite().getDealCard(),
                MyCard[1].getCardSite().getDealCard(),
                MyCard[2].getCardSite().getDealCard(),
                MyCard[3].getCardSite().getDealCard(),
                MyCard[4].getCardSite().getDealCard(),
                MyCard[5].getCardSite().getDealCard(),
                MyCard[6].getCardSite().getDealCard(),
                MyCard[7].getCardSite().getDealCard(),
                MyCard[8].getCardSite().getDealCard(),
                MyCard[9].getCardSite().getDealCard(),
                MyCard[10].getCardSite().getDealCard(),
                MyCard[11].getCardSite().getDealCard(),
                MyCard[12].getCardSite().getDealCard(),
                sleep);
    }
    public void playDealCard(){dealCard.start();}
    public AnimatorSet getDealCard(){return dealCard;}

    public void setPriorColor(int priorColor){ this.priorColor = priorColor;}
    public int getPriorColor(){return priorColor;}

    public int getMajorColor(){return majorColor;}
    public void setMajorColor(int majorColor){this.majorColor = majorColor;}

    public int getBridgeWinner(){return bridgeWinner;}
    public void judgeWhoAreBridgeWinner(){
        bridgeWinner = 0;
        int maxCard = -1;
        boolean isPriorColorExist = false;
        for(int i = 0; i < PlayedCard.length; i++){
            if (isPriorColorExist) {
                if (PlayedCard[i].getCardColor() == priorColor && PlayedCard[i].getCardPoint(priorColor) > maxCard){
                    maxCard = PlayedCard[i].getCardPoint(priorColor);
                    bridgeWinner = i;
                }
            }
            else{
                if (PlayedCard[i].getCardColor() == priorColor){
                    isPriorColorExist = true;
                    maxCard = PlayedCard[i].getCardPoint(priorColor);
                    bridgeWinner = i;
                }
                else if (PlayedCard[i].getCardColor() == majorColor && PlayedCard[i].getCardPoint(priorColor) > maxCard){
                    maxCard = PlayedCard[i].getCardPoint(priorColor);
                    bridgeWinner = i;
                }
            }
        }
    }

    public void judgeMyCardEnable(int cardColor){
        boolean playOtherColor = true;
        for (int i = 0; i < MyCard.length; i++){
            if (MyCard[i].isPlayed()) continue;
            else if (MyCard[i].getCardColor() == cardColor){
                MyCard[i].getCardSite().setImageResource(R.drawable.rectangle_yellow);
                playOtherColor = false;
            }
            else {
                MyCard[i].getCardSite().setImageResource(R.drawable.available);
                MyCard[i].setEnable(false);
            }
        }
        if (playOtherColor)
            for (int i = 0; i < MyCard.length; i++){
                if (MyCard[i].isPlayed()) continue;
                else MyCard[i].getCardSite().setImageResource(R.drawable.rectangle_yellow);
            }
    }
    public void IAmTheFirstPlayer(){
        for (int i = 0; i < MyCard.length; i++){
            if (MyCard[i].isPlayed()) continue;
            MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
        }
    }

    private void addWinBridge(int playerSite){
        winBridgeCount[playerSite]++;
        WinBridge[playerSite].setImageLevel(winBridgeCount[winBridgeCount[playerSite]]);
    }

    private boolean doesMyTeamWin(){return winBridgeCount[0]+winBridgeCount[2] >= myTeamBridgeNeedToWin;}
    private boolean doesOtherTeamWin(){return winBridgeCount[1]+winBridgeCount[3] >= otherTeamBridgeNeedToWin;}

    public void animationCloseBridge(){
        closeBridge.playTogether(
                PlayedCard[0].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[1].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[2].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[3].getCardSite().getCloseBridgeAnimator(bridgeWinner)
        );
    }

}
