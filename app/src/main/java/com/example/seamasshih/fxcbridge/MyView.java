package com.example.seamasshih.fxcbridge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by SeamasShih on 2018/3/20.
 */

public class MyView extends View{

    public MyView(Context context, AttributeSet attrs){
        super(context , attrs) ;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        int vWidth = getResources().getDisplayMetrics().widthPixels;
        int vHeight = getResources().getDisplayMetrics().heightPixels;

        super.onDraw(canvas);

         /*设置背景为白色*/
        canvas.drawColor(Color.rgb(0x4F,0x4F,0x4F));
        Paint paint=new Paint();
          /*去锯齿*/
        paint.setAntiAlias(true);

        paint.setColor(Color.rgb(0x00,0x91,0x00));
        paint.setStyle(Paint.Style.FILL);

        Path path1=new Path();
        path1.moveTo(0, vHeight);
        path1.lineTo(vWidth,vHeight);
        path1.lineTo(vWidth*5/6,vHeight/4);
        path1.lineTo(vWidth/6, vHeight/4);
        path1.close();
        canvas.drawPath(path1, paint);


        paint.setColor(Color.rgb(0x5B,0x4B,0x00));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        Path path2=new Path();
        path2.moveTo(0, vHeight);
        path2.lineTo(vWidth,vHeight);
        path2.lineTo(vWidth*5/6,vHeight/4);
        path2.lineTo(vWidth/6, vHeight/4);
        path2.close();
        canvas.drawPath(path2, paint);

        paint.setColor(Color.rgb(0x00,0x00,0x00));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        Path path3=new Path();
        path3.moveTo(0-5, vHeight+5);
        path3.lineTo(vWidth+5,vHeight+5);
        path3.lineTo(vWidth*5/6+5,vHeight/4-5);
        path3.lineTo(vWidth/6-5, vHeight/4-5);
        path3.close();
        canvas.drawPath(path3, paint);


        paint.setColor(Color.rgb(0x2B,0x1B,0x00));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        Path path4=new Path();
        path4.moveTo(0+5, vHeight-5);
        path4.lineTo(vWidth-5,vHeight-5);
        path4.lineTo(vWidth*5/6-5,vHeight/4+5);
        path4.lineTo(vWidth/6+5, vHeight/4+5);
        path4.close();
        canvas.drawPath(path4, paint);



    }
}
