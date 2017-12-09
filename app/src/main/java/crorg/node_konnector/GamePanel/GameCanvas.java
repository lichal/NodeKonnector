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

import crorg.node_konnector.Bond;
import crorg.node_konnector.GameScreen;
import crorg.node_konnector.Node;
import crorg.node_konnector.Scaler;
import crorg.node_konnector.Shapes.Circle;
import crorg.node_konnector.Shapes.DrawPath;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.MyOvalShape;
import crorg.node_konnector.Shapes.MyPathShape;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;
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

    private boolean isDrawingUpdated;

    //private GameScreen gameScreen;

    /** Rect for the moving node */
    private Rect move;

    /** Rect for the node being collide */
    private Rect collide;

    private Node firstSelectedShapeToBondWith;
    private Node secondSelectedShapeToBondWith;
    private Node selectedNode2;
    private Node currentNode;
    private Node movingNode;

    /**  */
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private DrawPath drawShape;

    private GameScreen gameScreen;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawShape = new DrawPath();

        //Log.v("MESSAGE#45689", "After super method in game canvas...");

        shapeArrayList = new ArrayList<Node>();

        bondArrayList = new ArrayList<Bond>();

        isDrawingUpdated = false;
        //this.gameScreen = gameScreen;



        numSelect = 0;
        typeBond = Bond.SINGLE;

        bondingMode = false;
        currentNode = null;
        firstSelectedShapeToBondWith = null;
        secondSelectedShapeToBondWith = null;
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

        //Log.v("MESSAGE#45689", "Game canvas: LOADED...");

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
        isDrawingUpdated = true;
    }

   // public boolean setIsDrawingUpdated(boolean bool) {
       // isDrawingUpdated = bool;
    //}


    public void setBondingMode(boolean startBonding, int type){
        this.bondingMode = startBonding;
        this.typeBond = type;
    }

    public int getShapeWidth(){
        return (int)(getHeight()*0.1);
    }




