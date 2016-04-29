package medium_challenges;

import java.util.Scanner;

/**
 * My response to the Heat Detector coding game
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * A bomb is located within a grid.  Using a heat detector to show current direction,
 * it must be found within a limited amount of turns.
 * 
 * @author Darren Pearson
 **/

class HeatDetector {

    public static void main(String args[]) {
        @SuppressWarnings("resource") // Due to infinite loop specifications.
		Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // width of the building.
        int H = in.nextInt(); // height of the building.
        @SuppressWarnings("unused")
		int N = in.nextInt(); // maximum number of turns before game over.
        int x = in.nextInt();
        int y = in.nextInt();

        int highLimit = 0;
        int lowLimit = H - 1;
        int leftLimit = 0;
        int rightLimit = W - 1;
        
        // game loop
        while (true) {
            String bombDir = in.next(); // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)

            // Set inclusive boundary limits, meaning the bomb can be within the limit.
            if (bombDir.contains("U")) lowLimit = y - 1;
            if (bombDir.contains("D")) highLimit = y + 1;
            if (bombDir.contains("R")) leftLimit = x + 1;
            if (bombDir.contains("L")) rightLimit = x - 1;

            x = Math.round((leftLimit + rightLimit) / 2);
            y = Math.round((highLimit + lowLimit) / 2);

            System.out.printf("%d %d%n", x, y);
        }
    }
}