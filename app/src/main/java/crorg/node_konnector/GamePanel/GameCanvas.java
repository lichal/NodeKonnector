package crorg.node_konnector.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import crorg.node_konnector.Bond;
import crorg.node_konnector.GameScreen;
import crorg.node_konnector.Node;
import crorg.node_konnector.Scaler;
import crorg.node_konnector.Structure;

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

    //private GameScreen gameScreen;

    /** Rect for the moving node */
    private Rect move;

    /** Rect for the node being collide */
    private Rect collide;

    private Node firstSelectedShapeToBondWith;
    private Node selectedNode2;
    private Node currentNode;
    private Node movingNode;

    /**  */
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Log.v("MESSAGE#45689", "After super method in game canvas...");

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();





        numSelect = 0;
        typeBond = 0;

        bondingMode = false;
        currentNode = null;
        firstSelectedShapeToBondWith = null;
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

        Log.v("MESSAGE#45689", "Game canvas: LOADED...");
    }

    protected void onDraw(Canvas canvas){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        scale = new Scaler(getWidth(), getHeight());

        for(Bond b: bondArrayList){
            paint.setStrokeWidth(40f);
            paint2.setStrokeWidth(20f);
            paint3.setStrokeWidth(10f);
            b.drawPathLine(canvas, paint, paint2, paint3, paint4, paint5);
        }

        for(Node k: shapeArrayList) {
            k.draw(canvas);
            if(k.isSelect()) {
                k.drawSelect(canvas);
            }
        }
    }

    public void setBondingMode(boolean startBonding, int type){
        this.bondingMode = startBonding;
        this.typeBond = type;
    }

    public int getShapeWidth(){
        return (int)(getHeight()*0.1);
    }




    public void setBondArrayList(ArrayList<Bond> newBonds) {
        bondArrayList = newBonds;
    }

    public void setNodesArrayList(ArrayList<Node> newNodes) {
        shapeArrayList = newNodes;
    }








    // we should separate these - deleting a node for one button, deleting a bond for another (deleting a bond means selecting the two nodes to delete it from, then hitting the button
    public void deleteSelectedNode(){
        if (currentNode != null) {
            Structure.deleteGivenNode(currentNode, bondArrayList, shapeArrayList, currentNode, firstSelectedShapeToBondWith, selectedNode2);
            //firstSelectedShapeToBondWith = null;
            //selectedNode2 = null;
            //currentNode = null;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                // NOT IN BONDING MODE....
                if (!bondingMode) {
                    boolean wasAShapeTouched = false;
                    for (Node k : shapeArrayList) {
                        if(k.checkSelect(x, y)){
                            currentNode = k;
                            wasAShapeTouched = true;
                        }
                    }
                    currentNode = (wasAShapeTouched) ? currentNode : null;
                // IN BONDING MODE...
                } else {
                    if (currentNode != null) {
                        currentNode.setSelect(false);
                        //redraw canvas here???  Get rid of white square?
                    }
                    currentNode = null;
                    Node shapeUserSelectedThisRound = null;
                    boolean wasAShapeTouched = false;
                    for (Node k : shapeArrayList) {
                        if (k.checkSelect(x, y)) {
                            wasAShapeTouched = true;
                            shapeUserSelectedThisRound = k;
                        }
                    }
                    if (!wasAShapeTouched) {
                        firstSelectedShapeToBondWith = null;
                    } else {
                        // Assign firstNodeToBond if it is now blank...
                        if (firstSelectedShapeToBondWith == null) {
                            firstSelectedShapeToBondWith = shapeUserSelectedThisRound;
                        } else {
                            // Otherwise, compare it to the node the user just selected THIS round...
                            if (shapeUserSelectedThisRound != firstSelectedShapeToBondWith) {
                                // Case 1: Change an existing bond to a different type...
                                boolean wasExistingBondChanged = false;
                                for (Bond b : bondArrayList) {
                                    Node one = b.getNode1();
                                    Node two = b.getNode2();
                                    if (((firstSelectedShapeToBondWith == one) || (firstSelectedShapeToBondWith == two))
                                            && ((shapeUserSelectedThisRound == one) || (shapeUserSelectedThisRound == two))) {
                                        b.setBondType(typeBond);
                                        wasExistingBondChanged = true;
                                    }
                                }
                                // Case 2: Otherwise, add a new bond between two previously unbonded nodes...
                                if (!wasExistingBondChanged) {
                                    // Add neighbors to each other, set bond type, increment konnections...
                                    Bond temp = new Bond(firstSelectedShapeToBondWith, shapeUserSelectedThisRound);
                                    bondArrayList.add(temp);
                                    firstSelectedShapeToBondWith.addNeighborNode(shapeUserSelectedThisRound);
                                    shapeUserSelectedThisRound.addNeighborNode(firstSelectedShapeToBondWith);
                                    switch (typeBond) {
                                        case 1:
                                            temp.setBondType(Bond.SINGLE);
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            break;
                                        case 2:
                                            temp.setBondType(Bond.DOUBLE);
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            break;
                                        case 3:
                                            temp.setBondType(Bond.TRIPLE);
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            firstSelectedShapeToBondWith.incrementKonnections();
                                            shapeUserSelectedThisRound.incrementKonnections();
                                            break;
                                    }
                                }
                                // Make sure you deselect first node selected and turn off bonding mode...
                                firstSelectedShapeToBondWith = null;
                                bondingMode = false;    // this doesn't quite work - how to send message to GameScreen to reactivate buttons?
                            }
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!bondingMode) {
                    oneLoop:
                    for (Node k : shapeArrayList) {
                        if (k.isSelect()) {
                            movingNode = k;
                            k.redraw(x, y);
                            break oneLoop;
                        }
                    }
                    movingNode = null;
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
