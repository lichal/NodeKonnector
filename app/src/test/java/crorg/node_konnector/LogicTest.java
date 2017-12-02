package crorg.node_konnector;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Ryan on 2017-11-27.
 */
public class LogicTest {
    @Test
    public void randomizeAStructure() throws Exception {
        new Structure(3);
    }

    // this one won't work anymore because I made the methods private
    @Test
    public void testIfStructureIsIntact() throws Exception {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);

        // add neighbors
        a.addNeighborNode(b);
        b.addNeighborNode(a);
        e.addNeighborNode(c);
        c.addNeighborNode(e);
        c.addNeighborNode(d);
        d.addNeighborNode(c);
        d.addNeighborNode(e);
        e.addNeighborNode(d);

        // connect all
        a.addNeighborNode(e);
        e.addNeighborNode(a);


        ArrayList<Node> allKonnectedNodes = new ArrayList<Node>();
        //int totalRelatives = Structure.countAllNodeRelatives(c, allKonnectedNodes);
        //System.out.println("# Nodes In Structure containing C: " + totalRelatives);
    }
}