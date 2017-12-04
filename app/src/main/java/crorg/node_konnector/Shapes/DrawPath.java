package crorg.node_konnector.Shapes;

import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by d on 12/3/17.
 */

public class DrawPath extends Path implements Serializable{

    public DrawPath(){
        super();
    }
    public DrawPath drawTriangle() {
        MyPoint p1 = new MyPoint();
        p1.x = 50;
        p1.y = 0;

        MyPoint p2 = null, p3 = null;

        p2 = new MyPoint(p1.x - 50, p1.y + 100);
        p3 = new MyPoint(p1.x + 50, p1.y + 100);

        DrawPath path = new DrawPath();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    public DrawPath drawHexagon() {
        MyPoint midPoint = new MyPoint();
        midPoint.x = 50;
        midPoint.y = 50;

        MyPoint p1 = null, p2 = null, p3 = null, p4 = null, p5 = null, p6 = null;

        p1 = new MyPoint(midPoint.x-50, midPoint.y);
        p2 = new MyPoint(midPoint.x-25, midPoint.y+50);
        p3 = new MyPoint(midPoint.x+25, midPoint.y+50);
        p4 = new MyPoint(midPoint.x+50, midPoint.y);
        p5 = new MyPoint(midPoint.x+25, midPoint.y-50);
        p6 = new MyPoint(midPoint.x-25, midPoint.y-50);

        DrawPath path = new DrawPath();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        path.lineTo(p6.x, p6.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    public DrawPath drawSquare(){
        MyPoint p1 = new MyPoint();
        p1.x = 0;
        p1.y = 0;

        MyPoint p2, p3, p4;

        p2 = new MyPoint(p1.x+100, p1.y);
        p3 = new MyPoint(p1.x + 100, p2.y + 100);
        p4 = new MyPoint(p1.x, p1.y + 100);

        DrawPath rectangle = new DrawPath();
        rectangle.moveTo(p1.x, p1.y);
        rectangle.lineTo(p2.x,p2.y);
        rectangle.lineTo(p3.x, p3.y);
        rectangle.lineTo(p4.x, p4.y);
        rectangle.lineTo(p1.x, p1.y);

        return rectangle;
    }

    public DrawPath drawBond(int startx, int starty, int endx, int endy){
        DrawPath bondPath = new DrawPath();
        bondPath.reset();
        bondPath.moveTo(startx, starty);
        bondPath.lineTo(endx, endy);
        bondPath.lineTo(endx-10, endy);
        bondPath.lineTo(startx-10, starty);
        bondPath.lineTo(startx, starty);
        return bondPath;
    }
}
