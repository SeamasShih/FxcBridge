package com.example.seamasshih.fxcbridge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

    public GameBoard(){
        for (int i = 0; i < MyCard.length; i++)
            MyCard[i] = new Card();
        for (int i = 0; i < MyCardHat.length; i++)
            MyCardHat[i] = new CardHat();
        for (int i = 0; i < PlayedCard.length; i++)
            PlayedCard[i] = new PlayingCard();
        for (int i = 0; i <winBridgeCount.length; i++)
            winBridgeCount[i] = 0;
    }

    public static ImageView[] WinBridge = new ImageView[4];
    public static Card[] MyCard = new Card[13];
    public static CardHat[] MyCardHat = new CardHat[13];
    public static PlayingCard[] PlayedCard = new PlayingCard[4];
    private int winBridgeCount[] = new int[4];
    private int myTeamBridgeNeedToWin;
    private int otherTeamBridgeNeedToWin;
    private static int myPlayingCardIndex = 0;
    private static int playedCount = 0;
    private static int[] cardWaitForDrawing = new int[52];
    private static int bridgeWinner = 0;
    private static int priorColor = 0;
    private static int majorColor = 0;
    private static AnimatorSet dealCard = new AnimatorSet();
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
            Log.d("TAG","player == "+i);
            if (isPriorColorExist) {
                if (PlayedCard[i].getCardColor() == priorColor && PlayedCard[i].getCardIndex() > maxCard){
                    maxCard = PlayedCard[i].getCardIndex();
                    bridgeWinner = i;
                }
            }
            else{
                if (PlayedCard[i].getCardColor() == priorColor){
                    isPriorColorExist = true;
                    maxCard = PlayedCard[i].getCardIndex();
                    bridgeWinner = i;
                }
                else if (PlayedCard[i].getCardColor() == majorColor && PlayedCard[i].getCardIndex() > maxCard){
                    maxCard = PlayedCard[i].getCardIndex();
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
                MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
                playOtherColor = false;
            }
            else {
                MyCardHat[i].getCardSite().setImageResource(R.drawable.hat);
                MyCard[i].setEnable(false);
            }
        }
        if (playOtherColor)
            for (int i = 0; i < MyCard.length; i++){
                if (MyCard[i].isPlayed()) continue;
                else MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
            }
    }
    public void enableAllMyCard(){
        for (int i = 0; i < MyCard.length; i++){
            if (MyCard[i].isPlayed()) continue;
            MyCardHat[i].getCardSite().setImageResource(R.drawable.available);
        }
    }

    public void addWinBridge(int playerSite){
        winBridgeCount[playerSite]++;
    }

    private boolean doesMyTeamWinThisGame(){return winBridgeCount[0]+winBridgeCount[2] >= myTeamBridgeNeedToWin;}
    private boolean doesOtherTeamWinThisGame(){return winBridgeCount[1]+winBridgeCount[3] >= otherTeamBridgeNeedToWin;}
    
    public void animationCloseBridge(){
        closeBridge.playTogether(
                PlayedCard[0].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[1].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[2].getCardSite().getCloseBridgeAnimator(bridgeWinner),
                PlayedCard[3].getCardSite().getCloseBridgeAnimator(bridgeWinner)
        );
        closeBridge.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                WinBridge[bridgeWinner].setImageLevel(winBridgeCount[bridgeWinner]);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        closeBridge.start();
    }

}
