package hard_challenges;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;

class TheLabyrinth {
    
    enum Direction {
	    	UP   (0, -1),
	    	DOWN (0, 1),
	    	LEFT (-1, 0), 
	    	RIGHT(1, 0);
	    
	    	private final int dc; // delta column
	    	private final int dr; // delta row
	    	
	    	Direction(int dc, int dr) {
	    		this.dc = dc;
	    		this.dr = dr;
	    	}
	    	
	    	public int dc() { return dc; }
	    	public int dr() { return dr; }
	    	
	    	/** Returns a new Direction 90 degrees counterclockwise of the current direction */
	    	public Direction left() {
	    		switch (this) {
	    		case UP: 	return LEFT;
	    		case DOWN: 	return RIGHT;
	    		case LEFT: 	return DOWN;
	    		default: 	return UP;
	    		}
	    	}
	    	
	    	/** Returns a new Direction 90 degrees clockwise of the current direction */
	    	public Direction right() {
	    		switch (this) {
	    		case UP: 	return RIGHT;
	    		case DOWN: 	return LEFT;
	    		case LEFT: 	return UP;
	    		default: 	return DOWN;
	    		}
	    	}
	    	
	    	/** Returns a new Direction reverse of the current direction */
	    	public Direction reverse() {
	    		switch (this) {
	    		case UP: 	return DOWN;
	    		case DOWN: 	return UP;
	    		case LEFT: 	return RIGHT;
	    		default: 	return LEFT;
	    		}
	    	}
    }
    
    /** Maps the description of the grid object to its character */
    enum Code {
	    	WALL('#'),
	    	OPEN('.'),
	    	START('T'),
	    	CONTROL('C'),
	    	UNKNOWN('?');
	    	
	    	private final char symbol;
	    	
	    	Code(char symbol) { this.symbol = symbol; }
	    	
	    	char symbol() { return this.symbol; }
    }
    
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt(); // number of rows.
        int C = in.nextInt(); // number of columns.
        int A = in.nextInt(); // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.

        char[][] grid = new char[R][C];
        boolean alarmActivated = false;
        Direction facing = Direction.UP;
        Node start = null; // initial starting position
        Node controlRoom = null; // location of control room
        
        // game loop
        while (true) {
            int KR = in.nextInt(); // row where Kirk is located.
            int KC = in.nextInt(); // column where Kirk is located.
            Node kirk = new Node(KR, KC);
            if (start == null) start = new Node(KR, KC);
            
            // Read currently known grid
            for (int i = 0; i < R; i++) {
                grid[i] = in.next().toCharArray(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
            }
            seeArray(grid); // just for testing
            
            // if location of control room is not known, keep scanning each game loop
            if (controlRoom == null) controlRoom = scanForControlRoom(grid, KR, KC);
            
            if (kirk.equals(controlRoom)) alarmActivated = true;
  
            // Determine the next move
            Direction nextMove = null;
            
            if (!alarmActivated) {
            
	            // if control room has not yet been found, simply navigate using the left-hand rule
	            if (controlRoom == null) {
		            nextMove = hugLeftWall(grid, kirk, facing);
		            
		        // if control room has been found then follow the shortest route
	            } else {
	            	RecursiveLabyrinthSearch search = new RecursiveLabyrinthSearch(grid, kirk, controlRoom);
	            	nextMove = search.getNextMove(kirk);
	            	if (nextMove == null) nextMove = hugLeftWall(grid, kirk, facing);
	            }
            
	        // Control Room found and alarm is activated
            } else {
            	RecursiveLabyrinthSearch search = new RecursiveLabyrinthSearch(grid, kirk, start);
            	nextMove = search.getNextMove(kirk);
            	if (nextMove == null) nextMove = hugLeftWall(grid, kirk, facing);
            }
            
            System.out.println(nextMove);
            facing = nextMove;
        }
    }
    
