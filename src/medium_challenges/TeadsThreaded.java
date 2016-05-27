package medium_challenges;

import java.util.*;

/**  
 * This problem is a multi-threaded approach to solving the Teads Graph Theory problem on www.codingame.com.
 * 
 * This was my first attempt at concurrent programming.  Given a grid of interconnected nodes, the goal
 * is to find the optimal way of distributing a message via one contact each hour.
 * 
 * Beyond eliminating the persons with only single contacts, I was unsure how to further optimize
 * the brute force search.  The brute force attempt works well for under 20000 nodes, however it did not
 * work within the prescribed time limits for the problem sets involving 30-50k nodes.
 * 
 * Although this multi-threaded approach did not adequately solve the problem for large problems sets,
 * it did execute as intended and was a good learning experience.
 * 
 * After trying several approaches to fully solving this problem for large problem sets,
 * I will put it on the shelf for a little while I keep learning.
 * 
 * @author darrenpearson
 *
 */

class TeadsThreaded {

    // Map of relations
    private static Map<Integer, List<Integer>> relations;
    
    private static List<Integer> minimums = Collections.synchronizedList(new ArrayList<>());      // minimum steps collected by concurrent threads
    private static List<Integer> rangeBreakUps = Collections.synchronizedList(new ArrayList<>()); // stores the ranges each thread will search.  
    private static int index = 0;  // index used by threads to determine range of responsibility.
    private static final int RANGE_STEP_INCREMENT = 1000;  
    
    static class ConcurrentSearch implements Runnable {
        public void run() {
            int start = 0;
            int end = 0;
            
            synchronized(this) {
                start = index;
                end = ++index;
            }
            int min = findMinSteps(start, end);
            minimums.add(min);
        }
    }
    
    public static void main(String args[]) throws InterruptedException {
        readMap();
        
        if (relations.size() < 30000) minimums.add(findMinSteps(0, relations.size())); 

        // Utilize multi-threaded approach on large node sets
        else {  
            // Establish thread range assignments
            for (int i = 0; i < relations.size(); i += RANGE_STEP_INCREMENT) {
                rangeBreakUps.add(i);
            }
            if (rangeBreakUps.get(rangeBreakUps.size() - 1) != relations.size()) { // Make sure final entry is on the list
                rangeBreakUps.add(relations.size());
            }
            
            // Start thread assignments
            Thread[] threads = new Thread[rangeBreakUps.size()];
            for (int i = 0, end = rangeBreakUps.size() - 1; i < end; i++) {
                threads[i] = new Thread(new ConcurrentSearch());
                threads[i].start();
            }
            for (int i = 0; i < rangeBreakUps.size() - 1; i++) {
                threads[i].join();
            }
        }
        System.out.println(Collections.min(minimums));
    }

	/**
	 * Finds the minimal steps required to disseminate through the people's contacts
	 * @param start index of starting point of check, inclusive
	 * @param end index of ending point of check, exclusive
	 * @return the minimum number of steps required
	 */
	static int findMinSteps(int start, int end) {
		// Determine the minimal amount of steps required to completely propogate the advertisement
        int minSteps = Integer.MAX_VALUE;

        // Try a brute force method of testing each person
        List<Integer> thisThreadTest = new ArrayList<>(relations.keySet());
        
        // Used to permit concurrent threads to handle this list
        thisThreadTest = thisThreadTest.subList(rangeBreakUps.get(start), rangeBreakUps.get(end));

        for (int i : thisThreadTest) {
            if (relations.get(i).size() > 1) { // ignore all persons with only one contact
                List<Integer> count = new ArrayList<>(); // saves the step of each branch
                count.add(0);
                int countIndex = 0; // the index of each branch count
                int linkedFrom = -1; // the previous person in the chain. `-1` indicates this person is the starting point.
    
                findSteps(i, linkedFrom, count, countIndex);
                
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
	 * Reads and stores map of all persons to their contacts in the class field 'relations'
	 * 
	 */
	static void readMap() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of adjacency relations
        
        // class field
        relations = new HashMap<>();
        
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
	}
    
	/**
	 * A recursive method to determine the number of steps it takes to reach all persons.  The count of all possible branches
	 * is contained in the count parameter list.
	 * 
	 * @param person the current person to determine further contacts.
	 * @param linkedFrom the person who referenced this contact, or -1 if the first person to consider
	 * @param count An Integer List containing the count of each branch followed to the end.
	 * @param countIndex The index of the count integer List following this branch in the recursive operations.
	 */
    public static void findSteps(int person, int linkedFrom, List<Integer> count, int countIndex) {
        List<Integer> contacts = relations.get(person); // all contacts of a person

    	boolean oneConnection = true;
    	int branchCountHolder = 0;
        for (int i = 0, cSize = contacts.size(); i < cSize; i++) { 	// go through each possible contact
            if (contacts.get(i) != linkedFrom) { 	// ignore if it was the person who just led here
                if (oneConnection) {				// for the first connection keep the count branch that led here, increase by one count, and keep looking for more contacts
                	oneConnection = false;
                	branchCountHolder = count.get(countIndex);
                	count.set(countIndex, count.get(countIndex) + 1);
                	findSteps(contacts.get(i), person, count, countIndex);
                } else {							// for further connections, make a clone of the count branch that led here, increase by one count, and keep looking for more contacts.
                	count.add(branchCountHolder + 1); // clone a new branch count if needed.  If search started in the middle, the new branches should have a fresh start count.
                	findSteps(contacts.get(i), person, count, count.size() - 1);                  
                }
            }
        }
    }
}
    

