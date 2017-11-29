package crorg.node_konnector;

import java.sql.Struct;
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
        removeBondsToBareMinimum(); // the PROBLEM is HERE - the other ones are commented out, so THIS is the culprit!!!
        fixAnyOverkonnectedNodes();
        randomizeBondTypes();
        displayStringDescription();

        // through logic, figure out what shapes the nodes must be
        // based on last step, give summary ("3 shapes: 2 unique, 1 unique...")
        System.out.println("Is Structure INTACT?: " + isStrutureIntact());
        displayStructureInfoForDebugging();
    }

    // true if successfully added a unique one
    private boolean addBond(Bond k) {
        if (bonds.size() == 0) {
            bonds.add(k);
            return true;
        }

        boolean isNotCopycat = true;
        for (Bond ko : bonds) {
            if (Bond.areEqual(k, ko)) {
                return false;
            }
        }

        bonds.add(k);
        return true;
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
                if (addBond(k)) {
                    n1.incrementKonnections();
                    n2.incrementKonnections();
                    n1.addNeighborNode(n2);
                    n2.addNeighborNode(n1);
                }
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
            int randIndex = r.nextInt(bonds.size());
            Bond bondSelected = bonds.get(randIndex);
            Node n1 = bondSelected.getNode1();
            Node n2 = bondSelected.getNode2();

            //if only one konnection for a node, don't remove
            int num1 = n1.getNumberOfKonnections();
            int num2 = n2.getNumberOfKonnections();
            if ((num1 > 1) && (num2 > 1)) {

                // if removing the bond won't upset the structure, then do it...
                // temporarily remove the both  nodes as neighbors - if it still works, you can remove the bond
                n1.removeNeighborNode(n2);
                n2.removeNeighborNode(n1);
                if (isStrutureIntact()) {               // comment this out to show Cheng - use 5 as nodes and 26
                    n1.decrementKonnections();
                    n2.decrementKonnections();
                    bonds.remove(bondSelected);
                } else {                                // comment this out to show Cheng
                    n1.addNeighborNode(n2);             // comment this out to show Cheng
                    n2.addNeighborNode(n1);             // comment this out to show Cheng
                    current++;                          // comment this out to show Cheng
                }                                       // comment this out to show Cheng
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
    // this one should not have a problem like the other one - if a node is
    // disconnected but then connected again to another one, the entire structure should
    // still stay intact
    // if worst comes to worst, we can just ensure that a structure is intact by testing it
    // if it fails, generate a new one for the player
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
                    mainLoop:
                    for (Node potentialReplacement : nodes) {
                        if (potentialReplacement != currentNode) {
                            boolean isNotOverkonnected = potentialReplacement.getNumberOfKonnections() < Logic.NUM_TOTAL_SHAPES;
                            boolean alreadyANeighbor = potentialReplacement.hasNeighborNode(loner);
                            if (isNotOverkonnected && (!alreadyANeighbor)) {
                                // Temporarily remove both  nodes as neighbors - if it doesn't upset the structure, do it
                                currentNode.removeNeighborNode(loner);
                                loner.removeNeighborNode(currentNode);
                                potentialReplacement.addNeighborNode(loner);
                                loner.addNeighborNode(potentialReplacement);

                                if (isStrutureIntact()) {
                                    replacementPartner = potentialReplacement;
                                    break mainLoop;
                                } else {
                                    currentNode.addNeighborNode(loner);
                                    loner.addNeighborNode(currentNode);
                                    potentialReplacement.removeNeighborNode(loner);
                                    loner.removeNeighborNode(potentialReplacement);
                                }
                            }
                        }
                    }

                    // Migrate the bond over to its new place, remove bond from list
                    bondToMove.setNode1(loner);
                    bondToMove.setNode2(replacementPartner);

                    currentNode.decrementKonnections();
                    replacementPartner.incrementKonnections();

                    // this feels redundant... (it is NOT if I didn't have the test here)
                    //currentNode.removeNeighborNode(loner);
                    //loner.removeNeighborNode(currentNode);
                    //replacementPartner.addNeighborNode(loner);
                    //loner.addNeighborNode(replacementPartner);

                    allBondsThisNodeHas.remove(bondSelected);
                }
            }
        }
    }


    // randomly classify all connections as single, double, or triple (don't go any higher - triple bonds are enough)
    private void randomizeBondTypes() {
        Random r = new Random();
        for (Bond current : bonds) {
            Node n1 = current.getNode1();
            Node n2 = current.getNode2();
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
                    current.setBondType(Bond.DOUBLE);
                    n1.incrementKonnections();
                    n2.incrementKonnections();
                } else if (howManyToAdd == Bond.TRIPLE - 1) {
                    current.setBondType(Bond.TRIPLE);
                    n1.incrementKonnections();
                    n1.incrementKonnections();
                    n2.incrementKonnections();
                    n2.incrementKonnections();
                }
            } else {
                current.setBondType(Bond.SINGLE);
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


    private void displayStringDescription() {
        int numNodes = nodes.size();
        int numKonnections = 0;

        ArrayList<String> shapeResults = new ArrayList<String>();
        for (int i = 0; i < Logic.NUM_TOTAL_SHAPES; i++) {
            String s = "# of Shape-" + (i + 1) + ": ";
            int count = 0;
            for (Node n : nodes) {
                numKonnections += n.getNumberOfKonnections();
                if (n.getNumberOfKonnections() == 1 + i) {
                    count++;
                }
            }
            s += count;
            shapeResults.add(s);
        }

        numKonnections = numKonnections / 2;
        System.out.println("Total Nodes: " + numNodes + "\nTotal Konnections: "
                + numKonnections + "\nTotal Bonds: " + bonds.size());
        for (String s1 : shapeResults) {
            System.out.println(s1);
        }

        int numSingleBonds = 0;
        int numDoubleBonds = 0;
        int numTripleBonds = 0;
        for (Bond b : bonds) {
            if (b.getBondType() == Bond.SINGLE) {
                numSingleBonds++;
            } else if (b.getBondType() == Bond.DOUBLE) {
                numDoubleBonds++;
            } else if (b.getBondType() == Bond.TRIPLE) {
                numTripleBonds++;
            }
        }

        System.out.println("# of Single Bonds: " + numSingleBonds + "\n# of Double Bonds: " + numDoubleBonds + "\n# of Triple Bonds: " + numTripleBonds);

    }




    // pick a random node - if structure is intact, all nodes should be counted
    public boolean isStrutureIntact() {
        Random r = new Random();
        int selection = r.nextInt(nodes.size());
        Node theOne = nodes.get(selection);
        ArrayList<Node> allFriendKonnections = new ArrayList<Node>();
        boolean isEqual = (nodes.size() == countAllNodeRelatives(theOne, allFriendKonnections));
        return isEqual;
    }


    // use this to test if removing a bond is NOT okay!
    private int countAllNodeRelatives(Node startingNode, ArrayList<Node> allFriendKonnections) {
        ArrayList<Node> onlyCloseFriends = new ArrayList<Node>();
        if (!allFriendKonnections.contains(startingNode)) {
            allFriendKonnections.add(startingNode);
        }

        for (Node m : startingNode.getNeighbors()) {
            if (!onlyCloseFriends.contains(m)) {
                onlyCloseFriends.add(m);
            }
        }

        // now the recursive part...
        for (Node immediateFriend : onlyCloseFriends) {
            if (!allFriendKonnections.contains(immediateFriend)) {
                countAllNodeRelatives(immediateFriend, allFriendKonnections);
            }
        }

        return allFriendKonnections.size();
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
}