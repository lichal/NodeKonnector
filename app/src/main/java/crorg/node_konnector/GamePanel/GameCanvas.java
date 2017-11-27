package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import crorg.node_konnector.Shapes.Circle;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {

    private Circle mDrawable;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mDrawable = new Circle(new OvalShape(), 200, 10, 100, 100);
        // If the color isn't set, the shape uses black as the default.
        mDrawable.getPaint().setColor(0xff74AC23);
        // If the bounds aren't set, the shape can't be drawn.
    }

    protected void onDraw(Canvas canvas){
        mDrawable.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDrawable.checkSelect(x, y);
            case MotionEvent.ACTION_MOVE:
                if(mDrawable.getSelect()) {
                    mDrawable.redraw(x, y);
                    invalidate();
                }

        }
        return true;
    }
}
