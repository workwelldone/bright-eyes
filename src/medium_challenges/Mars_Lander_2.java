package medium_challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * My response to the Mars Lander 2 coding game.
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * Successfully land a Mars Lander shuttle by controlling thrust and angle of the shuttle.
 * 
 * @author Darren Pearson
 **/

class Mars_Lander_2 {

    public static void main(String args[]) {
        @SuppressWarnings("resource") // Due to infinite loop specifications.
		Scanner in = new Scanner(System.in);
        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        int flatX = 0;
        
        // Map terrain, find index of flat area's left corner
        for (int i = 0; i < surfaceN; i++) {
            x.add(in.nextInt());
            y.add(in.nextInt());
            if (i > 0 && y.get(i).equals(y.get(i - 1))) flatX = i - 1;
        }
        
        int MAX_DESIRED_HSPEED = 30; // absolute value
        int MAX_DESIRED_VSPEED = -18; // down only
        int MAX_ANGLE = 39;
        int LANDING_THRESHOLD = 8;
        int SPEED_TOLERANCE = 7;
        int rOut = 0; // Rotate output
        
        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
            @SuppressWarnings("unused")
			int fuel = in.nextInt(); // the quantity of remaining fuel in liters.
            @SuppressWarnings("unused")
			int rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = in.nextInt(); // the thrust power (0 to 4).

            // While not over flat area, if needed, reverse direction
            if (X > x.get(flatX + 1) && hSpeed > 0)  rOut = MAX_ANGLE;
            if (X < x.get(flatX) && hSpeed < 0) rOut = -MAX_ANGLE;

            // While not over flat area, get to flat area as quickly as possible without going over max speed, reduce speed if going too fast.
            if (X > x.get(flatX + 1)) {
                if (-hSpeed < MAX_DESIRED_HSPEED) rOut = MAX_ANGLE;
                else if (Math.abs(-hSpeed - MAX_DESIRED_HSPEED) < SPEED_TOLERANCE) rOut = 0;
                else rOut = -MAX_ANGLE;
            }
            
            if (X < x.get(flatX)) {
                if (hSpeed < MAX_DESIRED_HSPEED) rOut = -MAX_ANGLE;
                else if (Math.abs(hSpeed - MAX_DESIRED_HSPEED) < SPEED_TOLERANCE) rOut = 0;
                else rOut = MAX_ANGLE;
            }
            
            // If over flat area, get Horizontal Speed to within landing threshold speed as quickly as possible.
            if (X > x.get(flatX) && X < x.get(flatX + 1)) {
                if (hSpeed > LANDING_THRESHOLD) rOut = MAX_ANGLE;
                else if (-hSpeed > LANDING_THRESHOLD) rOut = -MAX_ANGLE;
                else rOut = 0;
            }

            // If just above the ground, level out no matter what
            if (Y - y.get(flatX) < vSpeed * 3) rOut = 0;
            
            // Look for high obstacles, if found keep high until past.
            double HEIGHT_OBSTACLE_THRESHOLD = .5;
            int MIN_ANGLE = 9;
            
                if (X > x.get(flatX + 1)) {
                    for (int i = flatX + 1; i < surfaceN; i++) {
                        if (x.get(i) > X) break;
                        if ((y.get(i) - y.get(flatX)) / ((double)Y - y.get(flatX)) > HEIGHT_OBSTACLE_THRESHOLD) {
                            rOut = -MIN_ANGLE;          
                        }
                    }
                }
                if (X < x.get(flatX)) {
                    for (int i = flatX; i > 0; i--) {
                        if (x.get(i) < X) break;
                        if ((y.get(i) - y.get(flatX)) / ((double)Y - y.get(flatX)) > HEIGHT_OBSTACLE_THRESHOLD) {
                            rOut = MIN_ANGLE;
                        }
                    }
                }
            
            // Look for high landing, keep high
            int HIGH_GROUND_THRESHOLD = 2000;
            int MIN_RESISTANCE_ANGLE = 0;
            
            if (y.get(flatX) > HIGH_GROUND_THRESHOLD) {
                if (X > x.get(flatX + 1)) rOut = MIN_RESISTANCE_ANGLE;
                if (X < x.get(flatX)) rOut = -MIN_RESISTANCE_ANGLE;
                MAX_DESIRED_VSPEED = -1;
            }
            
            // Adjust vertical speed
            if (vSpeed > MAX_DESIRED_VSPEED) System.out.println(rOut + " " + (power > 0 ? --power : 0));
            else System.out.println(rOut + " " + (power < 4 ? ++power : 4));

        }
    }
}
