package medium_challenges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** 
 * This is a non-brute force approach to solving the Teads Challenge at codingame.com.
 * 
 * This finds the longest path that exists in the connected network.
 * The minimum time to navigate through the network will be the number of nodes in the
 * longest path divided by two, after subtracting the center starting node.
 * 
 * To find the longest path:
 * 1.  Map each node to its connected nodes.
 * 2.  Map each node to a count value, starting with one for itself.  The count value
 *     will be used to record the maximum branch size behind the node as it goes up the tree.
 * 3.  Make a pass through the map to find every node which is connected to one or more end of
 *     line nodes (nodes with only one connection) AND has exactly one connection that is NOT
 *     currently an end of line node.  Consider the branch counts of each of the end of line nodes and
 *     only add the greatest to the center node to make a new total.
 * 4.  Eliminate the end of line nodes, and update the new connection map.
 * 5.  Repeat step 3 and 4 until there is only one center node with connections, or there are just two nodes remaining.
 * 6.  If the number of connection nodes is greater than two, add the two greatest counts plus 1 to find the length of the longest path.
 * 7.  If only two nodes remain, just add the sum of their counts.
 * 8.  Special cases would be zero or one nodes, which would require zero time.
 * 
 * @author darrenpearson
 *
 */

class Solution {

    public static void main(String args[]) {

        // Read and Create Map of Relations
    	Map<Integer, List<Integer>> relations = readMap();
        
        // This map contains the maximum branch saize behind this node.
        // One is default because it counts itself.
        Map<Integer, Integer> count = new HashMap<>();
        for (Integer i: relations.keySet()) {
            count.put(i,1);
        }

        boolean finished = false;
        
        while (!finished) {
        	
        	/* Make a pass through the entire graph to evaluate and combine each end of line node
        	 * connected to a node which is connected to exactly one non-end of line node. */
        	
        	List<Integer> nodesToRemove = new ArrayList<>();
        	
        	search: for (Integer i: relations.keySet()) {
        	            
		        		if (relations.get(i).size() == 1) continue search;
		        		
		        		// Stop search if there is only one center node remaining
		        		if (relations.get(i).size() == relations.size() - 1) {
		        		    finished = true;
		        		    break search;
		        		}
		        		
		        		// Collect end of line nodes and find if only one middle connection exists.
			        	List<Integer> endOfLineNodes = new ArrayList<>();
			        	int middleNodeCount = 0;
			        	for (Integer j : relations.get(i)) {
			        		if (relations.get(j).size() == 1) {
			        			endOfLineNodes.add(j);
			        		} else {
			        			middleNodeCount++;
			        			if (middleNodeCount > 1) {
			        			continue search;
			        		    }
			        		}
			        	}
			        	if (middleNodeCount != 1) continue search;

			        	// Find the largest end of line and adjust the new total on the center node
			        	int largest = 0;
			        	for (Integer j : endOfLineNodes) {
			        		if (count.get(j) > largest) largest = count.get(j);
			        		count.remove(j);
			        		nodesToRemove.add(j);
			        		relations.get(i).remove(j);
			        	}
			        	
			        	// Update center node count
			        	count.put(i, count.get(i) + largest);
        	}
        	
        	// Remove nodes that have been combined
        	for (Integer i: nodesToRemove) {
        	    relations.remove(i);
        	}
    	}
    	
    	if (relations.size() == 1) System.out.println(count.values()
    	                                                   .stream()
    	                                                   .mapToInt(a -> a)
    	                                                   .sum());
    	else {
            // sum the largest two connecting nodes, add one, divide by two, floor.    
            int largest = 0;
            int secondLargest = 0;
            for (int i: relations.keySet()) {
                if (count.get(i) > largest) {
                    secondLargest = largest;
                    largest = count.get(i);
                } else {
                    if (count.get(i) > secondLargest) {
                        secondLargest = count.get(i);
                    }
                }
            }
            System.out.println((int)Math.floor((largest + secondLargest + 1) / 2));
    	}
    }
    
	/**
	 * Reads and returns map of all persons to their contacts.
	 * 
	 * @return map of persons to their contacts
	 */
	public static Map<Integer, List<Integer>> readMap() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of adjacency relations
        
        Map<Integer,List<Integer>> relations = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt(); // the ID of a person which is adjacent to yi
            int yi = in.nextInt(); // the ID of a person which is adjacent to xi
            
            if (!relations.containsKey(xi)) {
                List<Integer> temp = new ArrayList<>();
                temp.add(yi);
                relations.put(xi, temp);
            } else {
                List<Integer> temp = relations.get(xi);
                temp.add(yi);
            }
            
            if (!relations.containsKey(yi)) {
                List<Integer> temp = new ArrayList<>();
                temp.add(xi);
                relations.put(yi, temp);
            } else {
                List<Integer> temp = relations.get(yi);
                temp.add(xi);
            }
        }
		return relations;
	}
}
    