package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

import crorg.node_konnector.Bond;
import crorg.node_konnector.Node;
import crorg.node_konnector.Scaler;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View {

    private Scaler scale;

    private ArrayList<Node> shapeArrayList;

    private ArrayList<Bond> bondArrayList;

    private boolean bondingMode;

    private int numSelect;

    private Paint paint;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();

        bondingMode = false;

        numSelect = 0;

        selectedNode1 = null;
        selectedNode2 = null;

        movingNode = null;

        paint = new Paint();
        paint.setColor(Color.DKGRAY);

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
//        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        scale = new Scaler(getWidth(), getHeight());

        for(Bond b: bondArrayList){
            canvas.drawPath(b.getBondPath(), paint);
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

    private Node movingNode;



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
                        // add the neighbor to each other
                        selectedNode1.addNeighborNode(selectedNode2);
                        selectedNode2.addNeighborNode(selectedNode1);

                        // add number of connection to each node
                        selectedNode1.incrementKonnections();
                        selectedNode2.incrementKonnections();

                        // reset the selection to null
                        selectedNode1 = null;
                        selectedNode2 = null;

                        // reset number of shape selected to 0
                        numSelect = 0;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!bondingMode) {
                    for (Node k : shapeArrayList) {
                        if (k.isSelect()) {
                            movingNode = k;
                            k.redraw(x, y);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                    for (Node collide : shapeArrayList) {
                        if (movingNode != collide) {
                            System.out.print("collision: " + collided(movingNode, collide));
                            if(collided(movingNode, collide))
                                movingNode.redraw(collide.getPositionX() + (int) (collide.getWidth() * 1.5), collide.getPositionY() + (int) (collide.getHeight() / 2));
                        }
                    }
                invalidate();
                break;
        }

        return true;
    }

    private boolean intersetctOther(Node dropThis, Node collideOther){
        int x1 = dropThis.getPositionX();
        int y1 = dropThis.getPositionY();
        int width1 = dropThis.getWidth();
        int height1 = dropThis.getHeight();
        int x2 = collideOther.getPositionX();
        int y2 = collideOther.getPositionY();
        int width2 = collideOther.getWidth();
        int height2 = collideOther.getHeight();
        int right1 = x1 + width1;
        int right2 = x2 + width2;
        int bottom1 = y1 + height1;
        int bottom2 = y2 + height2;

        // Check if top-left point is in box
        if (x2 >= x1 && x2 <= right1 && y2 >= y2 && y2 <= bottom1)
            return true;
        // Check if bottom-right point is in box
        if (right2 >= x1 && right2 <= right1 && bottom2 >= y2 && bottom2 <= bottom1)
            return true;
        return false;
    }

    public Rect move;
    public Rect collide;
    public boolean collided(Node dropThis, Node collideOther) {
        move = new Rect(dropThis.getPositionX(), dropThis.getPositionY(), dropThis.getPositionX() + dropThis.getWidth(), dropThis.getPositionY() + dropThis.getHeight());
        collide = new Rect(collideOther.getPositionX(), collideOther.getPositionY(), collideOther.getPositionX() + collideOther.getWidth(), collideOther.getPositionY()+collideOther.getHeight());

        return Rect.intersects(move, collide);
    }

    /******************************************************************
     * Getter for the list of shapes on the canvas
     * @return shapeArrayList - the arraylist holds all shapes
     *****************************************************************/
    public ArrayList<Node> getShapeArrayList(){
        return shapeArrayList;
    }

    public ArrayList<Bond> getBondArrayList(){
        return bondArrayList;
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
