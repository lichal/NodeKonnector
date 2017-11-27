package crorg.node_konnector.Shapes;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by Cheng Li on 11/23/2017.
 */

public class Circle extends KonnectorShape {
    private final int CONNECTIONS = 1;

    public Circle(){

    }

    public Circle(Shape s, int x, int y, int height, int width){
        super(s);
        this.s = s;
        this.positionX = x;
        this.positionY = y;
        this.height = height;
        this.width = width;
        setBounds(x, y, x + width, y + height);
    }

    public boolean checkSelect(int xSelect, int ySelect){
        int leftBound = this.getPositionX();
        int rightBound = this.getPositionX() + this.getWidth();
        int topBound = this.getPositionY();
        int bottomBound = this.getPositionY() + this.getHeight();

        if(xSelect > leftBound && xSelect < rightBound && ySelect > topBound && ySelect < bottomBound){
            this.select = true;
            return this.select;
        }
        this.select = false;
        return this.select;
    }

    public void redraw(int x, int y){
        this.positionX = x - width/2;
        this.positionY = y - height/2;
        setBounds(positionX, positionY, x + width/2, y + height/2);
    }

}