    /** Scans the the 5 square grid centered around kirk, looking for control room.
     * @param grid 
     * @param KR the row kirk is currently located on.
     * @param KC the column kirk is currently located on.
     * @return the Node of the control room, if found, otherwise null.
     */
    static Node scanForControlRoom(char[][] grid, int KR, int KC) {
    		// Establish grid parameters to check
    		int leftColumn = (KC - 2 >= 0) ? KC - 2 : (KC - 1 >= 0) ? KC - 1 : KC;
    		int rightColumn = (KC + 2 < grid[0].length) ? KC + 2 : (KC + 1 < grid[0].length) ? KC + 1 : KC;
    		int topRow = (KR - 2 >= 0) ? KR - 2 : (KR - 1 >= 0) ? KR - 1 : KR;
    		int bottomRow = (KR + 2 < grid.length) ? KR + 2 : (KR + 1 < grid.length) ? KR + 1 : KR;
    		
    		for (int row = topRow; row <= bottomRow; row++) {
    			for (int column = leftColumn; column <= rightColumn; column++) {
    				if (grid[row][column] == Code.CONTROL.symbol()) {
    					return new Node(row, column);
    				}
    			}
    		}
    		return null;
    }
    
    /** Determines the correct direction to follow if following a maze using the left hand rule.
     * 
     * @param grid a labyrinth grid of ASCII characters
     * @param currentLocation Node object of current location
     * @param nowFacing current Direction person is facing within the labyrinth
     * @return the next Direction to go in order to follow the left wall
     */
    static Direction hugLeftWall(char[][] grid, Node currentLocation, Direction nowFacing) {
        // obtain all relevant information
    	Node toLeft = currentLocation.moveTo(nowFacing.left());
    	Node straightAhead = currentLocation.moveTo(nowFacing);
    	Node toRight = currentLocation.moveTo(nowFacing.right());
    	Node backLeftCorner = toLeft.moveTo(nowFacing.reverse());
    
    	// determine which nodes are walls
        boolean wallToLeft = grid[toLeft.r][toLeft.c] == Code.WALL.symbol;
    	boolean wallToRight = grid[toRight.r][toRight.c] == Code.WALL.symbol;
    	boolean wallStraightAhead = grid[straightAhead.r][straightAhead.c] == Code.WALL.symbol;
    	boolean wallBackLeftCorner = grid[backLeftCorner.r][backLeftCorner.c] == Code.WALL.symbol;
    	
    	// results
    	if (wallToLeft) {
    		if (!wallStraightAhead) return nowFacing;
    		else if (!wallToRight) return nowFacing.right();
    		else return nowFacing.reverse();
    	} else {
    		if (wallBackLeftCorner) return nowFacing.left();
    		else if (!wallStraightAhead) return nowFacing;
    		else if (!wallToRight) return nowFacing.right();
    		else return nowFacing.reverse();
    	}
    }
    
    /** Self-contained unit containing row and column elements for a grid node.
     */
    static class Node {
    	int r;
    	int c;
    	
    	Node(int r, int c) {
    		this.r = r;
    		this.c = c;
    	}
    	
    	public Node moveTo(Direction direction) {
    		return new Node(this.r + direction.dr, this.c + direction.dc);
    	}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + c;
			result = prime * result + r;
			return result;
		}

