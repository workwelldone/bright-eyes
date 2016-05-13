package medium_challenges;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class IndyEscape {

    public static void main(String args[]) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int w = in.nextInt(); // number of columns.
        int h = in.nextInt(); // number of rows.
        in.nextLine();
        
        // set up grid
        int[][] grid = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                grid[x][y] = in.nextInt();
            }
            in.nextLine();
        }
        
        @SuppressWarnings("unused")
		int EX = in.nextInt(); // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).
    
        // Map kinds of blocks
        Map<Integer,Map<String,String>> blocks = new HashMap<>();
        for (int i = 1; i < 14; i++) blocks.put(i,getMap(i));
        
        // game loop
        while (true) {
            int xI = in.nextInt();
            int yI = in.nextInt();
            String entry = in.next();
            
            String exit = blocks.get(grid[xI][yI]).get(entry);
            switch (exit) {
                case "BOTTOM": yI++; break;
                case "LEFT":   xI--; break;
                default:       xI++; break; // case "RIGHT"
            }
            System.out.println(xI + " " + yI);
        }
    }
    
    public static Map<String,String> getMap(int i) {
        
        Map<String,String> inOut = new HashMap<>();
        
        switch (i) {
            case 0:     break;
            case 1:     inOut.put("TOP",    "BOTTOM");
                        inOut.put("LEFT",   "BOTTOM");
                        inOut.put("RIGHT",  "BOTTOM");
                        break;
            case 2:
            case 6:     inOut.put("RIGHT",  "LEFT");
                        inOut.put("LEFT",   "RIGHT");
                        break;
            case 3:     inOut.put("TOP",    "BOTTOM");
                        break;
            case 4:     inOut.put("TOP",    "LEFT");
                        inOut.put("RIGHT",  "BOTTOM");
                        break;
            case 5:     inOut.put("LEFT",   "BOTTOM");
                        inOut.put("TOP",    "RIGHT");
                        break;
            case 7:     inOut.put("TOP",    "BOTTOM");
                        inOut.put("RIGHT",  "BOTTOM");
                        break;
            case 8:     inOut.put("LEFT",   "BOTTOM");
                        inOut.put("RIGHT",  "BOTTOM");
                        break;
            case 9:     inOut.put("LEFT",   "BOTTOM");
                        inOut.put("TOP",    "BOTTOM");
                        break;
            case 10:    inOut.put("TOP",    "LEFT");
                        break;
            case 11:    inOut.put("TOP",    "RIGHT");
                        break;
            case 12:    inOut.put("RIGHT",  "BOTTOM");
                        break;
            default:    inOut.put("LEFT",   "BOTTOM");
                        break;
        }
        
        return inOut;
    }
    
}