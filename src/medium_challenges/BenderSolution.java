package medium_challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BenderSolution {

    public static void main(String args[]) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int R = in.nextInt();
        int C = in.nextInt();
        in.nextLine();
        List<Node> teleports = new ArrayList<>();
        Node start = null;
        
        // Create (x,y) map and store starting point
        char[][] map = new char[C][R];
        for (int y = 0; y < R; y++) {
            String row = in.nextLine();
            for (int x = 0; x < C; x++){
            	char item = row.charAt(x);
            	map[x][y] = item;
            	if (item == '@') {
            		start = new Node(x,y);
            	}
            	if (item == 'T') {
            		teleports.add(new Node(x,y));
            	}
            }
        }
        
        // Create new robot with map
        Bender bender = new Bender(start, map, teleports);

        // Limit iterations
        boolean circular = false;
        final int MAX_ITERATIONS = 200;
        
        // Collect all moves.
        List<String> moves = new ArrayList<>();
        while (bender.alive && !circular) {
        	moves.add(bender.move());
        	circular = moves.size() > MAX_ITERATIONS;
        }
        
        // Output Result
        if (circular) System.out.println("LOOP");
        else {
        	for (String s: moves) System.out.println(s);
        }
    }
    
    /** Simple object to store coordinate pair */
    private static class Node {
    	final int x, y;
    	Node(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}

    	@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
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
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
    }
    
    /** Object to store state and behavior of Bender */
    private static class Bender {

    	Node position;
    	char[][] map;
    	boolean directionToggle;
    	boolean alive;
    	Direction facing;
    	boolean beerToggle;
    	List<Node> teleports;
    	
    	/** Direction enum includes the ability to find next node based on direction Bender is facing.
    	 */
    	private enum Direction {
    		SOUTH(0, 1), EAST(1, 0), NORTH(0, -1), WEST(-1, 0);
    		
    		private int dx;
    		private int dy;

    		Direction (int dx, int dy) {
    			this.dx = dx;
    			this.dy = dy;
    		}
    		
    		public Node newNode(Node original) {
    			return new Node(original.x + dx, original.y + dy);
    		}
    		
    		public Direction nextDirection(boolean toggle) {
    			if (toggle) {
    				switch (this) {
    				case SOUTH:   return EAST;
    				case EAST:    return NORTH;
    				default:      return WEST;
    				}
    			} else {
    				switch (this) {
    				case WEST:    return NORTH;
    				case NORTH:   return EAST;
    				default:      return SOUTH;
    				}
    			}
    		}
    	}

    	public Bender(Node start, char[][] map, List<Node> teleports) {
    		this.position = start;
    		this.map = map;
    		this.alive = true;
    		this.facing = Direction.SOUTH;
    		this.directionToggle = true;
    		this.beerToggle = false;
    		this.teleports = teleports;
    	}
    	
    	/** Updates the state of bender.
    	    Returns direction of the move.
    	  */
    	public String move() {
    		
    		char currentContent = map[position.x][position.y];

    		// Check for Teleporters
    		if (currentContent == 'T') {
    			position = (teleports.get(0).equals(position)) ? teleports.get(1) : teleports.get(0);
    		}

    		// Check for immediate move command
    		if ((""+currentContent).matches("[NESW]")) {
    			switch (currentContent) {
    			case 'N': facing = Direction.NORTH; position = facing.newNode(position); return Direction.NORTH.toString();
    			case 'W': facing = Direction.WEST;  position = facing.newNode(position); return Direction.WEST.toString();
    			case 'S': facing = Direction.SOUTH; position = facing.newNode(position); return Direction.SOUTH.toString();
    			default:  facing = Direction.EAST;  position = facing.newNode(position); return Direction.EAST.toString();
    			}
    		}
    		
    		// Check for inversion
    		if (currentContent == 'I') {
    			directionToggle = !directionToggle;
    		}

    		// Check for beer
    		if (currentContent == 'B') {
    			beerToggle = !beerToggle;
    		}
    		
    		// Trial next possibility
			Node trial = facing.newNode(position);
    		char content = map[trial.x][trial.y];
    		
    		// Check if Bender dies
    		if (content == '$') {
    			alive = false;
    			return facing.toString();
    		}
    		
    		// Check for beer power to remove X barrier
    		if (beerToggle && content == 'X') {
    			content = ' ';
    			map[trial.x][trial.y] = ' ';
    		}
    		
    		// Check for Obstacles
    		boolean initialCheck = true;
    		
    		while (content == 'X' || content == '#') {

    			// Check for obstacles
    			if (content == 'X' || content == '#') {
    				if (initialCheck) {
    					facing = directionToggle ? Direction.SOUTH : Direction.WEST;
    					initialCheck = false;
    				} else {
    					facing = facing.nextDirection(directionToggle);
    				}
    			}
    			
    			// Update position and facing
    			trial = facing.newNode(position);
    			content = map[trial.x][trial.y];
    		}
    			
    		// If we made it to this point, it's okay to move bender
    		position = facing.newNode(position);
    		if (content == '$') alive = false;
    		return facing.toString();
    	}
    }
}