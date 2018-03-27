package com.example.seamasshih.fxcbridge;

import android.widget.ImageView;

/**
 * Created by SeamasShih on 2018/3/23.
 */

public class PlayingCard {

    public PlayingCard(){}

    private int cardIndex;
    private PlayingCardImageView cardSite;
    private boolean enable = true;
    private boolean played = false;


    public int getCardIndex(){ return cardIndex; }
    public void setCardIndex( int cardIndex ){ this.cardIndex = cardIndex; }
    public int getCardColor(){return cardIndex/13;}
    public int getCardPoint(){return cardIndex%13;}

    public PlayingCardImageView getCardSite(){ return  cardSite; }
    public void setCardSite( PlayingCardImageView cardSite ){ this.cardSite = cardSite; }

    public boolean isEnable(){ return enable; }
    public void setEnable( boolean enable ){ this.enable = enable; }

    public boolean isPlayed(){ return played; }
    public void setPlayed( boolean played ){ this.played = played; }

}