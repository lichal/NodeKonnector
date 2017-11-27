package crorg.node_konnector;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ryan on 2017-11-27.
 */




public class Logic {


    public static void randomizeAStructure(int n) {
        Structure st = new Structure();
        ArrayList<Konnection> konnections = st.getKonnections();
        ArrayList<Node> nodes = st.getNodes();

        // Step 1: Make nodes
        for (int i = 0; i < n; i++) {
            st.addNode(new Node(i));
        }

        // Step 2: Connect them all [n(n + 1)/2 total bonds]
        for (int node1 = 0; node1 < nodes.size(); node1++) {
            for (int node2 = node1; node2 < nodes.size(); node2++) {
                Node n1 = nodes.get(node1);
                Node n2 = nodes.get(node2);
                if (n1 == n2) {
                    continue;
                }
                Konnection k = new Konnection(n1, n2);
                st.addKonnection(k);
            }
        }
        System.out.println("Number of nodes before: " + st.getNodes().size() + ", Number of connections: " + konnections.size());

        // Step 3: Remove all konnections except the bare minimum number
        // necessary for the structure to stay intact
        Random r = new Random();
        for (int current = konnections.size(); current > n - 1; current--) {
            int one = r.nextInt(konnections.size());
            Konnection k = konnections.get(one);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();

            //if only one connection for a node, don't remove
            int num1 = Logic.numberOfRepresenations(n1, konnections);
            int num2 = Logic.numberOfRepresenations(n2, konnections);
            if ((num1 >= 1) && (num2 >= 1)) {
                System.out.println("current: " + current);
                konnections.remove(one);
            } else {
                current++;
            }
        }

        System.out.println("Number of nodes: " + st.getNodes().size() + ", Number of connections: " + konnections.size());
        // randomly classify remaining connections as single or double, etc.
        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")

        for (Konnection k : konnections) {
            System.out.println("Node" + k.getNode1().getNum() + " bonded to " + k.getNode2().getNum());
        }



    }



    private static int numberOfRepresenations(Node interest, ArrayList<Konnection> list) {
        int sum = 0;
        for (Konnection k : list) {
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();
            if ((n1 == interest) || (n2 == interest)) {
                sum++;
            }
        }
         return sum;
    }






}
