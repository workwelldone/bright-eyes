package medium_challenges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TeadsSponsoredContest {

    public static void main(String args[]) {
        Map<Integer, List<Integer>> relations = readMap();
        int minSteps = findMinSteps(relations);
        System.out.println(minSteps);
    }

    
	/**
	 * Finds the minimal steps required to disseminate through the people's contacts
	 * @param relations map of person to their contacts
	 * @return the minimum number of steps required
	 */
	public static int findMinSteps(Map<Integer, List<Integer>> relations) {
		// Determine the minimal amount of steps required to completely propogate the advertisement
        int minSteps = Integer.MAX_VALUE;

        // Try a brute force method of testing each person
        for (int i : relations.keySet()) {
            if (relations.keySet().size() > 20000 && relations.get(i).size() > 3 || relations.keySet().size() < 20000 && relations.get(i).size() > 1) {
                List<Integer> count = new ArrayList<>(); // saves the step of each branch
                count.add(0);
                int countIndex = 0; // the index of each branch count
                int linkedFrom = -1; // the previous person in the chain. `-1` indicates this person is the starting point.
    
                findSteps(relations, i, linkedFrom, count, countIndex);
                
                // Find most steps
                int max = 0;
                for (int j : count) {
                    if (j > max) max = j;
                }
                
                // Save the lowest max
                if (max < minSteps) minSteps = max;
            }
        }
		return minSteps;
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
    
	/**
	 * A recursive method to determine the number of steps it takes to reach all persons.  The count of all possible branches
	 * is contained in the count parameter list.
	 * 
	 * @param relations the map of all people to a list of their contacts in all directions.
	 * @param person the current person to determine further contacts.
	 * @param linkedFrom the person who referenced this contact, or -1 if the first person to consider
	 * @param count An Integer List containing the count of each branch followed to the end.
	 * @param countIndex The index of the count integer List following this branch in the recursive operations.
	 */
    public static void findSteps(Map<Integer, List<Integer>> relations, int person, int linkedFrom, List<Integer> count, int countIndex) {
        //System.out.printf("Relations: %s -- person: %d -- linkedFrom: %d -- countList: %s -- countIndex: %d%n", relations, person, linkedFrom, count, countIndex);
        
        List<Integer> contacts = relations.get(person); // all contacts of a person

    	boolean oneConnection = true;
    	int branchCountHolder = 0;
        for (int i= 0; i < contacts.size(); i++) { 	// go through each possible contact
            if (contacts.get(i) != linkedFrom) { 	// ignore if it was the person who just led here
                if (oneConnection) {				// for the first connection keep the count branch that led here, increase by one count, and keep looking for more contacts
                	oneConnection = false;
                	branchCountHolder = count.get(countIndex);
                	count.set(countIndex, count.get(countIndex) + 1);
                	findSteps(relations, contacts.get(i), person, count, countIndex);
                } else {							// for further connections, make a clone of the count branch that led here, increase by one count, and keep looking for more contacts.
                	count.add(branchCountHolder + 1); // clone a new branch count if needed.  If search started in the middle, the new branches should have a fresh start count.
                	findSteps(relations, contacts.get(i), person, count, count.size() - 1);                  
                }
            }
        }
        
    }
    
}
    

