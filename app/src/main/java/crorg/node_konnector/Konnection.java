package crorg.node_konnector;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Konnection {
    public static int SINGLE_BOND = 1;
    public static int DOUBLE_BOND = 2;
    public static int TRIPLE_BOND = 3;
    private Node node1;
    private Node node2;
    private int classOfKonnection;


    public Konnection(Node n1, Node n2) {
        node1 = n1;
        node2 = n2;
        classOfKonnection = SINGLE_BOND;
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


    public void setType(int a) {
        classOfKonnection = a;
    }

    public int getType(int a) {
        return classOfKonnection;
    }


}
