package crorg.node_konnector;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ryan on 2017-11-27.
 */




public class Logic {
    private static int NUM_TOTAL_SHAPES = 4;



    public static void randomizeAStructure(int numberOfNodes) {
        Structure st = new Structure();
        ArrayList<Bond> bonds = st.getBonds();
        ArrayList<Node> nodes = st.getNodes();
        Logic.createNodes(st, numberOfNodes);
        Logic.bondAllNodesToEachOther(st);
        Logic.removeBondsToBareMinimum(st, numberOfNodes);
        //Logic.correctAllFaultyBonds(st, n);
        Logic.randomizeBondTypes(st);
        Logic.displayAllKonnections(st);


        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")
        // save this structure for gameplay
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println("Node" + i + ": " + nodes.get(i).getNumberOfBonds());
        }
    }



    private static int numberOfRepresenations(Node interest, ArrayList<Bond> list) {
        int sum = 0;
        for (Bond k : list) {
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

    // Step 2: Connect them all [n(n - 1)/2 total bonds]
    private static void bondAllNodesToEachOther(Structure st) {
        ArrayList<Node> nodes = st.getNodes();
        ArrayList<Bond> bonds = st.getBonds();
        for (int node1 = 0; node1 < nodes.size(); node1++) {
            for (int node2 = node1; node2 < nodes.size(); node2++) {
                Node n1 = nodes.get(node1);
                Node n2 = nodes.get(node2);
                if (n1 == n2) {
                    continue;
                }
                Bond k = new Bond(n1, n2);
                st.addBond(k);
                n1.incrementKonnections();
                n2.incrementKonnections();
            }
        }
        System.out.println("Number of nodes before: " + st.getNodes().size() + ", Number of connections: " + bonds.size());
    }

    // Step 3: Systematically remove all bonds except the bare minimum number
    // necessary for the structure to stay intact (a.k.a. all SINGLE bonds)
    // SUBTLE PROBLEM - SOME NODES ARE GIVEN CONNECTIONS HIGHER THAN Logic.NUM_OF_SHAPES!!!
    private static void removeBondsToBareMinimum(Structure st, int numNodes) {
        ArrayList<Bond> bonds = st.getBonds();
        ArrayList<Node> nodes = st.getNodes();
        Random r = new Random();
        for (int current = bonds.size(); current > numNodes - 1; current--) {
            int one = r.nextInt(bonds.size());
            Bond k = bonds.get(one);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();

            //if only one connection for a node, don't remove
            int num1 = n1.getNumberOfBonds();
            int num2 = n2.getNumberOfBonds();
            if ((num1 > 1) && (num2 > 1)) {
                bonds.remove(one);
                n1.decrementKonnections();
                n2.decrementKonnections();
            } else {
                current++;
            }
        }

        System.out.println("Number of nodes after: " + st.getNodes().size() + ", Number of connections: " + bonds.size());
        int sum = 0;
        for (Node nn : nodes) {
            sum += nn.getNumberOfBonds();
        }
        sum = sum / 2;

        System.out.println("Number of bonds from NODES: " + sum);
    }


    // randomly classify all connections as single, double, or triple (don't go any higher - triple bonds are enough)
    private static void randomizeBondTypes(Structure st) {
        ArrayList<Bond> bonds = st.getBonds();
        Random r = new Random();
        for (Bond k : bonds) {
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();
            int num1 = n1.getNumberOfBonds();
            int num2 = n2.getNumberOfBonds();
            int largerKonnection = Math.max(num1, num2);
            int maxAddableKonnections = Logic.NUM_TOTAL_SHAPES - largerKonnection;
            if (maxAddableKonnections >= 1) {
                // Only  have single, double, and triple bonds to deal with...
                if (maxAddableKonnections >= 3) {
                    maxAddableKonnections = 2;
                }
                int howManyToAdd = r.nextInt(1 + maxAddableKonnections);
                if (howManyToAdd == Bond.DOUBLE - 1) {
                    k.setBondType(Bond.DOUBLE);
                    n1.incrementKonnections();
                    n2.incrementKonnections();
                } else if (howManyToAdd == Bond.TRIPLE - 1) {
                    k.setBondType(Bond.TRIPLE);
                    n1.incrementKonnections();
                    n1.incrementKonnections();
                    n2.incrementKonnections();
                    n2.incrementKonnections();
                }
            } else {
                k.setBondType(Bond.SINGLE);
            }
        }
    }




    /**
     * display all connections with text
     * @param st
     */
    private static void displayAllKonnections(Structure st) {
        ArrayList<Bond> bonds = st.getBonds();
        for (Bond k : bonds) {
            System.out.println();
            System.out.print(k.getNode1().getNum() + " --- " + k.getNode2().getNum());
            int m = k.getBondType();
            String s = "";
            if (m == Bond.SINGLE) {
                s += "single";
            } else if (m == Bond.DOUBLE) {
                s += "double";
            } else {
                s += "triple";
            }

            System.out.println("\t" + s);
        }
    }
}
