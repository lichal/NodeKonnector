package crorg.node_konnector.Shapes;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by d on 11/27/17.
 */

public class Hexagon extends KonnectorShape{

    public Hexagon(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(0xff74AC23);
        setBounds(x, y, x + width, y + height);
    }

}
