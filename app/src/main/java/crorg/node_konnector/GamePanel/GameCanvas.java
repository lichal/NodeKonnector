package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import crorg.node_konnector.Bond;
import crorg.node_konnector.Node;
import crorg.node_konnector.Scaler;

/**
 * Created by Cheng on 11/27/17.
 */

public class GameCanvas extends View implements Serializable {

    private Scaler scale;

    private ArrayList<Node> shapeArrayList;

    private ArrayList<Bond> bondArrayList;

    private boolean bondingMode;

    private int numSelect;

    private int typeBond;



    /** Rect for the moving node */
    private Rect move;

    /** Rect for the node being collide */
    private Rect collide;

    private Node selectedNode1;
    private Node selectedNode2;

    private Node movingNode;


    /**  */
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();

        bondingMode = false;

        numSelect = 0;
        typeBond = 0;

        selectedNode1 = null;
        selectedNode2 = null;

        movingNode = null;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint2 = new Paint();
        paint2.setColor(Color.DKGRAY);
        paint3 = new Paint();

        paint3.setColor(0xffff00ff);

        paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setStrokeWidth(20);
        paint5 = new Paint();
        paint5.setStrokeWidth(5);
        paint5.setColor(Color.DKGRAY);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        scale = new Scaler(getWidth(), getHeight());

        for(Bond b: bondArrayList){
            paint.setStrokeWidth(40f);
            paint2.setStrokeWidth(20f);
            paint3.setStrokeWidth(10f);
            b.drawPathLine(canvas, paint, paint2, paint3, paint4, paint5);
//            canvas.drawPath(b.getBondPath(), paint);
        }

        for(Node k: shapeArrayList) {
            k.draw(canvas);
        }
    }

    public void setBondingMode(boolean startBonding, int type){
        this.bondingMode = startBonding;
        this.typeBond = type;
    }

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
                            if (numSelect == 0) {
                                selectedNode1 = k;
                                numSelect++;
                            }
                            if (numSelect == 1) {
                                if(selectedNode1 != k) {
                                    selectedNode2 = k;
                                    numSelect++;
                                }
                            }
                        }
                    }
                    if(numSelect == 2){
                        Bond temp = new Bond(selectedNode1, selectedNode2);
                        bondArrayList.add(temp);
                        // add the neighbor to each other
                        selectedNode1.addNeighborNode(selectedNode2);
                        selectedNode2.addNeighborNode(selectedNode1);

                        // add number of connection to each node
                        selectedNode1.incrementKonnections();
                        selectedNode2.incrementKonnections();

                        // reset the selection to null
                        selectedNode1 = null;
                        selectedNode2 = null;
                        switch (typeBond){
                            case 1:
                                temp.setBondType(Bond.SINGLE);
                                break;
                            case 2:
                                temp.setBondType(Bond.DOUBLE);
                                break;
                            case 3:
                                temp.setBondType(Bond.TRIPLE);
                                break;
                        }

                        // reset number of shape selected to 0
                        numSelect = 0;
                    }
                }
                invalidate();
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
                        if (movingNode != collide && movingNode!=null) {
                            if(collides(movingNode, collide))
                                movingNode.redraw(collide.getPositionX() + (int) (collide.getWidth() * 1.5), collide.getPositionY() + (int) (collide.getHeight() / 2));
                        }
                    }
                invalidate();
                break;
        }

        return true;
    }

    /******************************************************************
     * Detect collision, determine if two shape overlap
     * @param dropThis - the node being drop
     * @param collideOther - the node being collide
     * @return boolean if the node collides
     *****************************************************************/
    private boolean collides(Node dropThis, Node collideOther) {
        move = new Rect(dropThis.getPositionX(),
                dropThis.getPositionY(),
                dropThis.getPositionX() + dropThis.getWidth(),
                dropThis.getPositionY() + dropThis.getHeight());
        collide = new Rect(collideOther.getPositionX(),
                collideOther.getPositionY(),
                collideOther.getPositionX() + collideOther.getWidth(),
                collideOther.getPositionY()+collideOther.getHeight());

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


}
