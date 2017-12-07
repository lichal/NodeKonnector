package crorg.node_konnector.Shapes;

import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;

import java.io.Serializable;

import crorg.node_konnector.Node;

/**
 * Created by d on 11/27/17.
 */

public class Hexagon extends Node implements Serializable{

    public Hexagon(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(Color.RED);
        setBounds(x, y, x + width, y + height);
    }

}
