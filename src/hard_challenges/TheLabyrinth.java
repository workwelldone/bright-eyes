package hard_challenges;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
        boolean controlFound = false;
        Direction facing = Direction.UP;
        List<TravelLog> travel = new ArrayList<>(); // a bread crumb trail, holding the number of times traveled between two nodes.
        
        // game loop
        while (true) {
            int KR = in.nextInt(); // row where Kirk is located.
            int KC = in.nextInt(); // column where Kirk is located.
            Node kirk = new Node(KR, KC);
            
            for (int i = 0; i < R; i++) {
                grid[i] = in.next().toCharArray(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
            }
            
            // Determine the next move
            Direction nextMove = null;
            if (!controlFound) {
	            nextMove = findAdjacent(grid, kirk, Code.CONTROL);
	            if (nextMove != null) controlFound = true;
	            else nextMove = hugLeftWall(grid, kirk, facing);
	            
	            // Record how many times we traveled between these two nodes
	            Set<Node> twoNodes = new HashSet<>();
	            twoNodes.add(kirk);
	            twoNodes.add(kirk.moveTo(nextMove));
	            
	            // Make a temporary TravelLog between the two
	            TravelLog currentTwo = new TravelLog(twoNodes);
	            
	            // Check and see if a travel log exists already for these two nodes, if so increase the count.
	            // If not, add them to the log.
	            if (travel.contains(currentTwo)) travel.get(travel.indexOf(currentTwo)).count++;
	            else travel.add(currentTwo);
	            
            } else {
            	nextMove = followOnceTroddenPath(kirk, facing, travel);
            }
            
            System.out.println(nextMove);
            facing = nextMove;
        }
    }
    
    /** Determines the correct direction to follow to return to the original position.
     * It does so by following the path which has only been traveled once, to avoid all dead ends.
     * 
     * @param grid a labyrinth grid of ASCII characters
     * @param currenLocation Node object of current location
     * @param nowFacing current Direction person is facing within the labyrinth
     * @param travel the bread crumb count of travel between two nodes throughout the labyrinth
     * @returns the next Direction to go in order to follow the path which has only been travelled once.
     */
    static Direction followOnceTroddenPath(Node currentLocation, Direction nowFacing, List<TravelLog> travel) {

    	if (testTravelCountForNodeInThisDirection(currentLocation, nowFacing, travel)) return nowFacing;
    	else if (testTravelCountForNodeInThisDirection(currentLocation, nowFacing.left(), travel)) return nowFacing.left();
    	else if (testTravelCountForNodeInThisDirection(currentLocation, nowFacing.right(), travel)) return nowFacing.right();
    	else return nowFacing.reverse();
    }

    
    /** Finds the one direction that is only traveled over once in the initial left-hand traversal of the labyrinth, and is
     * thus the shortest known path in reverse.
     * 
     * @param currentLocation Node object of current location
     * @param direction relative direction to test from current location
     * @param travel the bread crumb count of travel between two nodes throughout the labyrinth
     * @return true if it is the Direction leads to the path that has only been traveled once before, and is thus the shortest known path.
     */
    static private boolean testTravelCountForNodeInThisDirection(Node currentLocation, Direction direction, List<TravelLog> travel) {
    	Node adjacentNode = currentLocation.moveTo(direction);
    	Set<Node> pairNodes = new HashSet<>();
    	pairNodes.add(adjacentNode);
    	pairNodes.add(currentLocation);
    	TravelLog test = new TravelLog(pairNodes);
    	if (travel.contains(test) && travel.get(travel.indexOf(test)).count == 1) return true;
    	else return false;
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
    
    /* Self-contained travel log between nodes.
     * This hold the count of the number of times traveled between the two nodes, in either direction.
     */
    static private class TravelLog {
    	Set<Node> travel;
    	int count;
    	
    	TravelLog(Set<Node> travel) {
    		this.travel = travel;
    		count = 1;
    	}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((travel == null) ? 0 : travel.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TravelLog other = (TravelLog) obj;
			if (travel == null) {
				if (other.travel != null)
					return false;
			} else if (!travel.equals(other.travel))
				return false;
			return true;
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

    /**Determines if a argument code is located in an adjacent cell (only up, down, left, or right),
     * and returns the direction to move toward the matching node if found, otherwise returns null.
     * It is expected that this method will only be used for codes that exist only once in the labyrinth.
     * 
     * @param grid grid of nodes
     * @param currentLocation Node of currentLocation
     * @param code	Code to look for in adjacent nodes
     * @return	Direction to move toward code, if found, otherwise null.
     */
    static Direction findAdjacent(char[][] grid, Node currentLocation, Code code) {
    	if (testNodeContents(grid, currentLocation, code, Direction.UP))    return Direction.UP;
    	if (testNodeContents(grid, currentLocation, code, Direction.DOWN))  return Direction.DOWN;
    	if (testNodeContents(grid, currentLocation, code, Direction.LEFT))  return Direction.LEFT;
    	if (testNodeContents(grid, currentLocation, code, Direction.RIGHT)) return Direction.RIGHT;
    	return null;
    }
    
    /** Tests Node Contents in a certain direction relative to the current Location.
     * If the contents of the node match the Code character then true is returned.
     * 
     * @param grid grid of nodes
     * @param currentLocation Node of currentLocation
     * @param code Code to look for in adjacent nodes
     * @param direction the SPECIFIC direction to look at the adjacent node
     * @return true if the argument Code character is found in the particular node found by going the Direction argument
     */
    static private boolean testNodeContents(char[][] grid, Node currentLocation, Code code, Direction direction) {
    	Node test = currentLocation.moveTo(direction);
    	if (grid[test.r][test.c]==code.symbol) return true;
    	else return false;
    }
}
