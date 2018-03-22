package com.example.seamasshih.fxcbridge;

/**
 * Created by SeamasShih on 2018/3/19.
 */

public class Player {

    public Player(){}

    private int nowPlayer;

    public int getNowPlayer(){ return nowPlayer; }
    public void someonePlayerPlayedCard() { nowPlayer = ++nowPlayer %4; }
    public void setNowPlayer( int nowPlayer ){ this.nowPlayer = nowPlayer; }

}
