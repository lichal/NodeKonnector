package crorg.node_konnector;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import crorg.node_konnector.Shapes.Circle;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;

/**
 * Created by Ryan on 2017-11-27.
 */

public class Structure implements Serializable {
    private ArrayList<Bond> bonds;
    private ArrayList<Node> nodes;
    private int numNodes;

    public Structure(int numNodes) {
        bonds = new ArrayList<Bond>();
        nodes = new ArrayList<Node>();
        this.numNodes = numNodes;
        if (numNodes >=2) {
            createNodes();
            bondAllNodesToEachOther();
            randomlyRemoveRandomNumBondsFromStructure();
            fixAnyOverkonnectedNodes();
            randomizeBondTypes();
            //displayStringDescriptionForPlayer();

            // if the generated structure does NOT work (is not INTACT), then just keep
            // generating a new one until it works
            System.out.println("Is Structure INTACT?: " + isStrutureIntact());
            //displayStructureInfoForDebugging();
        } else {
            throw new IllegalArgumentException();
        }
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

    // Step 2: Connect them all [n(n - 1)/2 total single bonds]
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
    }

    // Step 3: Systematically remove a random number of bonds between
    // whatever the minimum is (in relation to shapes) to the max (keeping structure intact).
    // Note:  n must be at LEAST 2, and p must be at LEAST 2, for this formula to work!
    private void randomlyRemoveRandomNumBondsFromStructure() {
        Random r = new Random();
        int minToRemove = calcMinNumBondsToRemove();
        int maxToRemove = bonds.size() - (numNodes - 1);
        int difference = maxToRemove - minToRemove;
        // temporary - what if only the lower half are available?  Does it make gameplay easier?  What about rings?
        int halfDifference = difference / 2;
        //int numBondsLeftToRemove = minToRemove + r.nextInt(1 + difference);
        int numBondsLeftToRemove = minToRemove + halfDifference + r.nextInt(1 + halfDifference);
        while (numBondsLeftToRemove > 0) {
            int randIndex = r.nextInt(bonds.size());
            Bond bondSelected = bonds.get(randIndex);
            Node n1 = bondSelected.getNode1();
            Node n2 = bondSelected.getNode2();

            //if only one konnection for a node, don't remove
            int num1 = n1.getNumberOfKonnections();
            int num2 = n2.getNumberOfKonnections();
            if ((num1 > 1) && (num2 > 1)) {

                // if removing the bond won't upset the structure, then do it...
                n1.removeNeighborNode(n2);
                n2.removeNeighborNode(n1);
                if (isStrutureIntact()) {
                    n1.decrementKonnections();
                    n2.decrementKonnections();
                    bonds.remove(bondSelected);
                    numBondsLeftToRemove--;
                } else {
                    n1.addNeighborNode(n2);
                    n2.addNeighborNode(n1);
                }
            }
        }
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
                    mainLoop:
                    for (Node potentialReplacement : nodes) {
                        if ((potentialReplacement != currentNode) && (potentialReplacement != loner)) {
                            boolean isNotOverkonnected = (potentialReplacement.getNumberOfKonnections() < Logic.NUM_TOTAL_SHAPES);
                            boolean alreadyANeighbor = potentialReplacement.hasNeighborNode(loner);
                            if (isNotOverkonnected && (!alreadyANeighbor)) {
                                // Temporarily remove both  nodes as neighbors - if it doesn't
                                // upset the structure, do it
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

                    // Do magic if it works...
                    // Migrate the bond over to its new place, remove bond from list
                    if (replacementPartner != null) {
                        bondToMove.setNode1(loner);
                        bondToMove.setNode2(replacementPartner);
                        currentNode.decrementKonnections();
                        replacementPartner.incrementKonnections();
                        allBondsThisNodeHas.remove(bondSelected);
                    } else {
                        allBondsThisNodeHas.remove(bondToMove);
                        i++;
                    }
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
     * can remove this later
     */
    private void displayStructureInfoForDebugging() {
        for (Bond k : bonds) {
            System.out.println();
            System.out.print(k.getNode1().getNum() + " --- " + k.getNode2().getNum());
            int m = k.getBondType();
            String s = "";
            if (m == 1) {
                s += "single";
            } else if (m == 2) {
                s += "double";
            } else {
                s += "triple";
            }
            System.out.println("\t" + s);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    // shows the sorted relative number of shapes the USER currently has drawn
//    private static ArrayList<Integer> getUserRelativeShapesCount(ArrayList<Node> nodesList) {
//        int[] relativeNumbers = {0, 0, 0, 0};
//        for (Node n : nodesList) {
//            if (n instanceof Circle) {
//                relativeNumbers[0]++;
//            }
//            if (n instanceof Square) {
//                relativeNumbers[1]++;
//            }
//            if (n instanceof Triangle) {
//                relativeNumbers[2]++;
//            }
//            if (n instanceof Hexagon) {
//                relativeNumbers[3]++;
//            }
//        }
//        Arrays.sort(relativeNumbers);
//        ArrayList<Integer> relativeNumbers2 = new ArrayList<Integer>();
//        for (int i = 0; i < relativeNumbers.length; i++) {
//            int number = relativeNumbers[i];
//            if (number > 0) {
//                relativeNumbers2.add(number);
//            }
//        }
//        return relativeNumbers2;
//    }


    // shows the SORTED relative number of shapes the given structure has
    private static ArrayList<Integer> getRelativeShapesCount(ArrayList<Node> nodesList) {
        ArrayList<Integer> relativeNumbers = new ArrayList<Integer>();
        for (int i = 0; i < Logic.NUM_TOTAL_SHAPES; i++) {
            int count = 0;
            for (Node n : nodesList) {
                if (n.getNumberOfKonnections() == (1 + i)) {
                    count++;
                }
            }
            if (count > 0) {
                relativeNumbers.add(count);
            }
        }
        Collections.sort(relativeNumbers);
        return relativeNumbers;
    }

    // two structures are logically "similar enough" if they have (1) same number of nodes, (2) same relative amounts of shapes,
    // (3) same number of single, double, triple bonds
    public static boolean areStructuresSimilarEnough(ArrayList<Node> nodes1, ArrayList<Bond> bonds1, ArrayList<Node> nodes2, ArrayList<Bond> bonds2) {
        // Compare shapes configs...
        ArrayList<Integer> relativeShapesCount1 = Structure.getRelativeShapesCount(nodes1);
        ArrayList<Integer> relativeShapesCount2 = Structure.getRelativeShapesCount(nodes2);
        int size1 = relativeShapesCount1.size();
        int size2 = relativeShapesCount2.size();
        if (size1 != size2) {
            return false;
        } else {
            for (int i = 0; i < size1; i++) {
                if (relativeShapesCount1.get(i).intValue() != relativeShapesCount2.get(i).intValue()) {
                    return false;
                }
            }
        }

        // Now compare bonds...
//        int structure1Singles = 0;
//        int structure1Doubles = 0;
//        int structure1Triples = 0;
//        int structure2Singles = 0;
//        int structure2Doubles = 0;
//        int structure2Triples = 0;
//        int sizeBonds1 = bonds1.size();
//        int sizeBonds2 = bonds2.size();
//        for (int i = 0; i < sizeBonds1; i++) {
//            structure1Singles += (bonds1.get(i).getBondType() == Bond.SINGLE ? 1 : 0);
//            structure1Doubles += (bonds1.get(i).getBondType() == Bond.DOUBLE ? 1 : 0);
//            structure1Triples += (bonds1.get(i).getBondType() == Bond.TRIPLE ? 1 : 0);
//        }
//        for (int i = 0; i < sizeBonds2; i++) {
//            structure2Singles += (bonds2.get(i).getBondType() == Bond.SINGLE ? 1 : 0);
//            structure2Doubles += (bonds2.get(i).getBondType() == Bond.DOUBLE ? 1 : 0);
//            structure2Triples += (bonds2.get(i).getBondType() == Bond.TRIPLE ? 1 : 0);
//        }
//        // Return result...
//        if ((structure1Singles != structure2Singles) || (structure1Doubles != structure2Doubles)
//                || (structure1Triples != structure2Triples)) {
//            return false;
//        }
        return true;
    }


    public static boolean checkAllCircleKonnections(ArrayList<Node> playerNodes) {
        for (Node n : playerNodes) {
            if (n instanceof Circle) {
                if (n.getNumberOfKonnections() != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkAllSquareKonnections(ArrayList<Node> playerNodes) {
        for (Node n : playerNodes) {
            if (n instanceof Square) {
                if (n.getNumberOfKonnections() != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkAllTriangleKonnections(ArrayList<Node> playerNodes) {
        for (Node n : playerNodes) {
            if (n instanceof Triangle) {
                if (n.getNumberOfKonnections() != 3) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkAllHexagonKonnections(ArrayList<Node> playerNodes) {
        for (Node n : playerNodes) {
            if (n instanceof Hexagon) {
                if (n.getNumberOfKonnections() != 4) {
                    return false;
                }
            }
        }
        return true;
    }


    public void displayStringDescriptionForPlayer() {
        // check relative shape amounts...
        int numNodes = nodes.size();
        int numKonnections = 0;
        ArrayList<Integer> answerShapes = Structure.getRelativeShapesCount(nodes);
        for (Integer i : answerShapes) {
            System.out.println("Shape: " + i);
        }

        // check bonds now...
        numKonnections = numKonnections / 2;
        System.out.println("Total Nodes: " + numNodes + "\nTotal Konnections: "
                + numKonnections + "\nTotal Bonds: " + bonds.size());
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

    public String printNumShapes(){
        String message = "";
        ArrayList<Integer> answerShapes = Structure.getRelativeShapesCount(nodes);
        for (int i = 0; i < answerShapes.size(); i++) {
            String type = "";
            type += (i == 0) ? "'A'" : "";
            type += (i == 1) ? "'B'" : "";
            type += (i == 2) ? "'C'" : "";
            type += (i == 3) ? "'D'" : "";
            message += "# Of Shape " + type + ":  " + String.valueOf(answerShapes.get(i).intValue()) + "\n";
        }
        return message;
    }

    public String printNumBonds(){
        int numSingleBonds = 0;
        int numDoubleBonds = 0;
        int numTripleBonds = 0;
        String message = "";
        for (Bond b : bonds) {
            if (b.getBondType() == Bond.SINGLE) {
                numSingleBonds++;
            } else if (b.getBondType() == Bond.DOUBLE) {
                numDoubleBonds++;
            } else if (b.getBondType() == Bond.TRIPLE) {
                numTripleBonds++;
            }
        }

        if (numSingleBonds > 0) {
            message += "# Of Single Bonds:  " + numSingleBonds;
        }
        if (numDoubleBonds > 0) {
            message += "\n# Of Double Bonds:  " + numDoubleBonds;
        }
        if (numTripleBonds > 0) {
            message += "\n# Of Triple Bonds:  " + numTripleBonds;
        }
        return message;
    }

    // pick a random node - if structure is intact, all nodes should be counted
    public boolean isStrutureIntact() {
        Random r = new Random();
        int selection = r.nextInt(nodes.size());
        Node theOne = nodes.get(selection);
        ArrayList<Node> allFriendKonnections = new ArrayList<Node>();
        boolean isEqual = (nodes.size() == Structure.countAllNodeRelatives(theOne, allFriendKonnections));
        return isEqual;
    }


    // use this to test if removing a bond is NOT okay!
    public static int countAllNodeRelatives(Node startingNode, ArrayList<Node> allFriendKonnections) {
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



    private int calcMinNumBondsToRemove() {
        int p = Logic.NUM_TOTAL_SHAPES;
        if (p >= numNodes - 1) {
            return 0;
        }
        double minAmountToRemoveDOUBLE = (((double)numNodes *
                ((double)(numNodes - 1) - (double)p)) / 2.0);
        int minAmountToRemove = (int) Math.ceil(minAmountToRemoveDOUBLE);
        return minAmountToRemove;
    }

    public ArrayList<Node> getNodes() { return nodes; }

    public ArrayList<Bond> getBonds() { return bonds; }


}