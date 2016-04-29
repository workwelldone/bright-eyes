package medium_challenges;

import java.util.Scanner;

/**
 * My response to the APU:Init Phase coding game
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * Given a coordinate system of nodes, the goal is to find the horizontal and vertical neighbors of each node, when they exist.
 * 
 * @author Darren Pearson
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // the number of cells on the X axis
        int height = in.nextInt(); // the number of cells on the Y axis
        in.nextLine();
        
        // Get all all the data into a double array
        char[][] grid = new char[width][height];        
        for (int y = 0; y < height; y++) {
            String line = in.nextLine(); // width characters, each either 0 or .
            for (int x = 0; x < width; x++) {
                grid[x][y] = line.charAt(x);
            }
        }

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y] == '0') {
                    int nextRightX = getRightNode(grid, x, y);
                    int nextBottomY = getDownNode(grid, x, y);
                    System.out.printf("%s %s %s %s %s %s%n", x, y, nextRightX, nextRightX == -1 ? -1 : y
                                                                 , nextBottomY == -1 ? -1 : x, nextBottomY);   
                }
            }
        }
        in.close();
    }
    
    /** Returns the X coordinate of the next node to the right, if any.  Returns -1 if none.  */
    private static int getRightNode(char[][] g, int x, int y) {
        if (x == g.length - 1) return -1; // already at the right edge of the grid
        for (int i = x + 1; i < g.length; i++) {
            if (g[i][y] == '0') return i;
        }
        return -1; // no nodes found
    }
    
    /** Returns the Y coordinate of the next node down, if any.  Returns -1 if none. */
    private static int getDownNode(char[][] g, int x, int y) {
        if (y == g[0].length - 1) return -1;
        for (int j = y + 1; j < g[0].length; j++) {
            if (g[x][j] == '0') return  j;
        }
        return -1; // no nodes found
    }
}