package crorg.node_konnector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;

import crorg.node_konnector.Shapes.DrawPath;
import crorg.node_konnector.Shapes.MyPoint;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Bond implements Serializable {
    public static int SINGLE = 1;
    public static int DOUBLE = 2;
    public static int TRIPLE = 3;
    private Node node1;
    private Node node2;
    private int classOfBond;

    private DrawPath bondPath;

    private MyPoint startPoint;
    private MyPoint p2, p3, p4;


    public Bond(Node n1, Node n2) {
        node1 = n1;
        node2 = n2;
        bondPath = new DrawPath();
        classOfBond = Bond.SINGLE;
        classOfBond = 1;
        startPoint = new MyPoint();
        p2 = new MyPoint();
        p3 = new MyPoint();
        p4 = new MyPoint();
    }

    public void setNode1(Node rep1) {
        node1 = rep1;
    }

    public void setNode2(Node rep2) {
        node2 = rep2;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setBondType(int a) {
        if (a == Bond.SINGLE) {
            classOfBond = Bond.SINGLE;
        } else if (a == Bond.DOUBLE) {
            classOfBond = Bond.DOUBLE;
        } else if (a == Bond.TRIPLE) {
            classOfBond = Bond.TRIPLE;
        } else {
            return;
        }
    }

    public int getBondType() {
        return classOfBond;
    }

    public static boolean areEqual(Bond b1, Bond b2) {
        Node b1First = b1.getNode1();
        Node b1Second = b1.getNode2();
        Node b2First = b2.getNode1();
        Node b2Second = b2.getNode2();
        if (((b1First == b2First) || (b1First == b2Second)) && ((b1Second == b2First) || (b1Second == b2Second))) {
            return true;
        }
        return false;
    }

    public DrawPath getBondPath(){
        bondPath.reset();
        bondPath = new DrawPath(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY());
        return bondPath;
    }

    public void drawPathLine(Canvas canvas, Paint paint, Paint paint2, Paint paint3, Paint paint4, Paint paint5){

        if(getBondType() == 3) {
            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint);

            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint2);

            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint3);
        }

        else if(getBondType() == 2){

            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint4);

            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint5);
        }

        else if(getBondType() == 1){
            canvas.drawLine(node1.getMidX(), node1.getMidY(), node2.getMidX(), node2.getMidY(), paint3);
        }

    }
}
