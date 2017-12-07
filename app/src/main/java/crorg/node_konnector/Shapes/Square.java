package crorg.node_konnector.Shapes;

import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;

import java.io.Serializable;

import crorg.node_konnector.Node;

/**
 * Created by d on 11/28/17.
 */

public class Square extends Node implements Serializable {
    public Square(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(Color.BLUE);
        setBounds(x, y, x + width, y + height);
    }
}
