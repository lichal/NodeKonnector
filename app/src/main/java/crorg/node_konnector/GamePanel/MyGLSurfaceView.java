package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Cheng Li on 11/26/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView{
    private MyGLRenderer myGLRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myGLRenderer = new MyGLRenderer();
        setRenderer(myGLRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;
    }
}
