package contests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DropTheBlocks {
	
	public static final int C = 6;   // Number of Columns in Grid
	public static final int R = 12;  // Number of Rows in Grid
	public static final int ADDITIONAL_COLOR_PAIRS_TO_CONSIDER = 1;  // Number of color pairs to evaluate beyond first pair.
	
	public static void main (String...args) {
	    @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            
            // Upcoming Blocks
            int[] colorA = new int[8];
            int[] colorB = new int[8];
            for (int i = 0; i < 8; i++) {
                colorA[i] = in.nextInt(); // color of the first block
                colorB[i] = in.nextInt(); // color of the attached block
            }
            
            // Read My Grid
            char[][] grid = new char[C][R];
            
            for (int row = 0; row < R; row++) {
                String mapLine = in.next();
                for (int col = 0; col < C; col++) {
                    grid[col][row] = mapLine.charAt(col);  // One line of the map ('.' = empty, '0' = skull block, '1' to '5' = colored block)
                }
            }
            
            // Read Opponents Grid
            for (int row = 0; row < R; row++) {
                @SuppressWarnings("unused")
				String mapLine = in.next(); 
            }
  
            // Determine which column to drop the block
            int[] columnScores = getScoreByColumn(grid, colorA, colorB, ADDITIONAL_COLOR_PAIRS_TO_CONSIDER);
            
            // For now just choose the first column with the highest score.
            int highest = 0;
            int columnToChoose = 0;
            for (int i = 0; i < C; i++) {
            	if (columnScores[i] > highest) {
            		highest = columnScores[i];
            		columnToChoose = i;
            	}
            }
 
            System.out.println(columnToChoose);
  
        }
	}
	
	
	/**
	 * Determines the best score possible for each column by exploring the best spots to put blocks
	 * coming up in the queue.
	 * 
	 * @param grid
	 * @param colorA array of colorA in the queue
	 * @param colorB array of colorB in the queue
	 * @param max is the maximum number of color pairs in queue to try beyond the first.
	 * @return array containing best scores for each column, -1 score if column cannot be used.
	 */
	public static int[] getScoreByColumn(char[][] grid, int[] colorA, int[] colorB, int max) {
		
		int[] score = new int[C];
		
		for (int col = 0; col < C ; col++) {
			char[][] temp = new char[C][R];
			cloneDoubleArray(temp, grid);
			int rowPosition = dropPairIntoColumn(temp, col, colorA[0], colorB[0]);
			if (rowPosition == -1) {
				score[col] = -1;
			} else {
				int sd = scoreDrop(temp, col, rowPosition);
				int sbs = scoreBestScenario(temp, Arrays.copyOfRange(colorA, 1, colorA.length), Arrays.copyOfRange(colorB, 1, colorB.length), 0, max);
				score[col] = sd + sbs;
			}
//			score[col] = (rowPosition == -1) ? -1 : scoreDrop(temp, col, rowPosition) + scoreBestScenario(temp, colorA, colorB, 1, max);
		}
		
		return score;
	}
	
	
	/**
	 * Determines the highest score Scenario through brute force testing of every scenario of the blocks in queue.
	 * 
	 * @param grid
	 * @param colorA array of colorA in the queue
	 * @param colorB array of colorB in the queue
	 * @param colorIndex current index of queue pair.
	 * @param max is the maximum number of color pairs in queue to try beyond the first.
	 * @return the highest score Scenario given the best scenario scores of placing queue blocks.
	 */
	public static int scoreBestScenario(char[][] grid, int[] colorA, int[] colorB, int colorIndex, int max) {
		if (max == 0) return 0;
		int tempHigh = 0;
		for (int col = 0; col < C; col ++) {
			char[][] temp = new char[C][R];
			cloneDoubleArray(temp, grid);
			int rowPosition = dropPairIntoColumn(temp, col, colorA[colorIndex], colorB[colorIndex]);
			int tempScore = 0;
			if (rowPosition != -1) {
				tempScore = scoreDrop(temp, col, rowPosition);
				if (colorIndex + 1 < max) {
					tempScore += scoreBestScenario(temp, colorA, colorB, colorIndex + 1, max);
				}
			}
			if (tempScore > tempHigh) tempHigh = tempScore;
		}
		return tempHigh;
	}
	
	
	/** Attempts to drop the specified color pair into the specified column,
	 * updates the grid, and returns the int value of the row where the pair landed.
	 * If the drop was unsuccessful, -1 is returned.
	 * 
	 * @param grid grid to be updated with new color blocks.
	 * @param column column to drop the block
	 * @param colorA color of top block in pair to drop
	 * @param colorB color of bottom block in pair to drop
	 * @return the row index in the grid where colorA was placed.
	 */
	public static int dropPairIntoColumn(char[][] grid, int column, int colorA, int colorB) {
		// Place ColorB in first spot from bottom not filled, and ColorA on top of it.
		for (int row = R - 1; row > 1; row--) {
			if (grid[column][row] == '.') {
				grid[column][row] = Character.forDigit(colorB,  10);
				grid[column][row - 1] = Character.forDigit(colorA, 10);
				return row - 1;
			}
		}
		return -1;
		
	}
	
	
	/**
	 * Determines groups and chains. Removes the groups, chains, and adjacent skulls from the grid.
	 * Returns the count of blocks removed.
	 * @param grid
	 * @param column column index of the drop.
	 * @param row row index of colorA block drop.
	 * @return combined score of groups, chains, and adjacent skulls.
	 */
	public static int scoreDrop(char[][] grid, int column, int row) {
		int initialScore = findGroupScore(grid, column, row);
		int chainScore = 0;
		if (initialScore > 0) chainScore = findChainScore(grid);
		
		return initialScore + chainScore;
	}

	
	/**
	 * Determines the number of blocks removed by subsequent chain possibilities.
	 * Discovers possible chains and removes the appropriate blocks from the grid.
	 * 
	 * @param grid grid that may contain 'G' and 'S' symbols which are used as instructions as to which blocks to remove from the original grid.
	 * @return the number of blocks removed by subsequent chains.
	 */
	public static int findChainScore(char[][] grid) {
		
		boolean proceed = true;
		BlocksAndSkulls chainScore = new BlocksAndSkulls();
		
		while (proceed) {
			
			// These are columns where blocks shifted from deleting blocks.
			List<Integer> columnsToCheck = deleteBlocksFromGrid(grid);
			
			proceed = false;
			
			// Check for new groups in each changed column
			for (int x = 0; x < columnsToCheck.size(); x++) {
				int col = columnsToCheck.get(x);
				for (int row = R - 1; row >= 0; row--) {
					if (grid[col][row] == '.') break; // As soon as hitting a blank, no need to go higher.
					
					char[][] temp = new char[C][R];
					cloneDoubleArray(temp, grid);
					
					BlocksAndSkulls tempCount = new BlocksAndSkulls();
					findGroup(temp, col, row, temp[col][row], tempCount);
					
					if (tempCount.getBlocks() >= 4) {
						cloneDoubleArray(grid, temp);
						proceed = true;
						chainScore.addCounter(tempCount);
					}
				}	
			}
		}
		
		return chainScore.getScore();
		
	}
	
	
	/**
	 * Determines all groups of 4 or more that are formed as a result of dropping ColorA and ColorB into this column.
	 * The row in the argument refers to ColorA.
	 * 
	 * @param grid the Grid map is updated to include proper 'G' and 'S' marks for the blocks to be removed.
	 * @param column the column location of colorA
	 * @param row the row location of colorA
	 * @param bsCount BlocksAndSkulls counter object
	 * @return score the count of all blocks to be removed because of being a group of 4 like colors, or being an adjacent skull to the group.
	 */
	public static int findGroupScore(char[][] grid, int column, int row) {
		
		BlocksAndSkulls bsCountA = new BlocksAndSkulls();
		BlocksAndSkulls bsCountB = new BlocksAndSkulls();
		char[][] copyA = new char[C][R];
		cloneDoubleArray(copyA, grid);
		
		findGroup(copyA, column, row, grid[column][row], bsCountA);
		
		// If the group originating from colorA was less than 4, reset everything and check next block down.
		if (bsCountA.getBlocks() < 4) {
			bsCountA.reset();
			cloneDoubleArray(copyA, grid);
		}
		
		// Proceed here only if colorB is unique from colorA
		if (grid[column][row + 1] != grid[column][row]) {
			
			// Work with a copy in case I need to erase marks made to find a group, but the group wasn't big enough to keep.
			char[][] copyB = new char[C][R];
			cloneDoubleArray(copyB, copyA);
			
			findGroup(copyB, column, row + 1, grid[column][row + 1], bsCountB);
			
			// Disregard results if the group originating from colorB was less than 4.
			if (bsCountB.getBlocks() >= 4) {
				cloneDoubleArray(grid, copyB);
			} else {
				bsCountB.reset();
				cloneDoubleArray(grid, copyA);	
			} 
			
		} else {
			cloneDoubleArray(grid, copyA);
		}
		
		bsCountA.addCounter(bsCountB);
		
		return (bsCountA.getScore());
		
		
		
	}
	
	/** Update Array with contents from another
	 * 
	 * @param arrayToFill	Array to fill
	 * @param arrayToCopy	Array to copy
	 * @return false if no change
	 */
	public static boolean cloneDoubleArray(char[][] arrayToFill, char[][] arrayToCopy) {
		if (arrayToFill == null || arrayToCopy == null      ||
		   (arrayToFill[0].length != arrayToCopy[0].length) ||
		   (arrayToFill.length != arrayToFill.length)) return false;

		for (int i = 0; i < arrayToCopy.length; i++) {
			for (int j = 0; j < arrayToCopy[0].length; j++) {
				arrayToFill[i][j] = arrayToCopy[i][j];
			}
		}
		return true;
	}
	
	
	/**
	 * Updates the original grid by following instructions as listed.
	 * Any character marked as 'G' for group or 'S' for skull in the instructions will be removed in the original
	 *  
	 * @param grid containing 'G' and 'S' blocks which indicate group and skulls to remove
	 * @return a list of columns where blocks were shifted and need to be rechecked for possible new chains.  The List could be empty if no columns shifted.
	 */
	public static List<Integer> deleteBlocksFromGrid(char[][] grid) {
		
		char[][] instructions = new char[C][R];
		cloneDoubleArray(instructions, grid);
		
		List<Integer> columnsToCheck = new ArrayList<>();
		
		for (int x = 0; x < C; x++) {
			
			// Add non-deleted color blocks to new column temp constructor list.
			List<Character> newCol = new ArrayList<Character>();

			int blocksDeletedInColumn = 0;
			
			// Start at the bottom of each column and work up
			for (int y = R - 1; y >= 0; y--) {
				
				if (instructions[x][y] == '.') break;
				if (instructions[x][y] != 'G' && instructions[x][y] != 'S') {
					newCol.add(instructions[x][y]);
				} else {
					blocksDeletedInColumn++;
				}
			}
						
			// Assemble new column
			int y = R - 1;
			for (char c: newCol) {
				grid[x][y--] = c;
			}
			for (int i = y ; i >= 0; i--) {
				grid[x][i] = '.';
			}
			
			// Track which columns need to be rechecked for new groups.
			if (blocksDeletedInColumn > 0) columnsToCheck.add(x);
		}
		
		return columnsToCheck;
		
	}
	
	
	/**
	 * Populates the groupRow and groupCol argument lists with the coordinates of group members and adjacent skulls.
	 * Updates the grid with 'G' for the colored group found (e.g. There could be only one if the there is no other match to the initial block)
	 * Updates the grid with 'S' for any skull adjacent to the colored group (regardless if there are 4 blocks in the colored group or not)
	 * The purpose of this method is to give information about the group, such as size, group members, and adjacent skulls.
	 * No further analysis or action is taken here.
	 * 
	 * @param grid grid is updated with 'G' for groups and 'S' for adjacent skulls.
	 * @param col the col number of block to consider
	 * @param row the row number of block to consider (0 is top)
	 * @param groupColor
	 * @param count is a BlocksAndSkulls count holder object.
	 */
	public static void findGroup(char[][] grid, int x, int y, char groupColor, BlocksAndSkulls count) {
	
		if (groupColor == '.' || groupColor == '0'  || groupColor == 'G' || groupColor == 'S') return; // No group possible starting with blank or skull block.
		
		if (grid[x][y] == groupColor) {
			grid[x][y] = 'G';
			count.addBlocks(1);
			
			// Look around for more like colors or adjacent skulls.
			if (x < C - 1) findGroup(grid, x + 1, y, groupColor, count);
			if (x > 0)     findGroup(grid, x - 1, y, groupColor, count);
			if (y < R - 1) findGroup(grid, x, y + 1, groupColor, count);
			if (y > 0)     findGroup(grid, x, y - 1, groupColor, count);
		}
		
		if (grid[x][y] == '0') { // If a skull the search stops here.
			grid[x][y] = 'S';
			count.addSkulls(1);
		}
	}
		
	/** Tracks count of Blocks and Skulls.
	 * Separate tracking of blocks and skulls is needed in order to only count skulls if there is enough color blocks for a group.*/
	public static class BlocksAndSkulls {
		
		int blocks;
		int skulls;
		
		public BlocksAndSkulls() {
			this.blocks = 0;
			this.skulls = 0;
		}
		
		public int getBlocks() {
			return blocks;
		}
		
		public int getSkulls() {
			return skulls;
		}
		
		public void addBlocks(int blocks) {
			this.blocks += blocks;
		}
		
		public void addSkulls(int skulls) {
			this.skulls += skulls;
		}
		
		public void reset() {
			this.blocks = 0;
			this.skulls = 0;
		}
		
		public boolean addCounter(BlocksAndSkulls counter) {
			if (counter == null) return false;
			
			this.blocks += counter.getBlocks();
			this.skulls += counter.getSkulls();
			return true;
		}
		
		public int getScore() {
			return this.blocks + this.skulls;
		}
		
	}

}

