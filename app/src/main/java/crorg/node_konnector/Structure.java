package crorg.node_konnector;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Structure {
    private ArrayList<Konnection> konnections;
    private ArrayList<Node> nodes;

    public Structure() {
        konnections = new ArrayList<Konnection>();
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node n) {
        nodes.add(n);
    }
    


    public void addKonnections(Konnection n) {
        konnections.add(n);
    }


    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Konnection> getKonnections() {
        return konnections;
    }

    public void addKonnection(Konnection k) {
        if (konnections.size() == 0) {
            konnections.add(k);
            return;
        }

        for (Konnection ko : konnections) {
            if (!Konnection.areEqual(k, ko)) {
                konnections.add(k);
                return;
            }
        }
    }

}
