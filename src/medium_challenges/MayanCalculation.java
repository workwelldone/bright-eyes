package medium_challenges;

import java.util.Arrays;
import java.util.Scanner;

class MayanCalculation {

	// width and height of a mayan numeral
	static int W;
	static int H;
	
	static final int NUM_MAYAN_DIGITS = 20;
	
    public static void main(String args[]) {

        //--------- Set Up ---------------------------------------------------------
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        
        // get mayan character dimensions
        W = in.nextInt();
        H = in.nextInt();
        
        // get mayan numerals
        String[] numeralInput = new String[NUM_MAYAN_DIGITS];
        for (int i = 0; i < H; i++)  numeralInput[i] = in.next();

        // Populate static class with numerals
        @SuppressWarnings("unused")
		MayanCalculation.MayanNumerals numerals = new MayanCalculation.MayanNumerals(numeralInput);
        
        // get first operand
        int S1 = in.nextInt();
        String[] operand = new String[S1];
        for (int i = 0; i < S1; i++)  operand[i] = in.next();
        long firstOperand = decimalValueOfOperand(operand);
        
        // get second operand
        int S2 = in.nextInt();
        operand = new String[S2];
        for (int i = 0; i < S2; i++)  operand[i] = in.next();
        long secondOperand = decimalValueOfOperand(operand);
        
        // get operator
        String operation = in.next();

        //--------- Results ---------------------------------------------------------
        long result = (operation.equals("*") ? firstOperand * secondOperand :
                      operation.equals("/") ? firstOperand / secondOperand :
                      operation.equals("+") ? firstOperand + secondOperand :
                                              firstOperand - secondOperand);
        
        // print result in vertical mayan numerals
        String resultInBase20 = Long.toString(result, 20);
        int numSize = resultInBase20.length();
        for (int i = 0; i < numSize; i++) {
            Arrays.stream(MayanNumerals.getMayanNumeral(resultInBase20.charAt(i))).forEach(s -> System.out.println(s));
        }
        
    } // end of main
    
    
    
    /** Returns the decimal value of the mayan operand */
    private static long decimalValueOfOperand(String[] operand) {
        int numeralCount = operand.length / H;
        long[] numeralValues = new long[numeralCount];
        
        for (int n = 0; n < numeralCount; n++) {
            String[] numeral = new String[H];
            for (int line = 0; line < H; line++) {
                numeral[line] = operand[(n * H) + line];
            }
            numeralValues[n] = (long)Math.pow(NUM_MAYAN_DIGITS, numeralCount -n - 1) * MayanNumerals.decimalValueOfNumeral(numeral);
        }
        return Arrays.stream(numeralValues).sum();
    }
    
    /** Contains the list of mayan numerals and their decimal values */
    private static class MayanNumerals {
        
    	static Numeral[] mayanNumerals = new Numeral[NUM_MAYAN_DIGITS];
    	
        MayanNumerals(String[] inputLines) {
        	for (int i = 0; i < NUM_MAYAN_DIGITS; i++) {
        		mayanNumerals[i] = new Numeral(i, new String[H]);
        	}
        	for (int y = 0; y < H; y++) {
        		for (int d = 0; d < NUM_MAYAN_DIGITS; d++) {
        			for (int x = 0; x < W; x++) {
        				mayanNumerals[d].grid[y] = inputLines[y].substring(d * W, (d + 1) * W);
        			}
        		}
        	}
        }
        
        /** Return mayan character for a base 20 character */
        private static String[] getMayanNumeral(char c) {
            int numeralValue = (Character.isAlphabetic(c)) ? c - 'a' + 10 : Character.getNumericValue(c);
            for (Numeral n: mayanNumerals) {
                if (n.decimal == numeralValue) return n.grid;
            }
            throw new IllegalArgumentException("Must be between 0-19 for a mayan base 20 digit.");
        }
        
        /** Returns base 10 value of a mayan numeral */
        public static long decimalValueOfNumeral(String[] numeral) throws IllegalArgumentException {
            for (Numeral n: mayanNumerals) {
                if (Arrays.equals(n.grid, numeral)) return n.decimal;
            }
            throw new IllegalArgumentException("Numeral not found in the mayan definitions.");
        }
        
        /** Represents a single mayan numeral */
		private class Numeral {
        	final String[] grid;
        	final int decimal;
        	
        	Numeral(int decimal, String[] grid) {
        		this.decimal = decimal;
        		this.grid = grid;	
        	}

            @Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result + decimal;
				result = prime * result + Arrays.hashCode(grid);
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				Numeral other = (Numeral) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (decimal != other.decimal)
					return false;
				if (!Arrays.equals(grid, other.grid))
					return false;
				return true;
			}

			private MayanNumerals getOuterType() {
				return MayanNumerals.this;
			}

			@Override
			public String toString() {
				return "Numeral [grid=" + Arrays.toString(grid) + ", decimal=" + decimal + "]";
			}
        }
    }
}