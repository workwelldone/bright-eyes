package hard_challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

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
	
	/** Tests scan for control Room */
	@Test
	public void testScanForControlRoom() {
		// Set up
		String[] gridLines = new String[]{"#######",
										  "#   C #",
										  "# ### #",
										  "# ### #",
										  "#     #",
										  "#######"};
		char[][] grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}
		Node control = new Node(1, 4);
		//Test
		assertEquals(control, TheLabyrinth.scanForControlRoom(grid, 1, 2));
		assertEquals(null, TheLabyrinth.scanForControlRoom(grid, 5, 2));
		assertEquals(control, TheLabyrinth.scanForControlRoom(grid, 2, 6));
		assertEquals(control, TheLabyrinth.scanForControlRoom(grid, 0, 5));
		assertEquals(control, TheLabyrinth.scanForControlRoom(grid, 1, 4));
		assertEquals(control, TheLabyrinth.scanForControlRoom(grid, 3, 4));
		assertEquals(null, TheLabyrinth.scanForControlRoom(grid, 4, 4));		
	}
	
	/** Tests the Recursive Labyrinth Search Algorithm
	 * This returns a list of nodes contained in the shortest pathway possible from start to target.
	 * If the list is null, it means no pathway is currently known.
	 */
	@Test
	public void testRecursiveLabyrinthSearch() {
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
		
		//Test
		Node start = new Node(4, 1);
		Node end = new Node(3, 5);
		TheLabyrinth.RecursiveLabyrinthSearch search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertEquals(6, search.getShortestPathway().size());
		
		start = new Node(1, 1);
		end = new Node(1, 5);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertEquals(5, search.getShortestPathway().size());

		start = new Node(1, 1);
		end = new Node(4, 5);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertEquals(8, search.getShortestPathway().size());
		
		start = new Node(1, 1);
		end = new Node(5, 5);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertTrue(search.getShortestPathway() == null);

		start = new Node(1, 1);
		end = new Node(3, 3);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertTrue(search.getShortestPathway() == null);
		
		// Test unknown character
		gridLines = new String[]{ "#######",
								  "#     #",
								  "#?###?#",
								  "# ### #",
								  "#     #",
								  "#######"};
		grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}	
		
		start = new Node(1, 1);
		end = new Node(3, 1);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertTrue(search.getShortestPathway() == null);
		
		// Test unknown character one way, so it must go around.
		gridLines = new String[]{ "#######",
								  "#     #",
								  "#?### #",
								  "# ### #",
								  "#     #",
								  "#######"};
		grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}	
		
		start = new Node(1, 1);
		end = new Node(3, 1);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertEquals(13, search.getShortestPathway().size());

		// Test a larger set.
		gridLines = new String[]{   "###############################################",
									"#                                             #",
									"#??????????????????????????????????????????   #",
									"#??????????????????????????????????????????   #",
									"#??????????????????????????????????????????  C#",
									"###############################################"};
		grid = new char[gridLines.length][gridLines[0].length()];
		for (int i = 0; i < gridLines.length; i++) {
			grid[i] = gridLines[i].toCharArray();
		}	
		
		start = new Node(1, 1);
		end = new Node(4, 45);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		assertEquals(48, search.getShortestPathway().size());
		
		start = new Node(4, 45);
		end = new Node(1, 1);
		search = new TheLabyrinth.RecursiveLabyrinthSearch(grid, start, end);
		System.out.println(search.getShortestPathway());
		System.out.println(search.successfulPathways.size());
		assertEquals(48, search.getShortestPathway().size());
	}
}
