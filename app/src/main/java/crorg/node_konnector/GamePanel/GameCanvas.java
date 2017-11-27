package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {
    private Drawable circle;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){
        Rect imageBounds = canvas.getClipBounds();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_MOVE:

        }
        return true;
    }
}