		@Override
		public String toString() {
			return "Node [r=" + r + ", c=" + c + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (c != other.c)
				return false;
			if (r != other.r)
				return false;
			return true;
		}
    	
    }

    static private void seeArray(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            System.err.println(Arrays.toString(grid[i]));
        }
    }
    
    static class RecursiveLabyrinthSearch {
    	
    	int width;
    	int height;
    	char[][] grid; // The maze
    	List<ArrayList<Node>> successfulPathways = new ArrayList<>();
    	Node start, target;
  
    	/** Fills a Set with possible ArrayLists of directions to go to find target.
    	 * If no successful pathways found, the set will be empty.
    	 * 
    	 * @param grid char[row][col]
    	 * @param start starting Node
    	 * @param end target Node
    	 */
    	public RecursiveLabyrinthSearch(char[][] grid, Node start, Node target) {
    		this.grid = grid;
    		width = grid[0].length;
    		height = grid.length;
    		this.start = start;
    		this.target = target;
    		recursiveSearch(start, new ArrayList<Node>());
    	}
    	
    	public int shortestPath() {
    		int shortest = Integer.MAX_VALUE;
    		if (successfulPathways.isEmpty()) return Integer.MAX_VALUE;
    		else {
    			shortest = Integer.MAX_VALUE;
    			for (ArrayList<Node> l:successfulPathways) {
    				int count = l.size();
    				if (count < shortest) shortest = count;
    			}
    		}
    		return shortest;
    	}
    	
    	private void recursiveSearch(Node current, ArrayList<Node> possiblePathway) {
    		if (current.equals(target)) {
    			possiblePathway.add(current);
    			successfulPathways.add(possiblePathway);
    			return;
    		}
    		
    		possiblePathway.add(current);
    		if (possiblePathway.size() > shortestPath()) return;
    		
    		// Check down
    		if (current.r < height - 1) {
    			Node down = new Node(current.r + 1, current.c);
    			if (isOpenToPursue(down, possiblePathway)) recursiveSearch(down, new ArrayList<Node>(possiblePathway));
    		}
    		
    		// Check up
    		if (current.r > 0) {
	    		Node up = new Node(current.r - 1, current.c);
	    		if (isOpenToPursue(up, possiblePathway)) recursiveSearch(up, new ArrayList<Node>(possiblePathway));
    		}
	    		
    		// Check right
    		if (current.c < width - 1) {
	    		Node right = new Node(current.r, current.c + 1);
	    		if (isOpenToPursue(right, possiblePathway)) recursiveSearch(right, new ArrayList<Node>(possiblePathway));
    		}
	    		
    		// Check left
    		if (current.c > 0) {
	    		Node left = new Node(current.r, current.c - 1);
	    		if (isOpenToPursue(left, possiblePathway)) recursiveSearch(left, new ArrayList<Node>(possiblePathway));
    		}
	    		
    		// If no options, stop search.
    		return;
    	}
    	
    	/** Returns a list of nodes, in order, of the shortest known pathway from the start to the target.
    	 * Null is returned if no pathway is currently known.
    	 */
    	public List<Node> getShortestPathway() {
    		if (successfulPathways.isEmpty()) return null;
    		if (successfulPathways.size() == 1) return successfulPathways.get(0);
    		
    		// If more than two options, find the shortest
    		int fewestSteps = successfulPathways.get(0).size();
    		List<Node> shortestPath = successfulPathways.get(0);
    		for (List<Node> paths : successfulPathways) {
    			if (paths.size() < fewestSteps) {
    				fewestSteps = paths.size();
    				shortestPath = paths;
    			}
    		}
    		return shortestPath;
    	}
    	
    	/**
    	 * @param current current location of Kirk
    	 * @return the direction to travel in order to follow the shortest path.  Null is returned if no solution is available.
    	 */
    	public Direction getNextMove(Node current) throws IllegalArgumentException{
    		List<Node> shortestPathway = getShortestPathway();
    		if (shortestPathway == null) return null;
    		int currentIndex = shortestPathway.indexOf(current);
    		if (currentIndex == -1) return null;
    		Node next = shortestPathway.get(currentIndex + 1);
    		if (next.r == current.r) {
    			if (next.c > current.c) return Direction.RIGHT;
    			else return Direction.LEFT;
    		} else {
    			if (next.r > current.r) return Direction.DOWN;
    			else return Direction.UP;
    		}
    	}
    	
    	private boolean isOpenToPursue(Node nodeToCheck, ArrayList<Node> pathway) {
    		char currentChar = grid[nodeToCheck.r][nodeToCheck.c];
    		return (currentChar != Code.UNKNOWN.symbol() && currentChar != Code.WALL.symbol() && !pathway.contains(nodeToCheck));
        			//!considered[nodeToCheck.r][nodeToCheck.c]);
    	}
    }
}
