package contests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DropTheBlocks {
	
	public static final int W = 6;
	public static final int H = 12;
	public static final Set<Character> validBlocks;

	static {
		validBlocks = new HashSet<>();
		validBlocks.add('0');
		validBlocks.add('1');
		validBlocks.add('2');
		validBlocks.add('3');
		validBlocks.add('4');
		validBlocks.add('5');
	}
	
	public static void main (String...args) {
	}
	
	/** Attempts to drop the specified color pair into the specified column,
	 * updates the grid, and returns the int value of the row where the pair landed.
	 * If the drop was unsuccessful, -1 is returned.
	 * 
	 * @param grid grid to be updated with new color blocks.
	 * @param column column to drop the block
	 * @param colorA color of top block in pair to drop
	 * @param colorB color of bottom block in pair to drop
	 * @return the column index in the grid where colorB was placed.
	 */
	public static int dropPairIntoColumn(char[][] grid, int column, int colorA, int colorB) {
		// Place ColorB in first spot from bottom not filled, and ColorA on top of it.
		for (int i = H - 1; i > 1; i--) {
			if (grid[column][i] == '.') {
				grid[column][i] = Character.forDigit(colorB,  10);
				grid[column][i - 1] = Character.forDigit(colorA, 10);
				return i;
			}
		}
		return -1;
		
	}
	
	
	/**
	 * Determines groups and chains. Removes the groups, chains, and adjacent skulls from the grid.
	 * Returns the count of blocks removed.
	 * @param grid
	 * @param column location of the drop.
	 * @param row location of the colorA drop.
	 * @return
	 */
	public static int scoreDrop(char[][] grid, int column, int row) {
		List<Integer> groupRow = new ArrayList<>();
		List<Integer> groupCol = new ArrayList<>();
		char[][] copy = grid.clone();
		
		// Start search with colorA
		int groupSize = findGroup(copy, groupRow, groupCol, column, row, grid[column][row]);
		
		// Search colorB only if unique from colorA
		if (grid[column][row + 1] != 'G') {
			if (groupSize < 4) {
				copy = grid.clone(); // start with original grid if colorA didn't yield a group of at least 4.
				groupRow.clear();
				groupCol.clear();
				groupSize = findGroup(copy, groupRow, groupCol, column, row + 1, grid[column][row + 1]);
			} else {
				groupSize += findGroup(copy, groupRow, groupCol, column, row + 1, grid[column][row + 1]);
			}
		}
		if (groupSize < 4) return 0;  // No group found of 4 or more blocks.  Original grid is unchanged.
		deleteBlocksFromOriginal(grid, copy);
		
		/*
		 * Finish checking for chain and repeat until no change.
		 */
		
		return groupRow.size();
		}
		
	/**
	 * Updates the original grid by following instructions as listed.
	 * Any character marked as 'G' for group or 'S' for skull in the instructions will be removed in the original
	 *  
	 * @param original original grid to update
	 * @param instructions grid containing 'G' and 'S' blocks which indicate group and skulls to remove
	 */
	public static void deleteBlocksFromOriginal(char[][] original, char[][] instructions) {
		
		for (int x = 0; x < W; x++) {
			
			// Add only valid color blocks to new column temp list.
			List<Character> newCol = new ArrayList<Character>();

			for (int y = H - 1; y >= 0; y--) {
				if (instructions[x][y] == '.') break;
				if (validBlocks.contains(instructions[x][y])) {
					newCol.add(instructions[x][y]);
				}
			}
			
			// Assemble new column
			int y = H - 1;
			for (char c: newCol) {
				original[x][y--] = c;
			}
			for (int i = y ; i >= 0; i--) {
				original[x][i] = '.';
			}

		}
		
	}
	
	
	/**
	 * Populates the groupRow and groupCol argument lists with the coordinates of group members and adjacent skulls.
	 * Updates the grid with 'G' for the colored group found (e.g. There could be only one if the there is no other match to the initial block)
	 * Updates the grid with 'S' for any skull adjacent to the colored group (regardless if there are 4 blocks in the colored group or not)
	 * The purpose of this method is to give information about the group, such as size, group members, and adjacent skulls.
	 * No further analysis or action is taken here.
	 * 
	 * 
	 * @param grid grid is updated with 'G' for groups and 'S' for adjacent skulls.
	 * @param groupRow A list this method populates with Row coordinates of group members, and adjacent skulls.
	 * @param groupCol A list this method populates with Col coordinates of group members, and adjacent skulls.
	 * @param col the col number of block to consider
	 * @param row the row number of block to consider (0 is top)
	 * @param groupColor
	 * @return the number of colored blocks in this group
	 */
	public static int findGroup(char[][] grid, List<Integer> groupRow, List<Integer> groupCol, int x, int y, char groupColor) {
		
		if (groupColor == '.' || groupColor == '0') return 0; // No group possible starting with blank or skull block.
		
		int count = 0;
		
		if (grid[x][y] == groupColor) {
			groupCol.add(x);
			groupRow.add(y);
			grid[x][y] = 'G';
			count++;
			
			// Look around for more like colors or adjacent skulls.
			if (x < W - 1) count += findGroup(grid, groupRow, groupCol, x + 1, y, groupColor);
			if (x > 0)     count += findGroup(grid, groupRow, groupCol, x - 1, y, groupColor);
			if (y < H - 1) count += findGroup(grid, groupRow, groupCol, x, y + 1, groupColor);
			if (y > 0)     count += findGroup(grid, groupRow, groupCol, x, y - 1, groupColor);
		}
		
		if (grid[x][y] == '0') { // If a skull the search stops here.
			groupCol.add(x);
			groupRow.add(y);
			grid[x][y] = 'S';
		}
		return count;
	}
		
	
}
