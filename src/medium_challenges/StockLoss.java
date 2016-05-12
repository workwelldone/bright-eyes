package medium_challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Determines the maximum possible stock loss considering the history
 * of the stock.
 * 
 * @author darrenpearson
 *
 */
public class StockLoss {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        
        int high = -1;
        int low = Integer.MAX_VALUE;
        List<Integer> losses = new ArrayList<>();
        
        for (int i = 0; i < N; i++) {
            int n = in.nextInt();
            
            if (n > high) {
                if (high != -1) {
                    losses.add(low - high);
                }
                high = n;
                low = high;
            }
            if (n < low) low = n;
        }
        in.close();

        if (high > low) losses.add(low - high);
        
        // Find greatest loss, if no loss then report 0.
        int greatestLoss = 0;
        for (int i: losses) {
            if (i<greatestLoss) greatestLoss = i;
        }

        System.out.println(greatestLoss);
        
    }
    
}

