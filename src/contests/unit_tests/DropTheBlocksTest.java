package contests.unit_tests;

import static contests.DropTheBlocks.deleteBlocksFromGrid;
import static contests.DropTheBlocks.dropPairIntoColumn;
import static contests.DropTheBlocks.findGroup;
import static contests.DropTheBlocks.findChainScore;
import static contests.DropTheBlocks.findGroupScore;
import static contests.DropTheBlocks.scoreDrop;
import static contests.DropTheBlocks.scoreBestScenario;
import static contests.DropTheBlocks.getScoreByColumn;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import contests.DropTheBlocks;

public class DropTheBlocksTest {

	//------------------------------------------------------------------------------------------------------
	/** Tests the ability to drop the pair of colors into the correct position a the column. */
	@Test
	public void dropBlocksCorrectly() {
		int W = 6;
		
		// Setup grid
		char[][] grid = {
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '1'},
							{'.', '0', '1', '2', '3', '0', '3', '1', '2', '3', '1', '2'},
							{'1', '0', '0', '2', '4', '4', '3', '0', '1', '2', '3', '1'},
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'},
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '2'},
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
		};
		
		// Expected Grid
		int colorA = 1;
		int colorB = 2;
		
		char[][] expected = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '1', '2', '0', '1'},
								{'.', '0', '1', '2', '3', '0', '3', '1', '2', '3', '1', '2'},
								{'1', '0', '0', '2', '4', '4', '3', '0', '1', '2', '3', '1'},
								{'.', '.', '.', '.', '.', '1', '2', '3', '3', '0', '2', '3'},
								{'.', '.', '.', '.', '.', '.', '.', '.', '1', '2', '0', '2'},
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '2'}
		};
		
		// Catch Results in Array, contains the row of where colorB (The lowest block) landed.
		int[] whereLandedColorA = new int[6];
		
		// Drop blocks into each column
		for (int i = 0; i < W; i++) {
			whereLandedColorA[i] = dropPairIntoColumn(grid, i, colorA, colorB);
		}
		
		// Assert the results
		for (int i = 0; i < W; i++) {
			assertThat("Column comparison failed", grid[i], is(expected[i]));
		}

		assertThat("Row of Drop incorrect", whereLandedColorA[0], is(8));
		assertThat("Row of Drop incorrect", whereLandedColorA[1], is(-1));
		assertThat("Row of Drop incorrect", whereLandedColorA[2], is(-1));
		assertThat("Row of Drop incorrect", whereLandedColorA[3], is(5));
		assertThat("Row of Drop incorrect", whereLandedColorA[4], is(8));
		assertThat("Row of Drop incorrect", whereLandedColorA[5], is(10));
		
	}
	
	
	//------------------------------------------------------------------------------------------------------
	// Test group and skull removal from grid
	@Test
	public void testDeleteBlocks() {

		// Setup grid
		char[][] grid         = {//0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', 'G', 'S', '0', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', 'G', 'G', 'G', 'S', '2', '3'}, // char[2]
								{'2', '2', '0', '2', '4', '4', 'G', 'S', 'G', 'G', 'G', '1'}, // char[3]
								{'.', '0', '1', '2', '3', 'S', 'G', '1', '2', 'G', '1', '2'}, // char[4]
								{'.', '2', '2', '1', '2', '0', '1', '0', '1', 'G', 'S', '1'}  // char[5]
			};
		
		char[][] expected = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '3'}, // char[2]
								{'.', '.', '.', '.', '.', '2', '2', '0', '2', '4', '4', '1'}, // char[3]
								{'.', '.', '.', '.', '0', '1', '2', '3', '1', '2', '1', '2'}, // char[4]
								{'.', '.', '.', '2', '2', '1', '2', '0', '1', '0', '1', '1'}  // char[5]
		};
		
		List<Integer> columnsToCheck = new ArrayList<>();
		
		// Delete blocks from original
		columnsToCheck = deleteBlocksFromGrid(grid);
		
		// Assert the results
		assertThat("Grid did not update delete correctly.", grid, is(expected));
		assertThat("ColumnsToCheck was not properly filled.", columnsToCheck.get(0), is(1));
		assertThat("ColumnsToCheck was not properly filled.", columnsToCheck.size(), is(5));
		assertThat("ColumnsToCheck was not properly filled.", columnsToCheck.get(4), is(5));
		assertThat("ColumnsToCheck was not properly filled.", columnsToCheck.get(3), is(4));
	}
	
	
	//------------------------------------------------------------------------------------------------------
	/** Tests detection of a group and adjacent skulls.*/
	@Test
	public void testfindGroup() {
		
		// Test 1
			// Setup grid
			char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '1'}, // grid[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '1', '2'}, // grid[1]
								{'.', '.', '.', '.', '.', '.', '.', '0', '3', '3', '3', '1'}, // grid[2]
								{'.', '.', '.', '.', '.', '.', '3', '3', '3', '0', '2', '3'}, // grid[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '0', '2'}, // grid[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}  // grid[5]
							};
			
			// Set up scenario
			int col = 3;
			int row = 6;
			DropTheBlocks.BlocksAndSkulls bsCount = new DropTheBlocks.BlocksAndSkulls();
			
			// Execute
			findGroup(grid, col, row, grid[col][row], bsCount);
			
			// Assert
			assertThat("Group count of actual colored blocks is incorrect", bsCount.getBlocks(), is(9));
			assertThat("Count of adjacent skull blocks is incorrect", bsCount.getSkulls(), is(4));

		// Test 2 -- Insure skull or blank produces no group
	
			// Set up scenario to test sending a skull
			col = 4;
			row = 10;
			bsCount.reset();
			
			// Execute
			findGroup(grid, col, row, grid[col][row], bsCount);
			
			// Assert
			String message = "Blank or Skull as initial starting point should produce no group.";
			assertThat(message, bsCount.getBlocks(), is(0));
			assertThat(message, bsCount.getSkulls(), is(0));
		
		// Test 3 -- Test a small group
				
			// Set up scenario to test sending a skull
			col = 5;
			row = 10;
			bsCount.reset();
			
			// Execute
			findGroup(grid, col, row, grid[col][row], bsCount);
			
			// Assert
			assertThat("Group count of actual colored blocks is incorrect", bsCount.getBlocks(), is(2));
			assertThat("Count of adjacent skull blocks is incorrect", bsCount.getSkulls(), is(1));
	}

	//------------------------------------------------------------------------------------------------------

	// Test an overall basic grid to make sure chain score is correct.
	@Test
	public void testFindChainScore() {
	
		// Setup grid
		char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '.', 'G', 'S', '0', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', 'G', 'G', 'S', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', 'G', 'G', 'G', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', 'S', '1'}  // char[5]
		};
		
		// Find the score
		int score = findChainScore(grid);
		
		// Assert
		assertThat("Chain Score was incorrect.", score, is(0));
		
		
		// --------------------------------------
		// Setup grid to test a chain
		char[][] grid2 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', 'G'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1'}  // char[5]
		};
		
		// Find the score
		score = findChainScore(grid2);
		
		// Assert
		assertThat("Chain score was incorrect.", score, is(4));
		
		
		// --------------------------------------
		// Setup grid to test a chain
		char[][] grid3 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'S'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G', 'G'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', 'G'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1'}  // char[5]
		};
		
		// Find the score
		score = findChainScore(grid3);
		
		// Assert
		assertThat("Chain score was incorrect.", score, is(6));
		
		
		// --------------------------------------
		// Setup grid to test a double chain
		char[][] grid4 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'S'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '2', '2', '1', 'G', 'G'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'G'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'G'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'G'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1'}  // char[5]
		};
		
		// Find the score
		score = findChainScore(grid4);
		
		// Assert
		assertThat("Chain score was incorrect.", score, is(12));
		
		// --------------------------------------
		// Setup grid to test a double chain with skulls
		char[][] grid5 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '0', '2', '1', 'S'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '0', '2', '2', '1', 'G', 'G'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '.', '0', '2', '1', 'G'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'G'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '1', 'G'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '0', '1'}  // char[5]
		};
		
		// Find the score
		score = findChainScore(grid5);
		
		// Assert
		assertThat("Chain score was incorrect.", score, is(16));
	

	// --------------------------------------
	// Setup grid to test a triple chain with skulls
	char[][] grid6 = {//   0    1    2    3    4    5    6    7    8    9   10    11
						{'.', '.', '.', '.', '.', '.', '.', '.', '0', '2', '1', 'S'}, // char[0]
						{'.', '.', '.', '.', '.', '3', '0', '2', '2', '1', 'G', 'G'}, // char[1]
						{'.', '.', '.', '.', '.', '.', '.', '3', '0', '2', '1', 'G'}, // char[2]
						{'.', '.', '.', '.', '.', '.', '.', '.', '3', '2', '1', 'G'}, // char[3]
						{'.', '.', '.', '.', '.', '.', '.', '.', '3', '2', '1', 'G'}, // char[4]
						{'.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '0', '1'}  // char[5]
	};
	
	// Find the score
	score = findChainScore(grid6);
	
	// Assert
	assertThat("Chain score was incorrect.", score, is(21));
}
	
	//------------------------------------------------------------------------------------------------------
	
	// Test scoring the initial search for groups, without considering any chains.
	@Test
	public void testFindBaseGroupsAndCount() {
		
		// Setup grid basic test to count group blocks.
		char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '0', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '.', '.', '3', '3', '3', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '1'}  // char[5]
		};
		
		// Find the score
		int col = 2;
		int row = 7;
		int score = findGroupScore(grid, col, row);
		
		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(11));
		
		
		// --------------------------------------
		// Setup grid to test a group less than four.
		char[][] grid2 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '0', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '2', '2', '3', '1', '0', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '0', '1'}  // char[5]
		};
		
		// Find the score
		col = 3;
		row = 6;
		score = findGroupScore(grid2, col, row);

		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(0));
		
		
		// --------------------------------------
		// Setup grid to try another general scenario
				char[][] grid3 = {//   0    1    2    3    4    5    6    7    8    9   10    11
									{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '0'}, // char[0]
									{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '0', '2'}, // char[1]
									{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
									{'.', '.', '.', '.', '.', '.', '2', '2', '2', '0', '0', '1'}, // char[3]
									{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '1', '2'}, // char[4]
									{'.', '.', '.', '.', '.', '.', '.', '.', '2', '0', '0', '1'}  // char[5]
				};
				
		// Find the score
		col = 3;
		row = 6;
		score = findGroupScore(grid3, col, row);
		
		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(7));

		
		// --------------------------------------
		// Setup grid to try hitting a zero
		char[][] grid4 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '0'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '0', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '2', '2', '2', '0', '0', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '0', '0', '1'}  // char[5]
		};
		
		// Find the score
		col = 0;
		row = 10;
		score = findGroupScore(grid4, col, row);
		
		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(0));

		
		// --------------------------------------
		// Setup grid to Test having different colors at colorA, colorB
		char[][] grid5 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '4', '0', '0'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '3', '4', '4', '2', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '3', '3', '3', '4', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '2', '2', '2', '0', '0', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '.', '.', '2', '0', '0', '1'}  // char[5]
		};
		
		// Find the score
		col = 1;
		row = 7;
		score = findGroupScore(grid5, col, row);
		
		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(10));
	
		
		// --------------------------------------
		// Setup grid to Test another situation having different colors at colorA, colorB.  colorA doesn't have enough for a group, but colorB does.
		char[][] grid6 = {//   0    1    2    3    4    5    6    7    8    9   10    11
							{'.', '.', '.', '.', '.', '.', '.', '.', '.', '4', '0', '0'}, // char[0]
							{'.', '.', '.', '.', '.', '.', '.', '3', '4', '4', '2', '2'}, // char[1]
							{'.', '.', '.', '.', '.', '.', '3', '3', '3', '4', '2', '3'}, // char[2]
							{'.', '.', '.', '.', '.', '.', '2', '2', '2', '0', '0', '1'}, // char[3]
							{'.', '.', '.', '.', '.', '.', '.', '4', '2', '3', '1', '2'}, // char[4]
							{'.', '.', '.', '.', '.', '.', '4', '4', '2', '0', '0', '1'}  // char[5]
		};
		
		// Find the score
		col = 4;
		row = 7;
		score = findGroupScore(grid6, col, row);
		
		// Assert
		assertThat("The sum of the blocks to be removed, was incorrect.", score, is(7));
		assertThat("The proper 'G' has not been deposited on the map.", grid6[4][8], is('G'));
		assertThat("The proper 'S' has not been deposited on the mpa.", grid6[3][9], is('S'));
	
	}
	
	//------------------------------------------------------------------------------------------------------
	
		// Test the overall score that combines groups and chains.
		@Test
		public void testScoreDrop() {
			
			// Setup grid basic test to count group blocks and adjacent skulls with no chains.
			char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '0', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '3', '3', '3', '1'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '1', '2'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '0', '1'}  // char[5]
			};
			
			// Find the score
			int col = 2;
			int row = 7;
			int score = scoreDrop(grid, col, row);
			
			// Assert
			assertThat("The overal score of the sum of the chains and groups was incorrect.", score, is(11));
			
			
			// --------------------------------------
			// Setup grid to test a group that forms a chain.
			char[][] grid2 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '0', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '2', '2', '3', '1', '0', '1'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '1', '2'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '2', '3', '4', '1'}  // char[5]
			};
			
			// Find the score
			col = 1;
			row = 7;
			score = scoreDrop(grid2, col, row);

			// Assert
			assertThat("The sum of the blocks to be removed, was incorrect.", score, is(11));
			
			
			// --------------------------------------
			// Setup grid to test a group that forms a chain with skulls.
			char[][] grid3 = {//   0    1    2    3    4    5    6    7    8    9   10    11
					{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
					{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '0', '2'}, // char[1]
					{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '2', '3'}, // char[2]
					{'.', '.', '.', '.', '.', '.', '2', '2', '3', '1', '0', '1'}, // char[3]
					{'.', '.', '.', '.', '.', '.', '.', '0', '2', '3', '1', '2'}, // char[4]
					{'.', '.', '.', '.', '.', '.', '.', '0', '2', '0', '4', '1'}  // char[5]
			};
			
			// Find the score
			col = 1;
			row = 7;
			score = scoreDrop(grid3, col, row);
			
			// Assert
			assertThat("The sum of the blocks to be removed, was incorrect.", score, is(14));
			
			// --------------------------------------
			// Setup grid to test a group that forms a chain with skulls.
			char[][] grid4 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '3', '3', '0', '3', '1'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '3', '4', '2', '3', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '3', '3', '4', '4', '1', '2', '2'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '0', '4', '1', '1', '2'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '4', '0', '0', '0', '2'}  // char[5]
			};
			
			// Find the score
			col = 0;
			row = 10;
			score = scoreDrop(grid4, col, row);
			
			// Assert
			assertThat("The sum of the blocks to be removed, was incorrect.", score, is(23));
		}
		
		//------------------------------------------------------------------------------------------------------
		
		// Test scoreBestScenario
		@Test
		public void testScoreBestScenario() {

			int[] colorA = {1, 2, 1, 4, 4, 2, 1, 5};
			int[] colorB = {1, 2, 1, 4, 4, 2, 1, 5};
			
			// Setup grid basic test to score best scenario
			char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}  // char[5]
			};
			
			// Find the score
			int maxPairs = 1;
			
			int score = scoreBestScenario(grid, colorA, colorB, 0, maxPairs);
			
			// Assert
			assertThat("The best case scenario calculation was incorrect.", score, is(4));

			// --------------------------------------
			// Setup grid basic test to score best scenario with two iterations
			char[][] grid2 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}  // char[5]
			};
			
			// Find the score
			maxPairs = 2;
			
			score = scoreBestScenario(grid2, colorA, colorB, 0, maxPairs);
			
			// Assert
			assertThat("The best case scenario calculation was incorrect.", score, is(8));
			

			// --------------------------------------
			// Setup grid basic test to score best scenario with three iterations
			char[][] grid3 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}  // char[5]
			};
			
			// Find the score
			maxPairs = 3;
			
			score = scoreBestScenario(grid3, colorA, colorB, 0, maxPairs);
			
			// Assert
			assertThat("The best case scenario calculation was incorrect.", score, is(10));
			
			
			// --------------------------------------
			// Setup grid basic test no blocks on grid
			char[][] grid4 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}  // char[5]
			};
			
			// Find the score
			maxPairs = 3;
			
			score = scoreBestScenario(grid4, colorA, colorB, 0, maxPairs);
			
			// Assert
			assertThat("The best case scenario calculation was incorrect.", score, is(4));
		}		

		//------------------------------------------------------------------------------------------------------
		
		// Test getScoreByColumn
		@Test
		public void testGetScoreByColumn() {

			int[] colorA = {1, 2, 1, 4, 4, 2, 1, 5};
			int[] colorB = {1, 2, 1, 4, 4, 2, 1, 5};
			
			// Setup grid basic test the score determination by column with only first pair.
			char[][] grid = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}  // char[5]
			};
			
			// Find the score
			int maxAdditonalPairs = 0;
			
			int[] score = getScoreByColumn(grid, colorA, colorB, maxAdditonalPairs);
			
			// Assert
			assertThat("The score by column was incorrect.", score[0], is(0));
			assertThat("The score by column was incorrect.", score[1], is(0));
			assertThat("The score by column was incorrect.", score[2], is(4));
			assertThat("The score by column was incorrect.", score[3], is(4));
			assertThat("The score by column was incorrect.", score[4], is(0));
			assertThat("The score by column was incorrect.", score[5], is(0));
		

			// --------------------------------------
