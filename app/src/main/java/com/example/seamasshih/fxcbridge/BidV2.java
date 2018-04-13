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

    private int clubs,diamonds,hearts,spades,noTrump,myBid=-2;
    public BidButton btnClubs,btnDiamonds,btnHearts,btnSpades,btnNoTrumps,btnPass;
    public BidButton[] bidButtonArray = new BidButton[6];
    public TextView bidText;
    public int nowBid;

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
        for (int i = 0; i < 5; i++){
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
    }
    public void setButtonEnable(){
        for (int i = 0; i < bidButtonArray.length; i++){
            bidButtonArray[i].setBtnEnable(true);
        }
    }
    public void setBidUnitsInvisible(){
        for (int i = 0; i < bidButtonArray.length; i++)
            bidButtonArray[i].setVisibility(View.INVISIBLE);
        bidText.setVisibility(View.INVISIBLE);
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
    }

}
