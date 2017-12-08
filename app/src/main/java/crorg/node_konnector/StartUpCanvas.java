package crorg.node_konnector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by d on 12/6/17.
 */

public class StartUpCanvas extends View implements Serializable {
    private Timer tmr;
    private Drawable circle;
    private Drawable square;
    private Drawable triangle;
    private Drawable hexagon;
    private Drawable title;
    private int width;
    private float rotateRate;

    public StartUpCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tmr = new Timer();
        width = 0;

        rotateRate = 0f;
        circle = getResources().getDrawable(R.drawable.circle);
        square = getResources().getDrawable(R.drawable.square);
        triangle = getResources().getDrawable(R.drawable.triangle);
        hexagon = getResources().getDrawable(R.drawable.hexagon);
        title = getResources().getDrawable(R.drawable.title);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        width = getWidth()/9;

        int titleX = width/2;
        // x y position for circle
        int titleY = getWidth()/5;
        int titleWidth = getWidth()-width/2;
        int titleHeight = getHeight()/4;
        int circleX = getWidth()/2+width/2*2;
        int circleY = getHeight()/2-(int)(width*3.5) + 2*width;

        // x y position for hexagon
        int hexX = getWidth()/2-width/2;
        int hexY = getHeight()/2-(int)(width*1.5) +2*width;

        // x y position for triangle
        int triX = getWidth()/2 - width*2;
        int triY = getHeight()/2+width/2 +width;

        // x y position square
        int sqrX = getWidth()/2 + width;
        int sqrY = getHeight()/2+width/2+2*width;

        // rotate the canvas
//        canvas.rotate(rotateRate,canvas.getWidth()/2+width/2,canvas.getHeight()/2+rotateHeight);


        canvas.rotate(rotateRate,canvas.getWidth()/2+width/2,canvas.getHeight()/2);

        Paint doubleBond1 = new Paint();
        Paint doubleBond2 = new Paint();
        doubleBond1.setColor(Color.WHITE);
        doubleBond1.setStrokeWidth(15f);
        doubleBond2.setColor(Color.BLACK);
        doubleBond2.setStrokeWidth(5f);

        title.setBounds(titleX, titleY, titleWidth, titleY + titleHeight);
        title.draw(canvas);

        // from triangle to hexagon double bond
        canvas.drawLine(triX + width/2, triY+20, hexX+20,hexY+width/2, doubleBond1);
        canvas.drawLine(triX + width/2, triY+20, hexX+20, hexY+width/2, doubleBond2);

        doubleBond1.setStrokeWidth(5f);

        //from sqaure to hexagon single bond
        canvas.drawLine(sqrX, sqrY, hexX+width/4*3, hexY+width-10, doubleBond1);

        // connection from triangle to square
        canvas.drawLine(triX+width, triY+width-5, sqrX, sqrY+width-5, doubleBond1);

        // connection from hexagon to circle
        canvas.drawLine(hexX+width/4*3, hexY, circleX+width/2, circleY+width/2, doubleBond1);

        circle.setBounds(circleX, circleY, circleX+width, circleY+width);
        circle.draw(canvas);

        triangle.setBounds(triX, triY, triX+width, triY+width);
        triangle.draw(canvas);

        hexagon.setBounds(hexX, hexY, hexX+width, hexY+width);
        hexagon.draw(canvas);

        square.setBounds(sqrX, sqrY, sqrX+width, sqrY+width);
        square.draw(canvas);
    }
}
