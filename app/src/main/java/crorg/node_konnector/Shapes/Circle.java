package crorg.node_konnector.Shapes;

/**
 * Created by Cheng Li on 11/23/2017.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Circle extends Shape{
    private final int CONNECTIONS = 1;

    public Circle(){

    }
    public Circle(int x, int y, int height, int width){
        this.positionX = x;
        this.positionY = y;
        this.height = height;
        this.width = width;
    }



}
