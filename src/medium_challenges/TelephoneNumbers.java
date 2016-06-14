package medium_challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TelephoneNumbers {
    
    static int DigitCounter = 0;
    
    public static void main(String args[]) {		
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        TelephoneNumbers solution = new TelephoneNumbers();

        // List is stored from top to bottom
        List<Digit> top = new ArrayList<>();
        int N = in.nextInt();
        	
        for (int i = 0; i < N; i++) {
            String telephone = in.next();
            List<Digit> cList = top;
            
            for (int j = 0, size = telephone.length(); j < size; j++) {
                int num = Character.getNumericValue(telephone.charAt(j));
                
                // Check for existing link
                Digit cDigit = null;
                for (int h = 0, pointerSize = cList.size(); h < pointerSize; h++) {
                	if (cList.get(h).digit == num) {
                    	cDigit = cList.get(h);
                    	break;
                	}
                }
                
                // Else create a new digit object and add pointer to list
                if (cDigit == null) {
            		cDigit = solution.new Digit(num);
                	cList.add(cDigit);
                	DigitCounter++;
                }
                cList = cDigit.down;
            }
		}
        System.out.println(DigitCounter);
    }
    
    private class Digit {

    	final int digit;
    	List<Digit> down;
    	
    	Digit(int digit) {
    		this.digit = digit;
    		down = new ArrayList<>();
    	}
    }
}