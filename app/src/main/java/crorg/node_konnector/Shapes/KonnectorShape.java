package crorg.node_konnector.Shapes;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

import crorg.node_konnector.Scaler;

/**
 * Created by Cheng Li on 11/23/2017.
 */

public class KonnectorShape extends ShapeDrawable{

    protected int connection;
    protected int positionX;
    protected int positionY;
    protected int height;
    protected int width;
    protected boolean select;
    protected Shape s;

    public KonnectorShape(){
        select = false;
    }

    public KonnectorShape(Shape s){
        super(s);
        this.s = s;
        this.width = Scaler.width;
        this.height = Scaler.width;
    }

    public int getConnection(){
        return connection;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean getSelect(){
        return select;
    }

    public void setConnection(int connection) {
        this.connection = connection;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSelect(boolean select){
        this.select = select;
    }

    public boolean checkSelect(int xSelect, int ySelect) {
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
