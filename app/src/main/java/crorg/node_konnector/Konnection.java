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
        classOfKonnection = Konnection.SINGLE_BOND;
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
        if (a == Konnection.SINGLE_BOND) {
            classOfKonnection = Konnection.SINGLE_BOND;
        } else if (a == Konnection.DOUBLE_BOND) {
            classOfKonnection = Konnection.DOUBLE_BOND;
        } else if (a == Konnection.TRIPLE_BOND) {
            classOfKonnection = Konnection.TRIPLE_BOND;
        } else {
            return;
        }
    }

    public int getType(int a) {
        return classOfKonnection;
    }







    public static boolean areEqual(Konnection k1, Konnection k2) {
        Node n1 = k1.getNode1();
        Node n2 = k1.getNode2();
        Node n3 = k2.getNode1();
        Node n4 = k2.getNode2();
        if (((n1 == n3) || (n1 == n4)) && ((n2 == n3) || (n2 == n4))) {
            return true;
        }
        return false;
    }


}
