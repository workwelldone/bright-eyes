package medium_challenges;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * My response to the ParnoidAndroid coding game
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * A puzzle to guide a stream of drones to an exit by immobilizing lead drones to block certain pathways.
 * 
 * @author Darren Pearson
 **/

class ParanoidAndroid {

    @SuppressWarnings("unused")
	public static void main(String args[]) {
        @SuppressWarnings("resource") // Due to infinite loop specifications
		Scanner in = new Scanner(System.in);
        int nbFloors = in.nextInt(); // number of floors
        int width = in.nextInt(); // width of the area
        int nbRounds = in.nextInt(); // maximum number of rounds
        int exitFloor = in.nextInt(); // floor on which the exit is found
        int exitPos = in.nextInt(); // position of the exit on its floor
        int nbTotalClones = in.nextInt(); // number of generated clones
        int nbAdditionalElevators = in.nextInt(); // ignore (always zero)
        int nbElevators = in.nextInt(); // number of elevators
        
        Map<Integer,Integer> elevators = new HashMap<>(); // elevator floor maps to position on floor (assuming on elevator per floor)
        for (int i = 0; i < nbElevators; i++) {
            elevators.put(in.nextInt(), in.nextInt()); // floor, position
        }

        // game loop
        while (true) {
            int cloneFloor = in.nextInt(); // floor of the leading clone
            int clonePos = in.nextInt(); // position of the leading clone on its floor
            String direction = in.next(); // direction of the leading clone: LEFT or RIGHT

            if (direction.equals("NONE")) {
                System.out.println("WAIT");
                continue;
            }
            
            // Check if lead clone is going in direction of exit or secondarily elevator on the current floor,
            // if not, block.
            
            if ((exitFloor == cloneFloor) &&
                    ((direction.equals("RIGHT") && exitPos < clonePos) ||
                     (direction.equals("LEFT")  && exitPos > clonePos))) {
                  System.out.println("BLOCK");
                  continue;
            } else if ((elevators.get(cloneFloor) != null) &&
                      ((direction.equals("RIGHT") && elevators.get(cloneFloor) < clonePos) ||
                      (direction.equals("LEFT")  && elevators.get(cloneFloor) > clonePos))) {
                  System.out.println("BLOCK"); 
                  continue;
            }
            System.out.println("WAIT");
        }
    }
}
