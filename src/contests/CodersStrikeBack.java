package contests;

/** AI bot programming on www.codingame.com
 * Takes into consideration thrust, direction, and when to use a single boost.
 * */

import java.util.*;
import java.io.*;
import java.math.*;

class CoderStrikeBack {

	static final int GAME_LAPS = 3;
    static final int CHECK_POINTS = 3;
    
    // Used to determine whether or not to use the final stretch for the boost.
    static final double FINISH_THRESHOLD = 0.30;
    static final int BOOST_MAX_ANGLE = 20;
    static final int DISTANCE_TO_DECELERATE = 100;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // Variables used to determine at which point to use a single boost
        boolean beginningLap = true;
        int lastX = -1;
        int lastY = -1;
        int lap = 1;
        int greatestDistance = 0;
        int nextCheckPoint = 0;
        boolean thrust = false;
        boolean thrustUsed = false;
        int nodeToThrustTowards = 0;
        
        // game loop
        int speed = 0;
        while (true) {
            // Read current position
            int x = in.nextInt();
            int y = in.nextInt();
            
            // Remember starting position
            if (lastX == -1) {
                lastX = x;
                lastY = y;
            }
            
            // Read next checkpoint
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();
            
            // Perform when checkpoint is passed
            if (nextCheckpointX != lastX && nextCheckpointY != lastY) {
                
                // Update checkpoint counter
                if (++nextCheckPoint > CHECK_POINTS) {
                    nextCheckPoint = 0;
                    if (beginningLap = true) beginningLap = false;
                }
                
                // Update lap counter
                // The beginningLap condition was required to avoid updating the lap variable on lap 1.
                if (!beginningLap && nextCheckPoint == 1) lap++;

                // On first lap determine the greatest distance between nodes
                if (lap == 1) {
                    int distance = (int)(Math.sqrt(Math.pow(lastX - nextCheckpointX, 2) + Math.pow(lastY - nextCheckpointY, 2)));
                    
                    // Check for conditions to thrust towards the finish line
                    // FINISH_THRESHOLD allows for the opportunity to choose the finish line boost, even if it is a smaller distance by this percentage.
                    if ((nextCheckPoint == 0) && (distance > greatestDistance) || (distance / (double)greatestDistance) > FINISH_THRESHOLD){
                        nodeToThrustTowards = 0;
                    } else {
                        
                        // Track the possibility of thrust towards other non-finish line checkpoints.
                        if (distance > greatestDistance) {
                            greatestDistance = distance;
                            nodeToThrustTowards = nextCheckPoint;
                        }
                    }
                }
                
                thrust = (!thrustUsed && lap == GAME_LAPS && nextCheckPoint == nodeToThrustTowards);
                
                // Remember last checkpoint
                lastX = nextCheckpointX;
                lastY = nextCheckpointY;
            }

            if (nextCheckpointAngle > 90 || nextCheckpointAngle < -90 || nextCheckpointDist < DISTANCE_TO_DECELERATE) speed = 0;
            else speed = 100;
            
            System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + ((thrust && (Math.abs(nextCheckpointAngle) < BOOST_MAX_ANGLE)) ? "BOOST" : speed));
            if (thrust) thrustUsed = true;
        }
    }
}