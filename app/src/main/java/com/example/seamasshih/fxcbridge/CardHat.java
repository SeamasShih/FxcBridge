package com.example.seamasshih.fxcbridge;

import android.widget.ImageView;

/**
 * Created by SeamasShih on 2018/3/19.
 */

public class CardHat {

    public CardHat(){}

    private int cardIndex;
    private ImageView cardSite;
    private boolean enable = true;
    private boolean played = false;

    public int getCardIndex(){ return cardIndex; }
    public void setCardIndex( int cardIndex ){ this.cardIndex = cardIndex; }

    public ImageView getCardSite(){ return  cardSite; }
    public void setCardSite( ImageView cardSite ){ this.cardSite = cardSite; }

    public boolean isEnable(){ return enable; }
    public void setEnable( boolean enable ){ this.enable = enable; }

    public boolean isPlayed(){ return played; }
    public void setPlayed( boolean played ){ this.played = played; }

}
