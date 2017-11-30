package crorg.node_konnector.Shapes;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by d on 11/28/17.
 */

public class Square extends KonnectorShape{
    public Square(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        getPaint().setColor(0xff74AC23);
        setBounds(x, y, x + width, y + height);
    }
}
