package crorg.node_konnector;

/**
 * Created by Ryan on 2017-11-27.
 */


public class Node {
    private int num;
    private int numberKonnections;


    public Node(int num) {
        this.num = num;
        numberKonnections = 0;
    }

    public int getNum() {
        return num;
    }

    public void incrementKonnections() {
        numberKonnections++;
    }

    public void decrementKonnections() {
        numberKonnections--;
    }

    public int getNumberKonnections() {
        return numberKonnections;
    }
}
