package crorg.node_konnector.Shapes;

import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;

import java.io.Serializable;

import crorg.node_konnector.Node;

/**
 * Created by Cheng Li on 11/23/2017.
 */

public class Circle extends Node implements Serializable{

    public Circle(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(0xff74AC23);
        setBounds(x, y, x + width, y + height);
    }

    public Shape getShapeFromParent() {
        return s;
    }

}
