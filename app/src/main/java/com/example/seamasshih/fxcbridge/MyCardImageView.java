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

public class MyCardImageView extends android.support.v7.widget.AppCompatImageView{

    public MyCardImageView(Context context){
        super(context);
    }
    public MyCardImageView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }
    Resources resources = this.getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    float width = (float) dm.widthPixels;
    float height = (float) dm.heightPixels;
    float mX ;
    float mY ;
    private  ObjectAnimator animatorCardTranslationX;
    private  ObjectAnimator animatorCardTranslationY;
    private  ObjectAnimator AnimatorCardRotationY;
    private AnimatorSet dealCard = new AnimatorSet();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mX = getX();
        mY = getY();
    }

    public void initialDealAnimator(){
        animatorCardTranslationX = ObjectAnimator.ofFloat(this, "translationX",width/2.0f - mX,0).setDuration(150);
        animatorCardTranslationY = ObjectAnimator.ofFloat(this, "translationY",height - mY,0).setDuration(150);
        AnimatorCardRotationY = ObjectAnimator.ofFloat(this, "rotationY", 0, 360).setDuration(150);
        dealCard.play(animatorCardTranslationX).with(animatorCardTranslationY).with(AnimatorCardRotationY);
    }
    public AnimatorSet getDealCard(){return dealCard;}

    public void setResourceToAnimatorDealCard(final int resId){
        dealCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setImageResource(R.drawable.poker_card_54);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setImageResource(resId);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
