package com.example.seamasshih.fxcbridge;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
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
            MyCardHat[i] = new Card();
        for (int i = 0; i < PlayedCard.length; i++)
            PlayedCard[i] = new Card();
    }

    public static ImageView[] WinBridge = new ImageView[4];
    public static Card[] MyCard = new Card[13];
    public static Card[] MyCardHat = new Card[13];
    public static Card[] PlayedCard = new Card[4];
    private static int myPlayingCardIndex;
    private static int playedCount;
    private static int[] cardWaitForDrawing = new int[52];

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

}
