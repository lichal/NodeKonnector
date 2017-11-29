package crorg.node_konnector;

import java.util.ArrayList;

/**
 * Created by Ryan on 2017-11-27.
 */


public class Node {
    private int num;
    private int numberKonnections;
    private ArrayList<Node> neighbors;


    public Node(int num) {
        this.num = num;
        numberKonnections = 0;
        neighbors = new ArrayList<Node>();
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

    public int getNumberOfKonnections() {
        return numberKonnections;
    }

    public void addNeighborNode(Node potentialFriend) {
        if ((neighbors.contains(potentialFriend) == false) && (potentialFriend != this)) {
            neighbors.add(potentialFriend);
        }
    }


    public void removeNeighborNode(Node potentialNeighbor) {
        if (neighbors.contains(potentialNeighbor) == true) {
            neighbors.remove(potentialNeighbor);
        }
    }


    public boolean hasNeighborNode(Node possibleNeighbor) {
        if (neighbors.contains(possibleNeighbor) == true) {
            return true;
        }
        return false;
    }



    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

}
