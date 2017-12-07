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

import crorg.node_konnector.Bond;
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



    /** Rect for the moving node */
    private Rect move;

    /** Rect for the node being collide */
    private Rect collide;

    private Node selectedNode1;
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

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();





        numSelect = 0;
        typeBond = 0;

        bondingMode = false;
        currentNode = null;
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
        }

        for(Node k: shapeArrayList) {
            k.draw(canvas);
            if(k.isSelect()) {
                k.drawSelect(canvas);
            }
        }
//        if(selectedNode1!=null)
//            selectedNode1.drawSelect(canvas);
//        if(selectedNode2!=null)
//            selectedNode2.drawSelect(canvas);
    }

    public void setBondingMode(boolean startBonding, int type){
        this.bondingMode = startBonding;
        this.typeBond = type;
    }

    public int getShapeWidth(){
        return (int)(getHeight()*0.1);
    }













    // we should separate these - deleting a node for one button, deleting a bond for another (deleting a bond means selecting the two nodes to delete it from, then hitting the button
    public void deleteSelectedNode(){
        if (currentNode != null) {
            Structure.deleteGivenNode(currentNode, bondArrayList, shapeArrayList, currentNode, selectedNode1, selectedNode2);
            selectedNode1 = null;
            selectedNode2 = null;
            currentNode = null;
            invalidate();
        }



//        // case 1:  delete a node = remove all bonds konnected to it, decrement konnections (make SURE if it's a triple bond, etc. that you get rid of THREE konnections for BOTH nodes!) for other nodes
//        Bond temp = null;
//        Bond temp2 = null;
//        Bond temp3 = null;
//        Bond temp4 = null;
//
//        Node one = null;
//        Node two = null;
//
//        if(currentNode!=null){
//            ArrayList<Bond> tempBondToDelete = new ArrayList<Bond>();
//
//            for(Bond b: bondArrayList){
//                if ((b.getNode1() == currentNode) || (b.getNode2() == currentNode)) {
//                    one = b.getNode1();
////                    (b.getNode1() == currentNode) ? currentNode : b.getNode2();
//                    two = b.getNode2();
////                    (b.getNode1() == currentNode) ? currentNode : b.getNode2();
//                    two.removeNeighborNode(one);
//                    one.removeNeighborNode(two);
//                    if (b.getBondType() == Bond.SINGLE) {
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                    } else if (b.getBondType() == Bond.DOUBLE) {
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                    } else if (b.getBondType() == Bond.TRIPLE) {
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                        one.decrementKonnections();
//                        two.decrementKonnections();
//                    }
//                    if(b!=null)
//                        tempBondToDelete.add(b);
//                    for(Node neighbors: shapeArrayList){
//                        if(neighbors!=currentNode)
//                            neighbors.removeNeighborNode(currentNode);
//                    }
//                }
//            }
//
//
//            for(Bond delete:tempBondToDelete){
//                bondArrayList.remove(delete);
//            }
//            tempBondToDelete.clear();
//
//            shapeArrayList.remove(currentNode);
//            selectedNode1=null;
//            selectedNode2=null;
//            // must ALSO any bonds konnecting it...
//
////            currentNode.removeAllNeighborNodes();     // NO! - Konnections are only counted for a SINGLE node, not for the structure as a whole
//            invalidate();
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();







        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (!bondingMode) {
                    int numberOfCurrents = 0;
                    for (Node k : shapeArrayList) {
                        if(k.checkSelect(x, y)){
                            currentNode = k;
                            numberOfCurrents = 1;
                            break;
                        }
                    }

                    if (numberOfCurrents == 1) {




                        // do everything else
                    } else { // user touched empty screen
                        selectedNode2 = null;
                        selectedNode1 = null;
                        currentNode = null;
                    }
                } else {
                    // do bonding stuff here...
                    currentNode = null;
                    for (Node k : shapeArrayList) {
                        if(k.checkSelect(x, y)) {
                            if (numSelect == 0) {
                                selectedNode1 = k;
                                numSelect = 1;
                                break;
                            } else if (numSelect == 1) {
                                if(selectedNode1 != k) {
                                    selectedNode2 = k;
                                    numSelect = 2;
                                    // do stuff immediately
                                    // remove all previous bonds between these two nodes FIRST...
                                    for (Bond b : bondArrayList) {
                                        Node one = b.getNode1();
                                        Node two = b.getNode2();
                                        if (((selectedNode1 == one) || (selectedNode1 == two))
                                                && ((selectedNode2 == one) || (selectedNode2 == two))) {
                                            bondArrayList.remove(b);
                                        }
                                    }

                                    // NOW add bonds...
                                    Bond temp = new Bond(selectedNode1, selectedNode2);
                                    bondArrayList.add(temp);

                                    // add the neighbor to each other, set bond type, increment konnections
                                    selectedNode1.addNeighborNode(selectedNode2);
                                    selectedNode2.addNeighborNode(selectedNode1);
                                    switch (typeBond){
                                        case 1:
                                            temp.setBondType(Bond.SINGLE);
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            break;
                                        case 2:
                                            temp.setBondType(Bond.DOUBLE);
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            break;
                                        case 3:
                                            temp.setBondType(Bond.TRIPLE);
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            selectedNode1.incrementKonnections();
                                            selectedNode2.incrementKonnections();
                                            break;
                                    }
                                    // reset the selection to null
                                    selectedNode1 = null;
                                    selectedNode2 = null;

                                    // reset number of shape selected to 0
                                    numSelect = 0;
                                    break;
                                }
                            } else {
                            }
                        }
                    }
                }


//                if(currentNode!=null)
//                    currentNode.setSelect(false);
//                currentNode = null;
//                selectedNode1 = null;
//                selectedNode2 = null;
//                for (Node k : shapeArrayList) {
//                    if(k.checkSelect(x, y)){
//                        currentNode = k;
//                        break;
//                    }
//                }

                // print results
                String selectedNode1Type = "SelectedNode1Type= " + Structure.printShapeType(selectedNode1);
                String selectedNode2Type = "SelectedNode2Type= " + Structure.printShapeType(selectedNode2);
                String currentNodeType = "CurrentNodeType= " + Structure.printShapeType(currentNode);
                Log.d("TAG",currentNodeType + "\t" + selectedNode1Type + "\t" + selectedNode2Type);
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
