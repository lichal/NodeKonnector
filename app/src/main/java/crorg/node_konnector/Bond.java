package crorg.node_konnector;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Bond {
    public static int SINGLE = 1;
    public static int DOUBLE = 2;
    public static int TRIPLE = 3;
    private Node node1;
    private Node node2;
    private int classOfBond;


    public Bond(Node n1, Node n2) {
        node1 = n1;
        node2 = n2;
        classOfBond = Bond.SINGLE;
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


}
