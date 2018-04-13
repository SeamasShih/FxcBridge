package com.example.seamasshih.fxcbridge.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.seamasshih.fxcbridge.GameBoard;
import com.example.seamasshih.fxcbridge.PokerCardResource;
import com.example.seamasshih.fxcbridge.R;

/**
 * Created by SeamasShih on 2018/3/29.
 */

public class MyCardSector extends View {
    public MyCardSector(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        initial();
        setRegion();
    }

    private void initial(){
        for (int i = 0; i < myCardImage.length; i++)
            myCardImage[i] = BitmapFactory.decodeResource(this.getResources(),R.drawable.poker_card_58);
        for (int i = 0; i < cardRegion.length; i++)
            cardRegion[i] = new Region();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mShadowPaint.setColor(Color.GRAY);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAlpha(120);
        for (int i = 0; i < playedCard.length; i++)
            playedCard[i] = false;
        for (int i = 0; i < mPath.length; i++)
            mPath[i] = new Path();
        for (int i = 0; i < cardAvailability.length; i++)
            cardAvailability[i] = true;
    }
    private int n = 13;
    private int cardAmount = -1;
    private Bitmap[] myCardImage = new Bitmap[13];
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;
    private int[] viewSideLength = {screenWidth,screenHeight/3};
    private int[] cardSideLength = {71 , 96};
    private int[] rotationCenter = {screenWidth/2,4*screenHeight};
    private Region[] cardRegion = new Region[13];
    private Region all = new Region();
    private int radius = viewSideLength[1]*3/4;
    private int r = radius - viewSideLength[1]*2/5;
    private int touching = -1;
    private float sweepAngle = 1.5f;
    private float startAngle = 90 - ((n-1)/2 * sweepAngle);
    private boolean isSel = false;
    private int[] myCardList = new int[13];
    private int[] myCardResourceList = new int[13];
    private boolean[] cardAvailability = new boolean[13];
    float[] fgr = new float[2];
    Paint mPaint = new Paint();
    Path[] mPath = new Path[13];
    Paint mShadowPaint = new Paint();
    boolean[] playedCard = new boolean[13];
    private boolean canPlay = false;
    private boolean readToPlay = false;

