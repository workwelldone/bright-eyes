package medium_challenges;

/** Original solution to classic problem "The Gift", on www.codingame.com. */

import java.util.*;

class TheGift {

    public static void main(String args[]) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int C = in.nextInt();
        int[] budget = new int[N];
        int[] contribution = new int[N];
        
        int sum = 0;
        for (int i = 0; i < N; i++) {
            budget[i] = in.nextInt();
            sum += budget[i];
        }
        if (sum < C) System.out.println("IMPOSSIBLE");
        else {
            Arrays.sort(budget);
            int baseEquality = C / N;
            int startCloseEqualityHere = 0;
            int moneyContributed = 0;
            for (int i = 0; i < N; i++) {
                if (budget[i] < baseEquality) {
                    moneyContributed += budget[i];
                    contribution[i] = budget[i];
                    baseEquality = (C - moneyContributed) / (N - i - 1);
                } else {
                    startCloseEqualityHere = i;
                    break;
                }
            }
            Arrays.fill(contribution,startCloseEqualityHere,N,baseEquality);
            
            // distribute the remainder
            int contributorsLeft = N - startCloseEqualityHere;
            int remainder = (C - moneyContributed) % contributorsLeft;
            for (int i = 0; i < remainder; i++) {
                contribution[N - i - 1] += 1;
            }
            
            Arrays.stream(contribution).mapToObj(c -> "" + c).forEach(c -> System.out.println(c));
           
        }
    }
}