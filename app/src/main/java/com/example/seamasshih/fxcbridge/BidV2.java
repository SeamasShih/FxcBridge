package com.example.seamasshih.fxcbridge;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seamasshih.fxcbridge.View.BidButton;

/**
 * Created by SeamasShih on 2018/3/30.
 */

public class BidV2 {
    public BidV2(){}

    private int clubs,diamonds,hearts,spades,noTrump;
    public BidButton btnClubs,btnDiamonds,btnHearts,btnSpades,btnNoTrumps;
    public Button btnPass;
    public BidButton[] bidButtonArray = new BidButton[5];
    public TextView bidText;
    public int nowBid,myBid=-2;

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
    public void setMyBid(){
        myBid = -2;
        for (int i = 0; i < bidButtonArray.length; i++){
            if (bidButtonArray[i].getMyBid() != -2) {
                myBid = bidButtonArray[i].getMyBid();
            }
        }
    }
    public int getMyBid(){
        return myBid;
    }
    public boolean readyToBid(){
        return myBid != -2;
    }

    public void resetButtonBid(){
        for (int i = 0; i < bidButtonArray.length; i++){
            bidButtonArray[i].resetMyBid();
        }
    }
    public void setButtonDisable(){
        for (int i = 0; i < bidButtonArray.length; i++){
            bidButtonArray[i].setBtnEnable(false);
        }
        btnPass.setEnabled(false);
    }
    public void setButtonEnable(){
        for (int i = 0; i < bidButtonArray.length; i++){
            bidButtonArray[i].setBtnEnable(true);
        }
        btnPass.setEnabled(true);
    }
    public void setBidUnitsInvisible(){
        for (int i = 0; i < bidButtonArray.length; i++)
            bidButtonArray[i].setVisibility(View.INVISIBLE);
        bidText.setVisibility(View.INVISIBLE);
        btnPass.setVisibility(View.INVISIBLE);
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
        boolean b = false;
        Log.d("Seamas","now = " + nowBid);
        for (int i = 10; i <= 70 ; i+=10){
            if (i > nowBid) {
                clubs = i/10-1;
                Log.d("Seamas","clubs = "+clubs);
                bidButtonArray[0].setIsEnable(clubs);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[0].setBtnEnable(false);
        }

    }
    private void setDiamondsString(){
        boolean b = false;

        for (int i = 11; i <= 71 ; i+=10){
            if (i > nowBid) {
                diamonds = i/10-1;
                bidButtonArray[1].setIsEnable(diamonds);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[1].setBtnEnable(false);
        }

    }
    private void setHeartsString(){
        boolean b = false;

        for (int i = 12; i <= 72 ; i+=10){
            if (i > nowBid) {
                hearts = i/10-1;
                bidButtonArray[2].setIsEnable(hearts);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[2].setBtnEnable(false);
        }

    }
    private void setSpadesString(){
        boolean b = false;

        for (int i = 13; i <= 73 ; i+=10){
            if (i > nowBid) {
                spades = i/10-1;
                bidButtonArray[3].setIsEnable(spades);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[3].setBtnEnable(false);
        }

    }
    private void setNoTrumpString(){
        boolean b = false;

        for (int i = 14; i <= 74 ; i+=10){
            if (i > nowBid) {
                noTrump = i/10-1;
                bidButtonArray[4].setIsEnable(noTrump);
                b = true;
                break;
            }
        }
        if (!b) {
            bidButtonArray[4].setBtnEnable(false);
        }

    }

}