//    public void setBondArrayList(ArrayList<Bond> newBonds) {
//        bondArrayList = newBonds;
//    }
//
//    public void setNodesArrayList(ArrayList<Node> newNodes) {
//        Log.v("MESSAGE#45689", "Assigning file nodes to user current nodes...");
//        shapeArrayList = newNodes;
//        Log.v("MESSAGE#45689", "... Success. Nodes assigned!");
//    }


    // we should separate these - deleting a node for one button, deleting a bond for another (deleting a bond means selecting the two nodes to delete it from, then hitting the button
    public void deleteSelectedNode(){
        Log.v("ERROR44", "Preparing to delete node...");
        Structure.deleteGivenNode(currentNode, bondArrayList, shapeArrayList);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                actionDownMethod(x, y);
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
                printAllNodes();
                printAllBonds();
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



//    public void cycleCurrentNodeToNextShape() {
//        Circle circle = new Circle(new MyOvalShape(), currentNode.getMidX(), currentNode.getMidY());
//        Square square = new Square(new MyPathShape(drawShape.drawSquare(), 100, 100), currentNode.getMidX(), currentNode.getMidY());
//        Triangle triangle = new Triangle(new MyPathShape(drawShape.drawTriangle(), 100, 100), currentNode.getMidX(), currentNode.getMidY());
//        Hexagon hexagon = new Hexagon(new MyPathShape(drawShape.drawHexagon(), 100, 100), currentNode.getMidX(), currentNode.getMidY());
//        if (currentNode != null) {
//            if (currentNode instanceof Circle) {
//                try {
//                    currentNode =(Square) currentNode;
//                    currentNode.setShape(square.getShapeFromParent());
//                } catch (ClassCastException c) {
//                }
//            } else if (currentNode instanceof Square) {
//                try {
//                    currentNode =(Triangle) currentNode;
//                    currentNode.setShape(triangle.getShapeFromParent());
//                } catch (ClassCastException c) {
//                }
//            } else if (currentNode instanceof Triangle) {
//                try {
//                    currentNode =(Hexagon) currentNode;
//                    currentNode.setShape(hexagon.getShapeFromParent());
//                } catch (ClassCastException c) {
//                }
//            } else if (currentNode instanceof Hexagon) {
//                try {
//                    currentNode =(Circle) currentNode;
//                    currentNode.setShape(circle.getShapeFromParent());
//                } catch (ClassCastException c) {
//                }
//            }
//        }
//    }



    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    // EXPERIMENTAL
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private void actionDownMethod(int x, int y) {
        if (bondingMode) {
            processInBondingMode(x, y);
        } else {
            processNotInBondingMode(x, y);
        }
        if (3==3) {
            // change shape to different one
            return;
        }
        if (2==1) {
            // turn on REMOVE_BONDING MODE
                return;
        }
    }


    private void processNotInBondingMode(int x, int y) {
        for (Node k : shapeArrayList) {
            if (k.checkSelect(x, y)) {
                currentNode = k;
                return;
            }
        }
        currentNode = null;
        firstSelectedShapeToBondWith = null;
        deselectAll();
    }


    private void processInBondingMode(int x, int y) {
        deselectAll();
        currentNode = null;
        Node shapeUserSelectedThisRound = null;
        boolean wasAShapeTouched = false;
        mainLoop:
        for (Node k : shapeArrayList) {
            if (k.checkSelect(x, y)) {
                wasAShapeTouched = true;
                shapeUserSelectedThisRound = k;
                break mainLoop;
            }
        }

        if (wasAShapeTouched) {
            // Assign firstNodeToBond if it is now blank...
            if (firstSelectedShapeToBondWith == null) {
                firstSelectedShapeToBondWith = shapeUserSelectedThisRound;
            } else {
                // Otherwise, compare it to the node the user just selected THIS round...
                if (shapeUserSelectedThisRound != firstSelectedShapeToBondWith) {
                    startBondingProcess(shapeUserSelectedThisRound, firstSelectedShapeToBondWith);
                }
            }
        } else {
            deselectAll();
            firstSelectedShapeToBondWith = null;
        }
    }


    private void startBondingProcess(Node shapeUserSelectedThisRound, Node previousShapePicked) {
        // Case 1: Change an existing bond to a different type...
        boolean wasExistingBondChanged = false;
        for (Bond b : bondArrayList) {
            Node one = b.getNode1();
            Node two = b.getNode2();
            if (((previousShapePicked == one) && (shapeUserSelectedThisRound == two))
                    || ((previousShapePicked == two) && (shapeUserSelectedThisRound == one))) {
                int currentBondType = b.getBondType();
                int netKonnections = typeBond - currentBondType;
                previousShapePicked.addKonnections(netKonnections);
                shapeUserSelectedThisRound.addKonnections(netKonnections);
                b.setBondType(typeBond);
                wasExistingBondChanged = true;
                deselectAll();
                firstSelectedShapeToBondWith = null;
                currentNode = null;
            }
        }
        // Case 2: Otherwise, add a new bond between two previously unbonded nodes...
        // CLEAR all previous bonds FIRST...
        if (!wasExistingBondChanged) {
            //Structure.clearAllBondsBetweenTwoNodes(shapeUserSelectedThisRound, previousShapePicked, bondArrayList);
            createNewBondBetweenNodes(shapeUserSelectedThisRound, previousShapePicked);
        }
    }


    private void createNewBondBetweenNodes(Node shapeUserSelectedThisRound, Node previousShapePicked) {
        // Add neighbors to each other, set bond type, increment konnections...
        Bond temp = new Bond(previousShapePicked, shapeUserSelectedThisRound);
        previousShapePicked.addNeighborNode(shapeUserSelectedThisRound);
        shapeUserSelectedThisRound.addNeighborNode(previousShapePicked);
        bondArrayList.add(temp);
        switch (typeBond) {
            case 1:
                temp.setBondType(Bond.SINGLE);
                previousShapePicked.addKonnections(1);
                shapeUserSelectedThisRound.addKonnections(1);
                break;
            case 2:
                temp.setBondType(Bond.DOUBLE);
                previousShapePicked.addKonnections(2);
                shapeUserSelectedThisRound.addKonnections(2);
                break;
            case 3:
                temp.setBondType(Bond.TRIPLE);
                previousShapePicked.addKonnections(3);
                shapeUserSelectedThisRound.addKonnections(3);
                break;
        }
        deselectAll();
        firstSelectedShapeToBondWith = null;
        currentNode = null;
    }


    // This method only unhighlights nodes - it does not not make currentNode or firstSelectedNode null!
    public void deselectAll() {
        for (Node n : shapeArrayList) {
            n.setSelect(false);
        }
        invalidate();
    }


    // This method only unhighlights nodes - it does not not make currentNode or firstSelectedNode null!
    public void nullifyCurrentNodeAndFirstSelectedNode() {
        currentNode = null;
        firstSelectedShapeToBondWith = null;
    }


    public void printAllBonds() {
        int i = 1;
        Log.v("PRINTBONDSANDNODES1", "\t\t---------------- BONDS -------------------");
        for (Bond b : bondArrayList) {

            Node one = b.getNode1();
            Node two = b.getNode2();
            Log.v("PRINTBONDSANDNODES1", "Bond Type: " + b.getBondType());
            String nodeDescription1 = "\t";
            String nodeDescription2 = "\t";
            // Node one data
            if (one instanceof Circle) {
                nodeDescription1 += "Circle" + i;
            } else if (one instanceof Square) {
                nodeDescription1 += "Square" + i;
            } else if (one instanceof Triangle) {
                nodeDescription1 += "Triangle" + i;
            } else if (one instanceof Hexagon) {
                nodeDescription1 += "Hexagon" + i;
            }
            nodeDescription1 += ": Konnections: " + one.getNumberOfKonnections();
            Log.v("PRINTBONDSANDNODES1", nodeDescription1);

            // Node 2 data
            if (two instanceof Circle) {
                nodeDescription2 += "Circle" + i;
            } else if (two instanceof Square) {
                nodeDescription2 += "Square" + i;
            } else if (two instanceof Triangle) {
                nodeDescription2 += "Triangle" + i;
            } else if (two instanceof Hexagon) {
                nodeDescription2 += "Hexagon" + i;
            }
            nodeDescription2 += ": Konnections: " + two.getNumberOfKonnections();
            Log.v("PRINTBONDSANDNODES1", nodeDescription2);
            i++;
        }
    }


    public void printAllNodes() {
        int i = 1;
        Log.v("PRINTBONDSANDNODES1", "\t\t---------------- NODES -------------------");
        for (Node n : shapeArrayList) {
            String nodeDescription1 = "\t";
            if (n instanceof Circle) {
                nodeDescription1 += "Circle" + i;
            } else if (n instanceof Square) {
                nodeDescription1 += "Square" + i;
            } else if (n instanceof Triangle) {
                nodeDescription1 += "Triangle" + i;
            } else if (n instanceof Hexagon) {
                nodeDescription1 += "Hexagon" + i;
            }
            nodeDescription1 += ": Konnections: " + n.getNumberOfKonnections();
            Log.v("PRINTBONDSANDNODES1", nodeDescription1);

            for (Node neighbor : n.getNeighbors()) {
                String nodeDescription2 = "\t\t";
                if (neighbor instanceof Circle) {
                    nodeDescription2 += "Circle" + i;
                } else if (neighbor instanceof Square) {
                    nodeDescription2 += "Square" + i;
                } else if (neighbor instanceof Triangle) {
                    nodeDescription2 += "Triangle" + i;
                } else if (neighbor instanceof Hexagon) {
                    nodeDescription2 += "Hexagon" + i;
                }
                nodeDescription2 += ": Konnections: " + neighbor.getNumberOfKonnections();
                Log.v("PRINTBONDSANDNODES1", nodeDescription2);
            }
            i++;
        }
    }


}
