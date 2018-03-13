package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    /**
     * Finds a non-optimal short path from the data file specified.
     * @implNote output is saved under resources directory parallel to the source directory.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{

        try {
            //Scanner to read the user's filename input.
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter the name for the data file? ");
            String fileName = scanner.next();

            BufferedReader reader;
            //Array list to store unsorted list of pools taken from the input database file.
            ArrayList<Pool> poolArrayList = new ArrayList<Pool>();

            //Input file should be in the resources directory parallel to the source directory.
            reader = new BufferedReader(new FileReader("resources/" + fileName));

            //Code used to run the program without entering database filename - For testing only.
            //reader = new BufferedReader(new FileReader("resources/database.txt"));

            String line = reader.readLine();
            while (line != null) {

                String arrLine[] = line.split(",");

                //Adding each pool to the ArrayList
                if (arrLine.length == 3) {
                    String poolName = arrLine[0];
                    double poolLongitude = Double.parseDouble(arrLine[1]);
                    double poolLatitude = Double.parseDouble(arrLine[2]);
                    poolName = poolName.replace("\"", "");
                    poolArrayList.add(new Pool(poolName,poolLongitude,poolLatitude));
                }

                line = reader.readLine();
            }
            reader.close();

            /*
            //Prints the pool list to the console - Testing only
            for (Pool P: poolArrayList) { System.out.println("NAME: " + P.getName() + " Longitude: " + P.getLongitude() + " Latitude " + P.getLatitude()); }
            System.out.println("\n\n");
            */

            //Sort the pools from West to East.
            poolArrayList.sort(Comparator.comparing(Pool::getLongitude));

           /*
            //Prints the pool list to the console - Testing only
            for (Pool P: poolArrayList) { System.out.println("NAME: " + P.getName() + " Longitude: " + P.getLongitude() + " Latitude " + P.getLatitude()); }
            System.out.println("\n\n");
            */

            //Store the most Western pool as the root node
            poolArrayList.get(0).setDistanceToParent(0);
            Node<Pool> root = new Node<Pool>(poolArrayList.get(0));
            Tree<Pool> poolTree = new Tree<Pool>(root);
            poolArrayList.remove(0);

            //Connect the closest pool with an edge as the child of the root.
            Pool closestPool = poolArrayList.get(0);
            double closestPoolDistance = closestPool.distanceFrom(root.getData());
            int closestPoolIndex = 0;
            for (int i = 1; i < poolArrayList.size(); i++) {
                double currentPoolDistance = poolArrayList.get(i).distanceFrom(root.getData());
                if (currentPoolDistance < closestPoolDistance) {
                    closestPool = poolArrayList.get(i);
                    closestPoolDistance = currentPoolDistance;
                    closestPoolIndex = i;
                }
            }
            poolTree.getRoot().addChild(new Node<Pool>(closestPool));
            closestPool.setDistanceToParent(closestPoolDistance);
            closestPool.setDistanceToRoot(closestPoolDistance);
            poolArrayList.remove(closestPoolIndex);

            //For each pool from West to East connect the node for the pool with an edge as the child of the closest node in the tree.
            //Creating Stack from ArrayList
            Stack<Pool> poolStack = new Stack<Pool>();
            poolStack.addAll(poolArrayList);

            while (!poolStack.empty()) {
                Node<Pool> nodeToAdd = new Node<Pool>(poolStack.pop());

                //Travers Tree
                ArrayList<Node<Pool>> preOrderTree = poolTree.getPreOrderTraversal();
                Node<Pool> closestPoolNode = preOrderTree.get(0);
                double closestPoolNodeDistance = nodeToAdd.getData().distanceFrom(closestPoolNode.getData());
                for (int i = 1; i < preOrderTree.size(); i++) {
                    double currentPoolDistance = nodeToAdd.getData().distanceFrom(preOrderTree.get(i).getData());
                    if (currentPoolDistance < closestPoolNodeDistance) {
                            closestPoolNode = preOrderTree.get(i);
                            closestPoolNodeDistance = currentPoolDistance;
                    }
                }

                //calculate the distance to Root
                Node<Pool> parent = closestPoolNode;
                double distanceToRoot = closestPoolNodeDistance;
                while (parent != poolTree.getRoot()) {
                    distanceToRoot += parent.getData().getDistanceToParent();
                    parent = parent.getParent();
                }

                //updating node to add
                nodeToAdd.getData().setDistanceToParent(closestPoolNodeDistance);
                nodeToAdd.getData().setDistanceToRoot(distanceToRoot);
                //adding node
                closestPoolNode.addChild(nodeToAdd);
            }

            //Given the above tree, perform a pre-order traversal to find a route that visits all nodes.
            ArrayList<Node<Pool>> preOrderPoolTree = poolTree.getPreOrderTraversal();
            double distanceTravelled = 0;
            Node<Pool> previousNodeVisited = preOrderPoolTree.get(0);

            File solution = new File("resources/solution.txt");
            solution.createNewFile();
            FileWriter writer = new FileWriter(solution);

            for (int i = 0; i < preOrderPoolTree.size(); i++) {
                //Make sure to calculate the distance travelled in kilometers.
                distanceTravelled += preOrderPoolTree.get(i).getData().distanceFrom(previousNodeVisited.getData());
                previousNodeVisited = preOrderPoolTree.get(i);

                writer.write(preOrderPoolTree.get(i).getData().getName() + " " + distanceTravelled + "\n");

                //Console Output - Use only for testing.
                //System.out.println( preOrderPoolTree.get(i).getData().getName() + " " + distanceTravelled);
                //System.out.println(preOrderPoolTree.get(i).getChildren().size());
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("File Error!");
            e.printStackTrace();
        }

    }
}