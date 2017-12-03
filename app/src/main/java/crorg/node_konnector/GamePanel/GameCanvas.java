package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import crorg.node_konnector.Bond;
import crorg.node_konnector.Bonds.KonnectorBond;
import crorg.node_konnector.Bonds.SingleBond;
import crorg.node_konnector.Node;
import crorg.node_konnector.Scaler;
import crorg.node_konnector.Shapes.Circle;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.KonnectorShape;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {

    private Scaler scale;

    private ArrayList<Node> shapeArrayList;

    private ArrayList<Bond> bondArrayList;

    private boolean bondingMode;

    private int numSelect;

    private Path drawBonding;

    private int startx;
    private int starty;
    private int touchx;
    private int touchy;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();

        bondingMode = false;

        numSelect = 0;

        selectedNode1 = null;
        selectedNode2 = null;

        drawBonding = new Path();

        startx = 0;
        starty = 0;
        touchx = 0;
        touchy = 0;

//        mDrawable = new Circle(new OvalShape(), 200, 10);
//        // If the color isn't set, the shape uses black as the default.
//        mDrawable.getPaint().setColor(0xff74AC23);
//        // If the bounds aren't set, the shape can't be drawn.
//
//        mTriangle = new Triangle(new PathShape(drawTriangle(), 100, 100), 200, 200);
//        mTriangle.getPaint().setColor(0xff74AC23);
//
//        mHexagon = new Hexagon(new PathShape(drawHexagon(), 100, 100), 300, 300);
//        mHexagon.getPaint().setColor(0xff74AC23);
//
//        mSquare = new Square(new PathShape(drawSquare(), 100, 100), 200, 300);
//        mSquare.getPaint().setColor(0xff74AC23);

    }

    protected void onDraw(Canvas canvas){
        scale = new Scaler(getWidth(), getHeight());

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(100f);

        for(Bond b: bondArrayList){
            canvas.drawPath(b.drawSingleBond(), paint);
        }

        for(Node k: shapeArrayList) {
            k.draw(canvas);
        }
    }

    public void setBondingMode(boolean startBonding){
        this.bondingMode = startBonding;
    }


    private Node selectedNode1;
    private Node selectedNode2;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!bondingMode) {
                    for (Node k : shapeArrayList) {
                        k.checkSelect(x, y);
                    }
                }
                // bonding mode
                if(bondingMode) {
                    for (Node k : shapeArrayList) {
                        if(k.checkSelect(x, y)) {
                            if (numSelect == 1) {
                                if(selectedNode1 != k) {
                                    selectedNode2 = k;
                                    numSelect++;
                                }
                            }

                            if (numSelect == 0) {
                                selectedNode1 = k;
                                numSelect++;
                            }
                        }
                    }
                    if(numSelect == 2){
                        bondArrayList.add(new Bond(selectedNode1, selectedNode2));
                        selectedNode1 = null;
                        selectedNode2 = null;
                        numSelect = 0;
                    }
                }
                invalidate();
            case MotionEvent.ACTION_MOVE:
                if(!bondingMode) {
                    for (Node k : shapeArrayList) {
                        if (k.isSelect()) {
                            k.redraw(x, y);
                        }
                    }
                }
                invalidate();
        }
        return true;
    }

    private Path drawBond(int startPointX, int startPointY, int touchX, int touchY){
        Point startPoint = new Point();
        startPoint.x = startPointX;
        startPoint.y = startPointY;

        Point p2, p3, p4;

        p2 = new Point(touchX, touchY);
        p3 = new Point(p2.x-10, p2.y);
        p4 = new Point(startPoint.x-10, startPoint.y);

        Path bond = new Path();

        bond.moveTo(startPoint.x, startPoint.y);
        bond.lineTo(p2.x,p2.y);
        bond.lineTo(p3.x, p3.y);
        bond.lineTo(p4.x, p4.y);
        bond.lineTo(startPoint.x, startPoint.y);

        return bond;
    }

    /******************************************************************
     * Getter for the list of shapes on the canvas
     * @return shapeArrayList - the arraylist holds all shapes
     *****************************************************************/
    public ArrayList getShapeArrayList(){
        return shapeArrayList;
    }

    private Path drawTriangle() {
        Point p1 = new Point();
        p1.x = 50;
        p1.y = 0;

        Point p2 = null, p3 = null;

        p2 = new Point(p1.x - 50, p1.y + 100);
        p3 = new Point(p1.x + 50, p1.y + 100);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    private Path drawHexagon() {
        Point midPoint = new Point();
        midPoint.x = 50;
        midPoint.y = 50;

        Point p1 = null, p2 = null, p3 = null, p4 = null, p5 = null, p6 = null;

        p1 = new Point(midPoint.x-50, midPoint.y);
        p2 = new Point(midPoint.x-25, midPoint.y+50);
        p3 = new Point(midPoint.x+25, midPoint.y+50);
        p4 = new Point(midPoint.x+50, midPoint.y);
        p5 = new Point(midPoint.x+25, midPoint.y-50);
        p6 = new Point(midPoint.x-25, midPoint.y-50);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        path.lineTo(p6.x, p6.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    private Path drawSquare(){
        Point p1 = new Point();
        p1.x = 0;
        p1.y = 0;

        Point p2, p3, p4;

        p2 = new Point(p1.x+100, p1.y);
        p3 = new Point(p1.x + 100, p2.y + 100);
        p4 = new Point(p1.x, p1.y + 100);

        Path rectangle = new Path();
        rectangle.moveTo(p1.x, p1.y);
        rectangle.lineTo(p2.x,p2.y);
        rectangle.lineTo(p3.x, p3.y);
        rectangle.lineTo(p4.x, p4.y);
        rectangle.lineTo(p1.x, p1.y);

        return rectangle;
    }
}