    public boolean getReadToPlay(){
        return readToPlay;
    }
    public void setReadToPlay(boolean aa){
        readToPlay = aa;
    }
    public void setCanPlay(boolean aa){
        canPlay = aa;
    }
    public void setMyCardImage(int orderNumber , int resId){
        myCardImage[orderNumber].recycle();
        myCardImage[orderNumber] = BitmapFactory.decodeResource(this.getResources(),resId);
        invalidate();
    }
    public void setMyCardList(int order, int cardIndex){
        myCardList[order] = cardIndex;
    }
    public int getMyCardList(int order){
        return myCardList[order];
    }
    public void setMyCardResourceList(int order, int resId){
        myCardResourceList[order] = resId;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewSideLength[0],viewSideLength[1]);
    }

    public void arrangeCard(){
        for(int i = 1 ; i < myCardList.length ; i++){
            for(int j = i ; j > 0 ; j--){
                int card1 = myCardList[j];
                int card2 = myCardList[j-1];
                if (card1 < card2){
                    myCardList[j] = card2;
                    myCardList[j-1] = card1;
                }
            }
        }
    }

    private void setRegion(){
        cardSideLength[0] = myCardImage[0].getWidth();
        cardSideLength[1] = myCardImage[1].getHeight();
        all.set(0,0,viewSideLength[0],viewSideLength[1]);
        float[] pathPoint = new float[2];
        for (int i = 0 ; i < cardRegion.length ; i++) {
            cardRegion[i].setEmpty();
            mPath[i].reset();
        }
        for (int i = 0 , count = 0; i < cardRegion.length ; i++) {
            if (n == 0) continue;
            while(playedCard[i]) {
                i++;
                if (i == 13) break;
            }
            if (i == 13) break;
            startAngle = 90 - ((n-1)/2 * sweepAngle);
            double theta = Math.PI * (startAngle+sweepAngle*count) / 180;
            count++;
            pathPoint[0] =(float) (rotationCenter[0] - (rotationCenter[1]-r)*Math.cos(theta) - cardSideLength[0]/2*Math.sin(theta));
            pathPoint[1] =(float) (rotationCenter[1] - (rotationCenter[1]-r)*Math.sin(theta) + cardSideLength[0]/2*Math.cos(theta));
            mPath[i].moveTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] += cardSideLength[1]*Math.cos(theta);
            pathPoint[1] += cardSideLength[1]*Math.sin(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] += cardSideLength[0]*Math.sin(theta);
            pathPoint[1] -= cardSideLength[0]*Math.cos(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] -= cardSideLength[1]*Math.cos(theta);
            pathPoint[1] -= cardSideLength[1]*Math.sin(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            mPath[i].close();
            cardRegion[i].setPath(mPath[i],new Region(0,0,screenWidth,screenHeight));
            if (i > 0)
                cardRegion[i-1].op(cardRegion[i],Region.Op.DIFFERENCE);
            all.op(cardRegion[i],Region.Op.DIFFERENCE);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canPlay){
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                radius -= viewSideLength[1]*2/5;
            else if (event.getActionMasked() == MotionEvent.ACTION_UP)
                radius += viewSideLength[1]*2/5;
            invalidate();
            return true;
        }
        fgr[0] = event.getX();
        fgr[1] = event.getY();
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            radius -= viewSideLength[1]*2/5;
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1])) && cardAvailability[i]){
                    touching = i;
                }
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1])) && !cardRegion[i].isEmpty() && cardAvailability[i]){
                    touching = i;
                    if (isSel) {
                        isSel = false;
                        radius -= viewSideLength[1] * 2 / 5;
                    }
                }
            }
            if (all.contains((int)(fgr[0]),(int)(fgr[1])) && !isSel){
                isSel = true;
                radius += viewSideLength[1] * 2 / 5;
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (isSel && touching != -1) {
                playedCard[touching] = true;
                n -= 1;
                setRegion();
                GameBoard.getInstance().PlayedCard[0].setCardIndex(myCardList[touching]);
                GameBoard.getInstance().PlayedCard[0].getCardSite().setImageResource(myCardResourceList[touching]);
                unableAllMyCard();
                readToPlay = true;
                canPlay = false;
            }
            else {
                radius += viewSideLength[1] * 2 / 5;
            }
            isSel = false;
            touching = -1;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void judgeMyCardEnable(int cardColor){
        boolean playOtherColor = true;
        for (int i = 0; i < myCardList.length; i++){
            if (playedCard[i]) continue;
            else if (myCardList[i]/13 == cardColor){
                playOtherColor = false;
                cardAvailability[i] = true;
            }
            else {
                cardAvailability[i] = false;
            }
        }
        if (playOtherColor)
            for (int i = 0; i < myCardList.length; i++){
                if (playedCard[i]) continue;
                cardAvailability[i] = true;
            }
    }

    public void enableAllMyCard(){
        for (int i = 0; i < myCardList.length; i++){
            if (playedCard[i]) continue;
            cardAvailability[i] = true;
        }
    }
    public void unableAllMyCard(){
        for (int i = 0; i < myCardList.length; i++){
            if (playedCard[i]) continue;
            cardAvailability[i] = false;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (n == 0) return;
        canvas.save();
        canvas.translate(rotationCenter[0], rotationCenter[1]);
        canvas.rotate(-(n+1)/2*sweepAngle);
        if (isSel && touching!=-1){
            for (int i = 0; i < myCardImage.length; i++) {
                canvas.rotate(sweepAngle);
                while(playedCard[i]) {
                    i++;
                    if (i == 13) break;
                }
                if (i == 13) break;
                if (i != touching) {
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
                    if (!cardAvailability[i])
                        canvas.drawRoundRect(-cardSideLength[0] / 2, -rotationCenter[1] + radius,cardSideLength[0] / 2,cardSideLength[1]-rotationCenter[1] + radius , 4,4,mShadowPaint);
                }
            }
            canvas.restore();
            canvas.drawBitmap(myCardImage[touching],fgr[0]-cardSideLength[0]/2,fgr[1]-cardSideLength[1]/2,mPaint);
        }
        else {
            for (int i = 0; i < myCardImage.length; i++) {
                canvas.rotate(sweepAngle);
                while(playedCard[i]) {
                    i++;
                    if (i == 13) break;
                }
                if (i == 13) break;
                if (i == touching)
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius - cardSideLength[1] / 3, new Paint());
                else {
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
                    if (!cardAvailability[i])
                        canvas.drawRoundRect(-cardSideLength[0] / 2, -rotationCenter[1] + radius,cardSideLength[0] / 2,cardSideLength[1]-rotationCenter[1] + radius , 4,4,mShadowPaint);

                }
            }
            canvas.restore();
        }
    }
    private void drawRegion(Canvas canvas,Region rgn,Paint paint)
    {
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();

        while (iter.next(r)) {
            canvas.drawRect(r, paint);
        }
    }

}
