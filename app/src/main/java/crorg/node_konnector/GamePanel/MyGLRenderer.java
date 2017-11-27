package crorg.node_konnector.GamePanel;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import crorg.node_konnector.Shapes.Circle;

/**
 * Created by Cheng Li on 11/26/2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    /**/
    private Circle mCircle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color of OpenGL view
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mCircle = new Circle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
