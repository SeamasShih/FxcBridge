package com.example.seamasshih.fxcbridge;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SeamasShih on 2018/3/30.
 */

public class Bid {
    public Bid(){}

    private int clubs,diamonds,hearts,spades,noTrump;
    public Button btnClubs,btnDiamonds,btnHearts,btnSpades,btnNoTrumps,btnPass;
    public Button[] bidButtonArray = new Button[6];
    public TextView bidText;
    public int myBid, nowBid;

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
        Log.w("TAG","nowBid:"+nowBid);
    }

    public void setString(){
        int bidColor = nowBid%10;
        int bidNumber = nowBid/10;
        Log.w("TAG","bidColor:"+bidColor+" bidNumber:"+bidNumber);
        String s = "";
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
        s += String.valueOf(bidNumber);
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

        for (int i = 10; i <= 70 ; i+=10){
            if (i > nowBid) {
                s += String.valueOf(i / 10);
                clubs = i;
                bidButtonArray[0].setText(s);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[0].setText("X");
            bidButtonArray[0].setEnabled(false);
        }

    }
    private void setDiamondsString(){
        String s = "♦";
        boolean b = false, isGetValue = false;

        for (int i = 11; i <= 71 ; i+=10){
            if (i > nowBid && !isGetValue) {
                s += String.valueOf(i / 10);
                diamonds = i;
                bidButtonArray[1].setText(s);
                b = true;
                isGetValue = true;
            }
        }
        if (!b) {
            bidButtonArray[1].setText("X");
            bidButtonArray[1].setEnabled(false);
        }

    }
    private void setHeartsString(){
        String s = "♥";
        boolean b = false;

        for (int i = 12; i <= 72 ; i+=10){
            if (i > nowBid) {
                s += String.valueOf(i / 10);
                hearts = i;
                bidButtonArray[2].setText(s);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[2].setText("X");
            bidButtonArray[2].setEnabled(false);
        }

    }
    private void setSpadesString(){
        String s = "♠";
        boolean b = false;

        for (int i = 13; i <= 73 ; i+=10){
            if (i > nowBid) {
                s += String.valueOf(i / 10);
                spades = i;
                bidButtonArray[3].setText(s);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[3].setText("X");
            bidButtonArray[3].setEnabled(false);
        }

    }
    private void setNoTrumpString(){
        String s = "NT";
        boolean b = false;

        for (int i = 14; i <= 74 ; i+=10){
            if (i > nowBid) {
                s += String.valueOf(i / 10);
                noTrump = i;
                bidButtonArray[4].setText(s);
                b = true;
               break;
            }
        }
        if (!b) {
            bidButtonArray[4].setText("X");
            bidButtonArray[4].setEnabled(false);
        }

    }

    public void setButtonEnable(){
        for (int i = 0; i < bidButtonArray.length; i++){
            if (!bidButtonArray[i].getText().toString().equals("X"))
                bidButtonArray[i].setEnabled(true);
        }
    }

    public void setButtonDisable(){
        for (int i = 0; i < bidButtonArray.length; i++)
            bidButtonArray[i].setEnabled(false);
    }

    public void setBidUnitsInvisible(){
        for (int i = 0; i < bidButtonArray.length; i++)
            bidButtonArray[i].setVisibility(View.INVISIBLE);
        bidText.setVisibility(View.INVISIBLE);
    }

}
