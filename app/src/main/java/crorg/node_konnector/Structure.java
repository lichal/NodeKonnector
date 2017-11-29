package crorg.node_konnector;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Structure {
    private ArrayList<Bond> bonds;
    private ArrayList<Node> nodes;
    private int numNodes;

    public Structure(int numNodes) {
        bonds = new ArrayList<Bond>();
        nodes = new ArrayList<Node>();
        this.numNodes = numNodes;
        createNodes();
        bondAllNodesToEachOther();
        removeBondsToBareMinimum();
        fixAnyOverkonnectedNodes();
        randomizeBondTypes();

        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")
        displayStructureInfoForDebugging();
    }


    private void addBond(Bond k) {
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





    // returns true if no node exceeds allowable konnection limit
    private boolean statusOfNodes() {
        for (Node n : nodes) {
            if (n.getNumberOfKonnections() > Logic.NUM_TOTAL_SHAPES) {
                return false;
            }
        }
        return true;
    }


    private int getMaxKonnectionsFromAllNodes() {
        int max = -7;
        for (Node n : nodes) {
            if (n.getNumberOfKonnections() >= max) {
                max = n.getNumberOfKonnections();
            }
        }
        return max;
    }


    // step 1
    private void createNodes() {
        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i));
        }
    }

    // Step 2: Connect them all [n(n - 1)/2 total bonds]
    private void bondAllNodesToEachOther() {
        for (int node1 = 0; node1 < nodes.size(); node1++) {
            for (int node2 = node1; node2 < nodes.size(); node2++) {
                Node n1 = nodes.get(node1);
                Node n2 = nodes.get(node2);
                if (n1 == n2) {
                    continue;
                }
                Bond k = new Bond(n1, n2);
                addBond(k);
                n1.incrementKonnections();
                n2.incrementKonnections();
            }
        }

        // get rid of this when done
        System.out.println("Number of nodes before: " + nodes.size() + ", Number of connections: " + bonds.size());
    }

    // Step 3: Systematically remove all bonds except the bare minimum number
    // necessary for the structure to stay intact (a.k.a. all SINGLE bonds)
    private void removeBondsToBareMinimum() {
        Random r = new Random();
        for (int current = bonds.size(); current > numNodes - 1; current--) {
            int one = r.nextInt(bonds.size());
            Bond k = bonds.get(one);
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();

            //if only one konnection for a node, don't remove
            int num1 = n1.getNumberOfKonnections();
            int num2 = n2.getNumberOfKonnections();
            if ((num1 > 1) && (num2 > 1)) {
                bonds.remove(one);
                n1.decrementKonnections();
                n2.decrementKonnections();
            } else {
                current++;
            }
        }

        // just for testing... get rid of later
        System.out.println("Number of nodes after: " + nodes.size() + ", Number of connections: " + bonds.size());
        int sum = 0;
        for (Node nn : nodes) {
            sum += nn.getNumberOfKonnections();
        }
        sum = sum / 2;
        System.out.println("Number of bonds calc from KONNECTIONS: " + sum);
    }


    // This method MUST be called BEFORE randomize bonds - it assumes that all bonds are single
    // and reconnects them based on if there are too manay konnections somewhere.
    private void fixAnyOverkonnectedNodes() {
        // Search all nodes - see if num konnections is greater than Logic.Nums
        Random r = new Random();
        for (Node currentNode : nodes) {
            int konns = currentNode.getNumberOfKonnections();
            if (konns > Logic.NUM_TOTAL_SHAPES) {
                int numToRemove = konns - Logic.NUM_TOTAL_SHAPES;

                // acqure a list of all Bonds which include the current Node
                ArrayList<Bond> allBondsThisNodeHas = new ArrayList<Bond>();
                for (Bond bo : bonds) {
                    if ((bo.getNode1() == currentNode) || (bo.getNode2() == currentNode)) {
                        if (allBondsThisNodeHas.contains(bo) == false) {
                            allBondsThisNodeHas.add(bo);
                        }
                    }
                }

                // Keep removing bonds from this Node until it is no longer overkonnected...
                for (int i = 0; i < numToRemove; i++) {
                    // randomly pick one of these to move and get the other node it's bonded to
                    int bondSelected = r.nextInt(allBondsThisNodeHas.size());
                    Bond bondToMove = allBondsThisNodeHas.get(bondSelected);
                    Node loner = bondToMove.getNode1();
                    if (loner == currentNode) {
                        loner = bondToMove.getNode2();
                    }

                    // Find a suitable partner for this loner
                    Node replacementPartner = null;
                    for (Node potentialReplacement : nodes) {
                        if (potentialReplacement == currentNode) {
                            continue;
                        }
                        if (potentialReplacement.getNumberOfKonnections() < Logic.NUM_TOTAL_SHAPES) {
                            replacementPartner = potentialReplacement;
                            break;
                        }
                    }

                    // Migrate the bond over to its new place, remove bond from list
                    bondToMove.setNode1(loner);
                    bondToMove.setNode2(replacementPartner);
                    currentNode.decrementKonnections();
                    replacementPartner.incrementKonnections();
                    allBondsThisNodeHas.remove(bondSelected);
                }
            }
        }
    }


    // randomly classify all connections as single, double, or triple (don't go any higher - triple bonds are enough)
    private void randomizeBondTypes() {
        Random r = new Random();
        for (Bond k : bonds) {
            Node n1 = k.getNode1();
            Node n2 = k.getNode2();
            int num1 = n1.getNumberOfKonnections();
            int num2 = n2.getNumberOfKonnections();
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
     */
    private void displayStructureInfoForDebugging() {
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


        for (int i = 0; i < nodes.size(); i++) {
            System.out.println("Node" + i + ": " + nodes.get(i).getNumberOfKonnections());
        }

        System.out.println("Max konnections: " + getMaxKonnectionsFromAllNodes());
    }
}