//			int[] colorA = {1, 2, 1, 4, 4, 2, 1, 5};
//			int[] colorB = {1, 2, 1, 4, 4, 2, 1, 5};
			
			// Setup grid basic test the score determination by column with only first pair.
			char[][] grid2 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '3'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '3'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '4', '4'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '4'}  // char[5]
			};
			
			// Find the score
			maxAdditonalPairs = 0;
			
			score = getScoreByColumn(grid2, colorA, colorB, maxAdditonalPairs);
			
			// Assert
			assertThat("The score by column was incorrect.", score[0], is(0));
			assertThat("The score by column was incorrect.", score[1], is(0));
			assertThat("The score by column was incorrect.", score[2], is(0));
			assertThat("The score by column was incorrect.", score[3], is(0));
			assertThat("The score by column was incorrect.", score[4], is(0));
			assertThat("The score by column was incorrect.", score[5], is(0));
			
			
			// --------------------------------------
//			int[] colorA = {1, 2, 1, 4, 4, 2, 1, 5};
//			int[] colorB = {1, 2, 1, 4, 4, 2, 1, 5};
			
			// Setup grid basic test the score determination by column with one additional pair.
			char[][] grid3 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '3', '3'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '3'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '4', '4'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '4'}  // char[5]
			};
			
			// Find the score
			maxAdditonalPairs = 1;
			
			score = getScoreByColumn(grid3, colorA, colorB, maxAdditonalPairs);
			
			// Assert
			assertThat("The score by column was incorrect.", score[0], is(4));
			assertThat("The score by column was incorrect.", score[1], is(4));
			assertThat("The score by column was incorrect.", score[2], is(4));
			assertThat("The score by column was incorrect.", score[3], is(4));
			assertThat("The score by column was incorrect.", score[4], is(4));
			assertThat("The score by column was incorrect.", score[5], is(4));
			
			// --------------------------------------
//			int[] colorA = {1, 2, 1, 4, 4, 2, 1, 5};
//			int[] colorB = {1, 2, 1, 4, 4, 2, 1, 5};
			
			// Setup grid basic test the score determination by column with one additional pair.
			char[][] grid4 = {//   0    1    2    3    4    5    6    7    8    9   10    11
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1', '1'}, // char[0]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '0'}, // char[1]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '1', '1'}, // char[2]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '3'}, // char[3]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '2', '2'}, // char[4]
								{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '0', '2'}  // char[5]
			};
			
			// Find the score
			maxAdditonalPairs = 1;
			
			score = getScoreByColumn(grid4, colorA, colorB, maxAdditonalPairs);
			
			// Assert
			assertThat("The score by column was incorrect.", score[0], is(13));
			assertThat("The score by column was incorrect.", score[1], is(13));
			assertThat("The score by column was incorrect.", score[2], is(12));
			assertThat("The score by column was incorrect.", score[3], is(12));
			assertThat("The score by column was incorrect.", score[4], is(0));
			assertThat("The score by column was incorrect.", score[5], is(7));
		}

}
