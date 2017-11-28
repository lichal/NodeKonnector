package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import crorg.node_konnector.Shapes.Circle;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.Triangle;
import java.lang.Math;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {

    private Circle mDrawable;

    private Triangle mTriangle;

    private Hexagon mHexagon;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mDrawable = new Circle(new OvalShape(), 200, 10, 100, 100);
        // If the color isn't set, the shape uses black as the default.
        mDrawable.getPaint().setColor(0xff74AC23);
        // If the bounds aren't set, the shape can't be drawn.

        mTriangle = new Triangle(new PathShape(drawTriangle(), 100, 100), 200, 200, 100, 100);
        mTriangle.getPaint().setColor(0xff74AC23);

        mHexagon = new Hexagon(new PathShape(drawHexagon(), 100, 100), 300, 300, 100, 100);
        mHexagon.getPaint().setColor(0xff74AC23);
    }

    protected void onDraw(Canvas canvas){
        mDrawable.draw(canvas);
        mTriangle.draw(canvas);
        mHexagon.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDrawable.checkSelect(x, y);
                mTriangle.checkSelect(x, y);
                mHexagon.checkSelect(x, y);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                if(mDrawable.getSelect()) {
                    mDrawable.redraw(x, y);
                    invalidate();
                }
                if(mTriangle.getSelect()) {
                    mTriangle.redraw(x, y);
                    invalidate();
                }
                if(mHexagon.getSelect()) {
                    mHexagon.redraw(x, y);
                    invalidate();
                }
        }
        return true;
    }

    private Path drawTriangle() {
        Point p1 = new Point();
        p1.x = 50;
        p1.y = 0;

        Point p2 = null, p3 = null;

        p2 = new Point(p1.x - 50, p1.y + 100);
        p3 = new Point(p1.x + 50, p1.y + 100);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    private Path drawHexagon() {
        Point midPoint = new Point();
        midPoint.x = 50;
        midPoint.y = 50;

        Point p1 = null, p2 = null, p3 = null, p4 = null, p5 = null, p6 = null;

        p1 = new Point(midPoint.x-50, midPoint.y);
        p2 = new Point(midPoint.x-25, midPoint.y+50);
        p3 = new Point(midPoint.x+25, midPoint.y+50);
        p4 = new Point(midPoint.x+50, midPoint.y);
        p5 = new Point(midPoint.x+25, midPoint.y-50);
        p6 = new Point(midPoint.x-25, midPoint.y-50);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        path.lineTo(p6.x, p6.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }
}
