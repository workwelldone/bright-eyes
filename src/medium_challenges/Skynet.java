package medium_challenges;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * My response to the Skynet coding game
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * Given a web of node connections, the goal is to block a moving virus from reaching an exit node by severing a single
 * web connection each turn.
 * 
 * @author Darren Pearson
 **/

class Skynet {

    private static final int MAX_LINKS_TO_FOLLOW = 4;  // Maximum number of links to pursue in a path.
    private static Map<Integer, Set<Integer>> nC = new HashMap<>();  // Map of node connections

    public static void main(String args[]) {
        @SuppressWarnings("resource") // Due to infinite loop specifications
		Scanner in = new Scanner(System.in);
        @SuppressWarnings("unused")
		int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            
            // Make a single map of each node and a set of all nodes to which it is connected.
            if (nC.get(N1) == null) { // create set and add first connection node, if it is the first.
                Set<Integer> temp = new HashSet<>();
                temp.add(N2);
                nC.put(N1,temp);
            } else 
                nC.get(N1).add(N2); // add connection node to the existing set
            
            if (nC.get(N2) == null) { // second node connection follows same pattern
                Set<Integer> temp = new HashSet<>();
                temp.add(N1);
                nC.put(N2,temp);
            } else 
                nC.get(N2).add(N1);
        }
        
        Set<Integer> exits = new HashSet<>(); // Exit nodes
        for (int i = 0; i < E; i++) {
            exits.add(in.nextInt()); // the index of a gateway node
        }
        
        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn
            List<Integer> beginPoint = new ArrayList<Integer>();
            beginPoint.add(SI);
            Set<List<Integer>> allRoutes = new HashSet<>();
            findRoutes(allRoutes, beginPoint, exits); // allRoutes returns possible routes (limited to MAX_LINKS_TO_FOLLOW steps long)
            
            // determine the shortest route to an exit, if a tie, take one with the exit.
            int shortest = 10;
            List<Integer> pathToSever = null; // will hold index of path to sever
            for (List<Integer> aR : allRoutes) {
        	   if (aR.size() < shortest) {
        		   shortest = aR.size();
        		   pathToSever = aR;
        	   }
            } 
           
            // sever the one link closest to the exit
            int nodeSever1 = pathToSever.get(0);
            int nodeSever2 = pathToSever.get(1);
            
            System.out.printf("%s %s%n", nodeSever1, nodeSever2);
            
            // Update node links in the connection map
            nC.get(nodeSever1).remove(nodeSever2);
            nC.get(nodeSever2).remove(nodeSever1);
        }
        
    }
    
    /** Used as a recursive method to find all possible routes to an exit node from a starting node.
     * A Maximum of MAX_LINKS_TO_FOLLOW steps was chosen to reduce unnecessary calculations.
     * 
     * @param allRoutes	will fill this Set containing the lists of possible routes to an exit (or a max of MAX_LINKS_TO_FOLLOW steps)
     * @param currentRoute the current list of nodes traversed exploring for an exit
     * @param exits nodes containing an exit
     */
    private static void findRoutes(Set<List<Integer>> allRoutes, List<Integer> currentRoute, Set<Integer> exits) {
        
        int currentNode = currentRoute.get(currentRoute.size() - 1); // last node in the exploratory path so far
        
        // if current Node is an exit, return the pathway and add it to all Routes
        if (exits.contains(currentNode)) {
        	allRoutes.add(currentRoute);
        	return;
        }
        
        // if the current route is longer than MAX_LINKS_TO_FOLLOW, just return the path and stop exploring further to save time.
        if (currentRoute.size() > MAX_LINKS_TO_FOLLOW) {
        	allRoutes.add(currentRoute);
        	return;
        }
        
        // Since we haven't reached an exit node yet and we're less than four steps,
        // get a set of all the unused node connections
       Set<Integer> possibleLinks = new HashSet<>();
       possibleLinks.addAll(nC.get(currentNode));	// Get all possible connections on this node
       possibleLinks.removeAll(currentRoute); 		// Get rid of any nodes we've already hit
        
       // If the set is empty, its a dead end, discard exploratory pathway
       if (possibleLinks.size() == 0) {
    	   return;
       }
       
       // Since possible links exist, go through one by one and pursue each path with the recursive call.
       for (int i: possibleLinks) {
            List<Integer> newBranch = new ArrayList<>(currentRoute);
            newBranch.add(i);
            findRoutes(allRoutes, newBranch, exits);
        }
           
    }
}