package hard_challenges;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import hard_challenges.TheLabyrinth.Code;
import hard_challenges.TheLabyrinth.Direction;
import hard_challenges.TheLabyrinth.Node;

public class LabyrinthTests {

		
	/** Tests implementing the left hand rule in order to correctly hug the left wall while exploring a labyrinth */
	@Test
	public void testASimpleLabyrinth() {
		// Set up
		String[] gridLines = new String[]{"#######",
										  "#     #",
										  "# ### #",
										  "# ### #",
										  "#     #",
										  "#######"};
		char[][] grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}
		
		// Test
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1, 1), Direction.UP));
		assertEquals(Direction.DOWN,  TheLabyrinth.hugLeftWall(grid, new Node(1, 1), Direction.LEFT));
		assertEquals(Direction.DOWN,  TheLabyrinth.hugLeftWall(grid, new Node(2, 1), Direction.DOWN));
		assertEquals(Direction.UP,    TheLabyrinth.hugLeftWall(grid, new Node(2, 1), Direction.UP));
		assertEquals(Direction.UP,    TheLabyrinth.hugLeftWall(grid, new Node(4, 1), Direction.LEFT));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(4, 1), Direction.DOWN));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(4, 3), Direction.RIGHT));
		assertEquals(Direction.LEFT,  TheLabyrinth.hugLeftWall(grid, new Node(4, 3), Direction.LEFT));
	}

	/** Tests implementing the left hand rule in order to correctly hug the left wall while exploring a labyrinth */
	@Test
	public void testALessSimpleLabyrinth() {
		// Set up
		String[] gridLines = new String[]  {"##########",
											"# ####  ##",
											"# ###  ###",
											"# ###  ###",
											"#     ####",
											"##########"};
		char[][] grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}
		
		// Test
		assertEquals(Direction.DOWN, TheLabyrinth.hugLeftWall(grid, new Node(1, 1), Direction.UP));
		assertEquals(Direction.DOWN, TheLabyrinth.hugLeftWall(grid, new Node(1, 1), Direction.DOWN));
		assertEquals(Direction.UP,   TheLabyrinth.hugLeftWall(grid, new Node(4, 1), Direction.LEFT));
		assertEquals(Direction.RIGHT,TheLabyrinth.hugLeftWall(grid, new Node(4, 1), Direction.DOWN));
		assertEquals(Direction.RIGHT,TheLabyrinth.hugLeftWall(grid, new Node(4, 4), Direction.RIGHT));
		assertEquals(Direction.UP,   TheLabyrinth.hugLeftWall(grid, new Node(4, 5), Direction.RIGHT));
		assertEquals(Direction.LEFT, TheLabyrinth.hugLeftWall(grid, new Node(4, 5), Direction.DOWN));
		assertEquals(Direction.UP,TheLabyrinth.hugLeftWall(grid, new Node(3, 5), Direction.RIGHT));
	}
	
	/** Tests implementing the left hand rule in order to correctly hug the left wall while exploring a labyrinth */
	@Test
	public void testASingleLine() {
		// Set up
		String[] gridLines = new String[]  {"##########",
											"#T      C#",
											"##########"};
		char[][] grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}
		
		// Test
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1,1), Direction.UP));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1,2), Direction.RIGHT));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1,3), Direction.RIGHT));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1,4), Direction.RIGHT));
		assertEquals(Direction.RIGHT, TheLabyrinth.hugLeftWall(grid, new Node(1,7), Direction.RIGHT));
		assertEquals(Direction.LEFT, TheLabyrinth.hugLeftWall(grid, new Node(1,8), Direction.RIGHT));
	}
	
	/** Tests the findAdjacent method.  This method is supposed to find any argument character if adjacent,
	 * and return the proper direction to go to get to it.
	 */
	@Test
	public void testFindAdjacentMethod() {
		// Set up
				String[] gridLines = new String[]  {"##########",
													"#T      C#",
													"##########"};
				char[][] grid = new char[gridLines.length][gridLines[0].length()];
				for (int i = 0; i < gridLines.length; i++) {
					grid[i] = gridLines[i].toCharArray();
				}
				
				// Test
				assertEquals(Direction.LEFT, TheLabyrinth.findAdjacent(grid, new Node(1,2), Code.START));
				assertEquals(Direction.RIGHT, TheLabyrinth.findAdjacent(grid, new Node(1,7), Code.CONTROL));
				
	}
}
