package com.example.seamasshih.fxcbridge;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

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
    private ObjectAnimator animatorCardTranslationX;
    private ObjectAnimator animatorCardTranslationY;
    private ObjectAnimator animatorCardRotationYPartOne;
    private ObjectAnimator animatorCardRotationYPartTwo;
    private ObjectAnimator animatorCardRotationYPartThree;
    private ObjectAnimator animatorCardRotationYPartFour;
    private AnimatorSet rotationCard = new AnimatorSet();
    private AnimatorSet dealCard = new AnimatorSet();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mX = getX();
        mY = getY();
    }

    public void initialDealAnimator(){
        animatorCardTranslationX = ObjectAnimator.ofFloat(this, "translationX",width/2.0f - mX,0).setDuration(300);
        animatorCardTranslationY = ObjectAnimator.ofFloat(this, "translationY",height - mY - 150,0).setDuration(300);
        animatorCardRotationYPartOne = ObjectAnimator.ofFloat(this, "rotationY", 0, -90).setDuration(50);
        animatorCardRotationYPartTwo = ObjectAnimator.ofFloat(this, "rotationY", 90, -90).setDuration(100);
        animatorCardRotationYPartThree = ObjectAnimator.ofFloat(this, "rotationY", 90, -90).setDuration(100);
        animatorCardRotationYPartFour = ObjectAnimator.ofFloat(this, "rotationY", 90, 0).setDuration(50);
        rotationCard.playSequentially(
                animatorCardRotationYPartOne,
                animatorCardRotationYPartTwo,
                animatorCardRotationYPartThree,
                animatorCardRotationYPartFour);
        dealCard.play(animatorCardTranslationX).with(animatorCardTranslationY).with(rotationCard);
    }
    public AnimatorSet getDealCard(){return dealCard;}

    public void setResourceToAnimatorDealCard(final int resId){
        animatorCardRotationYPartOne.addListener(new Animator.AnimatorListener() {
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
        animatorCardRotationYPartTwo.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setImageResource(R.drawable.poker_card_54);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorCardRotationYPartThree.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
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
