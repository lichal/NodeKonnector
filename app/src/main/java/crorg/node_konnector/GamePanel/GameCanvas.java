package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {
    private Drawable circle;

    private ShapeDrawable mDrawable;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int x = 10;
        int y = 10;
        int width = 100;
        int height = 100;

        mDrawable = new ShapeDrawable(new OvalShape());
        // If the color isn't set, the shape uses black as the default.
        mDrawable.getPaint().setColor(0xff74AC23);
        // If the bounds aren't set, the shape can't be drawn.
        mDrawable.setBounds(x, y, x + width, y + height);
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
            case MotionEvent.ACTION_MOVE:

                mDrawable.setBounds(x, y, x +300, y+50);
                invalidate();

        }
        return true;
    }
}
