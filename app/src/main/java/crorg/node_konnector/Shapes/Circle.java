package crorg.node_konnector.Shapes;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by Cheng Li on 11/23/2017.
 */

public class Circle extends KonnectorShape {
    private final int CONNECTIONS = 1;

    public Circle(Shape s, int x, int y){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        this.height = 100;
        this.width = 100;
        setBounds(x, y, x + width, y + height);
    }

}
