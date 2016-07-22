package medium_challenges;

/** Original solution to the scrabble problem in the medium puzzle challenges on Codingame.com
 * 
 */

import java.util.Scanner;

class Scrabble {

    public static void main(String args[]) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        in.nextLine();
        String[] dictionary = new String[N];
        for (int i = 0; i < N; i++) dictionary[i] = in.nextLine();
        String LETTERS = in.nextLine();
        
        int highScore = 0;
        String highWord = "";
        
        // traverse entire dictionary, since words not ordered
        for (String word:dictionary) {
            
            String temp = LETTERS;
            boolean matches = true;
            
            for (String s: word.split("")) {
                if (temp.contains(s)) {
                    temp = temp.replaceFirst(s,"");
                } else {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                int score = getScore(word);
                if (score > highScore) {
                    highScore = score;
                    highWord = word;
                }
            }
        }
        System.out.println(highWord);
    }
    
    static int getScore(String word) {
        int sum = 0;
        for (char c: word.toCharArray()) {
            switch (c) {
                default:                                            sum += 1; break;
                case 'd': case 'g':                                 sum += 2; break;
                case 'b': case 'c': case 'm': case 'p':             sum += 3; break;
                case 'f': case 'h': case 'v': case 'w': case 'y':   sum += 4; break;
                case 'k':                                           sum += 5; break;
                case 'j': case 'x':                                 sum += 8; break;
                case 'q': case 'z':                                 sum += 10; break;
            }
        }
        return sum;
    }
}