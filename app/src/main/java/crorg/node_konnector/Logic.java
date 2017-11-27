package crorg.node_konnector;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ryan on 2017-11-27.
 */




public class Logic {
    private static int NUM_TOTAL_SHAPES = 4;



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

        // Step 3: Systematically remove all konnections except the bare minimum number
        // necessary for the structure to stay intact (a.k.a. all SINGLE bonds)
        // POTENTIAL PROBLEM - CANNOT LEAVE NODES WITH CONNECTIONS HIGHER THAN Logic.NUM_OF_SHAPES!!!
        Random r = new Random();
        for (int current = konnections.size(); current > n - 1; current--) {
            int one = r.nextInt(konnections.size());
            Konnection k = konnections.get(one);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();

            //if only one connection for a node, don't remove
            int num1 = Logic.numberOfRepresenations(n1, konnections);
            int num2 = Logic.numberOfRepresenations(n2, konnections);
            if ((num1 > 1) && (num2 > 1)) {
                konnections.remove(one);
            } else {
                current++;
            }
        }

        System.out.println("Number of nodes after: " + st.getNodes().size() + ", Number of connections: " + konnections.size());
        // randomly classify all connections as single, double, or triple (don't go any higher - triple bonds are enough)
        for (Konnection k : konnections) {
            int m = r.nextInt(Logic.NUM_TOTAL_SHAPES);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();
            int num1 = Logic.numberOfRepresenations(n1, konnections);
            int num2 = Logic.numberOfRepresenations(n2, konnections);

            // Add double bond, check
            if (m == Konnection.DOUBLE_BOND) {
                if ((num1 <= 3) && (num2 <= 3)) {

                }
            }



        }



        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")
        // save this structure for gameplay



        // display all connections with text
        for (Konnection k : konnections) {
            System.out.println("Node-" + k.getNode1().getNum() + " bonded to Node-" + k.getNode2().getNum());
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
