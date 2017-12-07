package crorg.node_konnector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
    private int width;
    private float rotateRate;
    private int rotateHeight;
    private Random rand;
    private int move;

    public StartUpCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tmr = new Timer();

        rand = new Random();
        move = 0;
        width = 0;

        rotateRate = 0f;
        rotateHeight = 0;
        circle = getResources().getDrawable(R.drawable.circle);
        square = getResources().getDrawable(R.drawable.square);
        triangle = getResources().getDrawable(R.drawable.triangle);
        hexagon = getResources().getDrawable(R.drawable.hexagon);

//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                // update rotation
//                rotateRate++;
//                if (rotateRate >= 360) {
//                    int i = rand.nextInt(3) + 1;
//                    rotateHeight = -(width);
////                    switch (i){
////                        case 0:
////                            rotateHeight = width;
////                            move++;
////                            break;
////                        case 1:
////                            rotateHeight = -(width);
////                            move++;
////                            break;
////                        case 2:
////                            rotateHeight = 2*width;
////                            move++;
////                            break;
////                        case 3:
////                            rotateHeight = 3*width;
////                            move = 0;
////                            break;
////                    }
//                    if (rotateRate >= 359) {
//                        rotateRate = 0;
//                    }
//                    // ask for the view to be redrawn
//                    invalidate();
//                }
//            }
//        };
//        tmr.schedule(task,0,10);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // update the y coordinate in c
                rotateRate++;
                if(rotateRate>=360){
                    rotateHeight = -(width);
                    rotateRate=0;
                }
                Log.d("TAG", "Got HERE!");
                // ask for the view to be redrawn
                //invalidate();
            }
        };
        tmr.schedule(task, 0, 10);
    }


    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        width = getWidth()/9;

        int circleX = getWidth()/2+width/2*2;
        int circleY = getHeight()/2-(int)(width*3.5) + 2*width;

        int hexX = getWidth()/2-width/2;
        int hexY = getHeight()/2-(int)(width*1.5) +2*width;

        int triX = getWidth()/2 - width*2;
        int triY = getHeight()/2+width/2 +width;

        int sqrX = getWidth()/2 + width;
        int sqrY = getHeight()/2+width/2+2*width;

        // rotate the canvas
        canvas.rotate(rotateRate,canvas.getWidth()/2+width/2,canvas.getHeight()/2+rotateHeight);
        canvas.rotate(rotateRate,canvas.getWidth()/2+width/2,canvas.getHeight()/2);

        Paint doubleBond1 = new Paint();
        Paint doubleBond2 = new Paint();
        doubleBond1.setColor(Color.WHITE);
        doubleBond1.setStrokeWidth(15f);
        doubleBond2.setColor(Color.BLACK);
        doubleBond2.setStrokeWidth(5f);

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
