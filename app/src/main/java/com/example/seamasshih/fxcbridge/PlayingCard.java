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
    public int getCardPoint(int priorColor){
        switch (priorColor){
            case 11:
                return 12 - cardIndex%13;
            case 5:
                return 12 - (cardIndex+1)%13;
            case 6:
                return 12- (cardIndex+7)%13;
            case 7:
                if (cardIndex%13 == 5)
                    return 12;
                else if((cardIndex+1)%13 - 6 > 0)
                    return 12 - ((cardIndex+1)%13 - 6) *2;
                else
                    return 12 - (6 - (cardIndex+1)%13)*2 + 1;
            case 8:
                if (cardIndex%13 == 5)
                    return 12;
                else if((cardIndex+1)%13 - 6 > 0)
                    return 12 - ((cardIndex+1)%13 - 6)*2 + 1;
                else
                    return 12 - (6 - (cardIndex+1)%13)*2;
            case 9:
                return (cardIndex+7)%13;
            case 10:
                return (cardIndex+1)%13;
            default:
                return cardIndex%13;
        }
    }

    public PlayingCardImageView getCardSite(){ return  cardSite; }
    public void setCardSite( PlayingCardImageView cardSite ){ this.cardSite = cardSite; }

    public boolean isEnable(){ return enable; }
    public void setEnable( boolean enable ){ this.enable = enable; }

    public boolean isPlayed(){ return played; }
    public void setPlayed( boolean played ){ this.played = played; }

}