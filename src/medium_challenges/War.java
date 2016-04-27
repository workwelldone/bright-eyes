package medium_challenges;

import java.util.*;

/**
 * My response to the Winamax Sponsored Contest coding game
 * Program specifications are at www.codingame.com in the medium difficulty puzzles.
 * 
 * Determine the winner of the card game of War, given a certain input of cards for two players.
 * 
 * @author Darren Pearson
 **/

class War {

    public static void main(String args[]) {
        
        Scanner in = new Scanner(System.in);
        
        // read in card values (omitting suit) for player one
        List<String> deckOne = new ArrayList<>();
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            String card = in.next();
            deckOne.add(card.length() == 2 ? card.substring(0,1) : card.substring(0,2));
        }
        
        // read in card values (omitting suit) for player two
        List<String> deckTwo = new ArrayList<>();
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            String card = in.next();
            deckTwo.add(card.length() == 2 ? card.substring(0,1) : card.substring(0,2));
        }
        
        String winner = "";
        int round = 0;
  game: while (deckTwo.size() > 0 && deckOne.size() > 0) {
            round++;
            
            // Get fight cards
            List<String> fightOne = new ArrayList<>();
            List<String> fightTwo = new ArrayList<>();
            fightOne.add(deckOne.remove(0));
            fightTwo.add(deckTwo.remove(0));
            
            // war
            while (fightOne.get(fightOne.size() - 1).equals(fightTwo.get(fightTwo.size() - 1))) {
                
                // Check for special case of not having enough cards for war
                if (deckOne.size() < 4 || deckTwo.size() < 4) {
                    winner = "PAT";
                    break game;
                }
                
                for (int i = 0; i < 4; i++) {
                    fightOne.add(deckOne.remove(0));
                    fightTwo.add(deckTwo.remove(0));
                }
            }
            
            // check for battle winner
            if (getCardValue(fightOne.get(fightOne.size() - 1)) > getCardValue(fightTwo.get(fightTwo.size() - 1))) {
                deckOne.addAll(fightOne);
                deckOne.addAll(fightTwo);
            } else {
                deckTwo.addAll(fightOne);
                deckTwo.addAll(fightTwo);
            }
            
        }
        
        // end of game
        if (winner.equals("PAT")) System.out.println(winner);
        else System.out.printf("%s %s", (deckOne.size() == 0) ? ((deckTwo.size() == 0) ? "PAT" : "2") : "1", round);
        
    }
    
    /** Returns integer value of all card ranks including face cards. */
    private static int getCardValue(String rank) {
        switch (rank) {
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
            default: return Integer.valueOf(rank);
        }
    }
    
    
}
