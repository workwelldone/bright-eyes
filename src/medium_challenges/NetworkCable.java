package medium_challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/** This is a solution to the Network Cabling problem on www.codingame.com.
 * Given a grid of buildings, it is our job to find the minimum amount of cable needed to connect each building
 * using a single horizontal line with vertical lines feeding each building.
 * 
 * @author darrenpearson
 *
 */

class NetworkCable {

    public static void main(String args[]) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] x = new int[N];
        int[] y = new int[N];
        
        int maxX = 0;
        int minX = Integer.MAX_VALUE;
        int aveY = 0;
        
        for (int i = 0; i < N; i++) {
            x[i] = in.nextInt();
            y[i] = in.nextInt();
            
            if (x[i] < minX) minX = x[i];
            if (x[i] > maxX) maxX = x[i];
            
            aveY += y[i];
        }
        
        aveY /= N;
                         
        // Start at the mean
        List<Long> lowestMean = new ArrayList<>();
        
        lowestMean.add(findVerticalCableLength(y, aveY));
        lowestMean.add(findVerticalCableLength(y, aveY + 1));
        lowestMean.add(findVerticalCableLength(y, aveY - 1));
        
        long lowest = Collections.min(lowestMean);
        
        // Search down to first node found to see if the cable length shortens
        int yMinDiffDown = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            if (y[i] < aveY && Math.abs(aveY - y[i]) < yMinDiffDown) yMinDiffDown = y[i];
        }
        
        long downTest = findVerticalCableLength(y, yMinDiffDown);
        if (downTest < lowest) lowest = downTest;
        
        // Search up to first node found to see if the cable length shortens
        int yMinDiffUp = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            if (y[i] > aveY && Math.abs(y[i] - aveY) < yMinDiffUp) yMinDiffUp = y[i];
        }
        
        long upTest = findVerticalCableLength(y, yMinDiffUp);
        if (upTest < lowest) lowest = upTest;
        
        System.out.println(lowest + Math.abs(maxX - minX));
    }
    
    public static long findVerticalCableLength(int[] y, int horizontalY) {
        return Arrays.stream(y)
                      .mapToLong(yCoord -> Math.abs(yCoord - horizontalY))
                      .sum();
    }
    
}