package crorg.node_konnector;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ryan on 2017-11-27.
 */


public class Node extends ShapeDrawable implements Serializable {
    private int num;
    private int numberKonnections;
    private ArrayList<Node> neighbors;


    protected int positionX;
    protected int positionY;
    protected int height;
    protected int width;
    protected boolean select;

    protected boolean bondingMode;
    protected Shape s;


    public Node(Shape s){
        super(s);
        this.s = s;
        this.width = (int) (Scaler.height * 0.1);
        this.height = (int) (Scaler.height * 0.1);
        numberKonnections = 0;
        neighbors = new ArrayList<Node>();
    }

    public Node(int num) {
        this.num = num;
        numberKonnections = 0;
        neighbors = new ArrayList<Node>();
    }

    public int getNum() {
        return num;
    }

    public void incrementKonnections() {
        numberKonnections++;
    }

    public void decrementKonnections() {
        numberKonnections--;
    }

    public int getNumberOfKonnections() {
        return numberKonnections;
    }

    public void addNeighborNode(Node potentialFriend) {
        if ((neighbors.contains(potentialFriend) == false) && (potentialFriend != this)) {
            neighbors.add(potentialFriend);
        }
    }


    public void removeNeighborNode(Node potentialNeighbor) {
        if (neighbors.contains(potentialNeighbor) == true) {
            neighbors.remove(potentialNeighbor);
        }
    }


    public boolean hasNeighborNode(Node possibleNeighbor) {
        if (neighbors.contains(possibleNeighbor) == true) {
            return true;
        }
        return false;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
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

    public boolean isSelect(){
        return select;
    }





    public int getMidX() {
        return positionX+width/2;
    }

    public int getMidY() {
        return positionY+width/2;
    }



    public boolean checkSelect(int xSelect, int ySelect) {
        int leftBound = this.getPositionX();
        int rightBound = this.getPositionX() + this.getWidth();
        int topBound = this.getPositionY();
        int bottomBound = this.getPositionY() + this.getHeight();

        if(xSelect >= leftBound && xSelect <= rightBound && ySelect >= topBound && ySelect <= bottomBound){
            this.select = true;
            return this.select;
        }

        this.select = false;
        return this.select;
    }

    public void redraw(int x, int y){
        this.positionX = x -width/2;
        this.positionY = y - width/2;
        checkScreen(x, y);
        setBounds(positionX, positionY, positionX + width, positionY + height);
    }

    private void checkScreen(int x, int y){
        if (x < width/2){
            positionX = 0;
            if (y < height/2) {
                positionY = 0;
            }
            if(y > Scaler.height-height/2){
                positionY = Scaler.height - height;
            }
        }
        else if(x > Scaler.width-width/2){
            positionX = Scaler.width -width;
            if(y < height/2){
                positionY = 0;
            }
            if(y > Scaler.height-height/2){
                positionY = Scaler.height-height;
            }
        }
        else if(y < height/2){
            positionY=0;
        }

        else if(y > Scaler.height-height/2) {
            positionY = Scaler.height-height;
        }
    }

}
