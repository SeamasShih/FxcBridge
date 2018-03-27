package com.example.seamasshih.fxcbridge;

import android.animation.Animator;

/**
 * Created by SeamasShih on 2018/3/23.
 */

public class Card {

    public Card(){}

    private int cardIndex;
    private MyCardImageView cardSite;
    private boolean enable = true;
    private boolean played = false;

    public int getCardIndex(){ return cardIndex; }
    public void setCardIndex( int cardIndex ){ this.cardIndex = cardIndex; }
    public int getCardColor(){return cardIndex/13;}
    public int getCardPoint(){return cardIndex%13;}

    public MyCardImageView getCardSite(){ return  cardSite; }
    public void setCardSite(MyCardImageView cardSite ){ this.cardSite = cardSite; }

    public boolean isEnable(){ return enable; }
    public void setEnable( boolean enable ){ this.enable = enable; }

    public boolean isPlayed(){ return played; }
    public void setPlayed( boolean played ){ this.played = played; }


}
