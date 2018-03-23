package com.example.seamasshih.fxcbridge;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;


/**
 * Created by SeamasShih on 2018/3/23.
 */

public class PlayingCardImageView extends android.support.v7.widget.AppCompatImageView{

    public PlayingCardImageView(Context context){
        super(context);
    }
    public PlayingCardImageView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
    }
    private int thisCardSite;
    public void setThisCardSite(int cardSite){thisCardSite = cardSite;}

    public void closeBridgeAnimator(int bridgeWinner){
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int mWidth = this.getWidth();
        int mHeight = this.getHeight();
        float siteX = this.getX();
        float siteY = this.getY();
        ObjectAnimator animatorTranslationX = ObjectAnimator.ofFloat(this, "translationX", 0, (width-mWidth)/2 - siteX).setDuration(500);
        ObjectAnimator animatorTranslationY = ObjectAnimator.ofFloat(this, "translationY", 0, (height-mHeight)/2 - siteY).setDuration(500);
        ObjectAnimator animatorScaleX;
        ObjectAnimator animatorScaleY;
        switch (thisCardSite){
            case 1:
            case 2:
            case 3:
                animatorScaleX = ObjectAnimator.ofFloat(this, "scaleX",1, 96.0f/80.0f).setDuration(500);
                animatorScaleY = ObjectAnimator.ofFloat(this, "scaleY",1, 96.0f/80.0f).setDuration(500);
                break;
            default:
                animatorScaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 1).setDuration(500);
                animatorScaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 1).setDuration(500);
                break;
        }
        ObjectAnimator animatorToBrideWinnerFromCenter;
        switch (bridgeWinner){
            case 1:
                animatorToBrideWinnerFromCenter = ObjectAnimator.ofFloat(this, "translationX", (width-mWidth)/2 - siteX, -width).setDuration(350);
                break;
            case 2:
                animatorToBrideWinnerFromCenter = ObjectAnimator.ofFloat(this, "translationY", (height-mHeight)/2 - siteY, -height).setDuration(350);
                break;
            case 3:
                animatorToBrideWinnerFromCenter = ObjectAnimator.ofFloat(this, "translationX", (width-mWidth)/2 - siteX, width).setDuration(350);
                break;
            default:
                animatorToBrideWinnerFromCenter = ObjectAnimator.ofFloat(this, "translationY", (height-mHeight)/2 - siteY, height).setDuration(350);
                break;
        }

        AnimatorSet animScaleSet = new AnimatorSet();
        animScaleSet.play(animatorScaleX).with(animatorScaleY);

        AnimatorSet animTranslationSet = new AnimatorSet();
        animTranslationSet.play(animatorTranslationX).with(animatorTranslationY);

        AnimatorSet animatorShiftToBridgeWinner = new AnimatorSet();
        animatorShiftToBridgeWinner.play(animTranslationSet).with(animScaleSet).before(animatorToBrideWinnerFromCenter);
        animatorShiftToBridgeWinner.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PlayingCardImageView.this.setImageResource(R.drawable.poker_card_58);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator backToOriginalSiteX = ObjectAnimator.ofFloat(this, "translationX", 0 , 0).setDuration(10);
        ObjectAnimator backToOriginalSiteY = ObjectAnimator.ofFloat(this, "translationY", 0 ,0).setDuration(10);
        AnimatorSet backAnimSet = new AnimatorSet();
        backAnimSet.play(backToOriginalSiteX).with(backToOriginalSiteY).after(animatorShiftToBridgeWinner);

        backAnimSet.start();
    }

}
