package com.example.seamasshih.fxcbridge;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SeamasShih on 2018/3/30.
 */

public class Bid {
    public Bid(){}

    private int clubs,diamonds,hearts,spades,noTrump,myBid,nowBid;
    public Button btnClubs,btnDiamonds,btnHearts,btnSpades,btnNoTrumps,btnPass;
    public TextView bidText;

    public int getClubs(){
        return clubs;
    }
    public int getDiamonds(){
        return diamonds;
    }
    public int getHearts(){
        return hearts;
    }
    public int getSpades(){
        return spades;
    }
    public int getNoTrump(){
        return noTrump;
    }
    public void setNowBid(int nowBid){
        this.nowBid = nowBid;
    }

    public void setString(){
        int bidColor = nowBid%10;
        int bidNumber = nowBid/10;
        String s = null;
        switch (bidColor){
            case 0:
                s += "♣";
                break;
            case 1:
                s += "♦";
                break;
            case 2:
                s += "♥";
                break;
            case 3:
                s += "♠";
                break;
            case 4:
                s += "NT";
                break;
            default:
                break;
        }
        s += String.valueOf(bidNumber+1);
        bidText.setText(s);
        setClubsString();
        setDiamondsString();
        setHeartsString();
        setSpadesString();
        setNoTrumpString();
    }
    private void setClubsString(){
        String s = "♣";
        boolean b = false;
        if(btnClubs.isEnabled()){
            for (int i = 10; i <= 70 ; i+=10){
                if (i > nowBid) {
                    s += String.valueOf(i / 10);
                    clubs = i;
                    btnClubs.setText(s);
                    b = true;
                }
            }
            if (!b)
                btnClubs.setText("X");
        }
    }
    private void setDiamondsString(){
        String s = "♦";
        boolean b = false;
        if(btnDiamonds.isEnabled()){
            for (int i = 11; i <= 71 ; i+=10){
                if (i > nowBid) {
                    s += String.valueOf(i / 10);
                    diamonds = i;
                    btnDiamonds.setText(s);
                    b = true;
                }
            }
            if (!b)
                btnDiamonds.setText("X");
        }
    }
    private void setHeartsString(){
        String s = "♥";
        boolean b = false;
        if(btnHearts.isEnabled()){
            for (int i = 12; i <= 72 ; i+=10){
                if (i > nowBid) {
                    s += String.valueOf(i / 10);
                    hearts = i;
                    btnHearts.setText(s);
                    b = true;
                }
            }
            if (!b)
                btnHearts.setText("X");
        }
    }
    private void setSpadesString(){
        String s = "♠";
        boolean b = false;
        if(btnSpades.isEnabled()){
            for (int i = 13; i <= 73 ; i+=10){
                if (i > nowBid) {
                    s += String.valueOf(i / 10);
                    spades = i;
                    btnSpades.setText(s);
                    b = true;
                }
            }
            if (!b)
                btnSpades.setText("X");
        }
    }
    private void setNoTrumpString(){
        String s = "♠";
        boolean b = false;
        if(btnNoTrumps.isEnabled()){
            for (int i = 14; i <= 74 ; i+=10){
                if (i > nowBid) {
                    s += String.valueOf(i / 10);
                    noTrump = i;
                    btnNoTrumps.setText(s);
                    b = true;
                }
            }
            if (!b)
                btnNoTrumps.setText("X");
        }
    }
}
