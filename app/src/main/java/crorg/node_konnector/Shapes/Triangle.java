package crorg.node_konnector.Shapes;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by Cheng Li on 11/23/2017.
 */

public class Triangle extends KonnectorShape {

    public Triangle(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(0xff74AC23);
        setBounds(x, y, x + width, y + height);
    }

}
