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
        Logic.createNodes(st, n);
        Logic.konnectAllNodesToEachOther(st);
        Logic.removeKonnectionsToBareMinimum(st, n);
        Logic.randomizeBondTypes(st);
        Logic.displayAllKonnections(st);

        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")
        // save this structure for gameplay
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



    // Step 1
    private static void createNodes(Structure st, int numNodes) {
        for (int i = 0; i < numNodes; i++) {
            st.addNode(new Node(i));
        }
    }

    // Step 2: Connect them all [n(n + 1)/2 total konnections]
    private static void konnectAllNodesToEachOther(Structure st) {
        ArrayList<Node> nodes = st.getNodes();
        ArrayList<Konnection> konnections = st.getKonnections();
        for (int node1 = 0; node1 < nodes.size(); node1++) {
            for (int node2 = node1; node2 < nodes.size(); node2++) {
                Node n1 = nodes.get(node1);
                Node n2 = nodes.get(node2);
                if (n1 == n2) {
                    continue;
                }
                Konnection k = new Konnection(n1, n2);
                st.addKonnection(k);
                n1.incrementKonnections();
                n2.incrementKonnections();
            }
        }
        System.out.println("Number of nodes before: " + st.getNodes().size() + ", Number of connections: " + konnections.size());
    }

    // Step 3: Systematically remove all konnections except the bare minimum number
    // necessary for the structure to stay intact (a.k.a. all SINGLE bonds)
    // POTENTIAL PROBLEM - CANNOT LEAVE NODES WITH CONNECTIONS HIGHER THAN Logic.NUM_OF_SHAPES!!!
    private static void removeKonnectionsToBareMinimum(Structure st, int numNodes) {
        ArrayList<Konnection> konnections = st.getKonnections();
        ArrayList<Node> nodes = st.getNodes();
        Random r = new Random();
        for (int current = konnections.size(); current > numNodes - 1; current--) {
            int one = r.nextInt(konnections.size());
            Konnection k = konnections.get(one);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();

            //if only one connection for a node, don't remove
            int num1 = n1.getNumberKonnections();
            int num2 = n2.getNumberKonnections();
            if ((num1 > 1) && (num2 > 1)) {
                konnections.remove(one);
                n1.decrementKonnections();
                n2.decrementKonnections();
            } else {
                current++;
            }
        }

        System.out.println("Number of nodes after: " + st.getNodes().size() + ", Number of connections: " + konnections.size());
        int sum = 0;
        for (Node nn : nodes) {
            sum += nn.getNumberKonnections();
        }
        sum = sum / 2;

        System.out.println("Number of konnections from NODES: " + sum);
    }


    // randomly classify all connections as single, double, or triple (don't go any higher - triple bonds are enough)
    private static void randomizeBondTypes(Structure st) {
        ArrayList<Konnection> konnections = st.getKonnections();
        Random r = new Random();
        for (Konnection k : konnections) {
            int typeOfBond = r.nextInt(3);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();
            int num1 = n1.getNumberKonnections();
            int num2 = n2.getNumberKonnections();
            if (typeOfBond == Konnection.DOUBLE_BOND) {
                if ((num1 <= 3) && (num2 <= 3)) {
                    k.setBondType(Konnection.DOUBLE_BOND);
                }
            } else if (typeOfBond == Konnection.TRIPLE_BOND) {
                if ((num1 <= 2) && (num2 <= 2)) {
                    k.setBondType(Konnection.TRIPLE_BOND);
                }
            } else {
                k.setBondType(Konnection.SINGLE_BOND);
            }
        }
    }




    /**
     * display all connections with text
     * @param st
     */
    private static void displayAllKonnections(Structure st) {
        ArrayList<Konnection> konnections = st.getKonnections();
        for (Konnection k : konnections) {
            System.out.println("Node-" + k.getNode1().getNum() + " bonded to Node-" + k.getNode2().getNum());
            int m = k.getBondType();
            String s = "";
            if (m == Konnection.SINGLE_BOND) {
                s += "single";
            } else if (m == Konnection.DOUBLE_BOND) {
                s += "double";
            } else {
                s += "triple";
            }

            System.out.println("\tType of connection:  " + s);
        }
    }
}
