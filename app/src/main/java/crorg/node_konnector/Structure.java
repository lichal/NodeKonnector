package crorg.node_konnector;

import java.util.ArrayList;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Structure {
    private ArrayList<Bond> bonds;
    private ArrayList<Node> nodes;

    public Structure() {
        bonds = new ArrayList<Bond>();
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node n) {
        nodes.add(n);
    }
    
    public void addKonnections(Bond n) {
        bonds.add(n);
    }


    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Bond> getBonds() {
        return bonds;
    }

    public void addBond(Bond k) {
        if (bonds.size() == 0) {
            bonds.add(k);
            return;
        }

        for (Bond ko : bonds) {
            if (!Bond.areEqual(k, ko)) {
                bonds.add(k);
                return;
            }
        }
    }

}
