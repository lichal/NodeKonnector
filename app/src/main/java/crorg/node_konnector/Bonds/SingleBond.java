package crorg.node_konnector.Bonds;

import android.graphics.Path;
import android.graphics.Point;

import crorg.node_konnector.Bonds.KonnectorBond;

/**
 * Created by d on 12/2/17.
 */

public class SingleBond extends KonnectorBond{
    private int startX;
    private int startY;
    private int touchX;
    private int touchY;

    private Point start1;
    private Point start2;
    private Point touch1;
    private Point touch2;

    private Point startPoint;
    private Point p2;
    private Point p3;
    private Point p4;

    public SingleBond(int startX, int startY, int touchX, int touchY){
        this.startX = startX;
        this.startY = startY;
        this.touchX = touchX;
        this.touchY = touchY;
//
//        start1 = new Point();
//        start2 = new Point();
//        touch1 = new Point();
//        touch2 = new Point();

        startPoint = new Point();
        startPoint.x = startX;
        startPoint.y = startY;

        p2 = new Point(touchX, touchY);
        p3 = new Point(p2.x-10, p2.y);
        p4 = new Point(startPoint.x-10, startPoint.y);

        moveTo(startPoint.x, startPoint.y);
        lineTo(p2.x,p2.y);
        lineTo(p3.x, p3.y);
        lineTo(p4.x, p4.y);
        lineTo(startPoint.x, startPoint.y);

    }

//    public Path getPath(){
//        return this;
//    }
//
//    public Path drawBond(){
//        start1.x = startX;
//        start1.y = startY;
//
//        start2.x = startX;
//        start2.y = startY+10;
//
//        touch1.x = touchX;
//        touch1.y = touchY;
//
//        touch2.x = touchX;
//        touch2.y = touchY + 10;
//
//        moveTo(start1.x, start1.y);
//        lineTo(start2.x, start2.y);
//        lineTo(touch2.x, touch2.y);
//        lineTo(touch1.x, touch1.y);
//        lineTo(start1.x, start1.y);
//        close();
//
//        return this;
//    }
}
