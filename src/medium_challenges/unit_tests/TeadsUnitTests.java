package medium_challenges.unit_tests;

import static medium_challenges.TeadsSponsoredContest.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TeadsUnitTests {

	/** Tests the recursive method to traverse the contact tree and correctly populate the count List, beginning at the top
	 * 
	 */
	@Test
	public void testFindSteps() {
		Map<Integer, List<Integer>> relations = new HashMap<>();
		relations.put(0, Arrays.asList(1));
		relations.put(1, Arrays.asList(2, 0));
		relations.put(2, Arrays.asList(1, 3, 4));
		relations.put(3, Arrays.asList(2));
		relations.put(4, Arrays.asList(2));
		
		int person = 0;
		int linkedFrom = -1;
		List<Integer> countList = new ArrayList<>();
		countList.add(0);
		int countIndex = 0;
		
		findSteps(relations, person, linkedFrom, countList, countIndex);
		assertThat("Incorrect branch count list while starting with the first person.", countList.get(0), is(3));
		assertThat("Incorrect branch count list while starting with the first person.", countList.get(1), is(3));
		
	}

	/** Tests the recursive method to traverse the contact tree and correctly populate the count List, beginning in the middle
	 * 
	 */
	@Test
	public void testFindStepsStartingInMiddle() {
		Map<Integer, List<Integer>> relations = new HashMap<>();
		relations.put(0, Arrays.asList(1));
		relations.put(1, Arrays.asList(2, 0));
		relations.put(2, Arrays.asList(1, 3, 4));
		relations.put(3, Arrays.asList(2));
		relations.put(4, Arrays.asList(2));
		
		int person = 2;
		int linkedFrom = -1;
		List<Integer> countList = new ArrayList<>();
		countList.add(0);
		int countIndex = 0;
		
		
		findSteps(relations, person, linkedFrom, countList, countIndex);
		assertThat("This test starts in the middle, and did not correctly find the highest branch count", Collections.max(countList), is(2));
		assertThat("This test starts in the middle, and did not correctly find the lowest branch count", Collections.min(countList), is(1));
		
	}
	
	
	/** Tests the recursive method to traverse the contact tree and correctly populate the count List, beginning in the middle
	 * 
	 */
	@Test
	public void testFindStepsStartingAtEnd() {
		Map<Integer, List<Integer>> relations = new HashMap<>();
		relations.put(0, Arrays.asList(1));
		relations.put(1, Arrays.asList(2, 0));
		relations.put(2, Arrays.asList(1, 3, 4));
		relations.put(3, Arrays.asList(2));
		relations.put(4, Arrays.asList(2));
		
		int person = 4;
		int linkedFrom = -1;
		List<Integer> countList = new ArrayList<>();
		countList.add(0);
		int countIndex = 0;
		
		
		findSteps(relations, person, linkedFrom, countList, countIndex);
		assertThat("This test starts at the end, and did not correctly find the highest branch count", Collections.max(countList), is(3));
		assertThat("This test starts at the end, and did not correctly find the lowest branch count", Collections.min(countList), is(2));
		
	}
	
	/** Tests a longer list starting at the top number
	 * 
	 */
	@Test
	public void testLongerListStartingAtTop() {
		Map<Integer, List<Integer>> relations = new HashMap<>();
		relations.put(0, Arrays.asList(1));
		relations.put(1, Arrays.asList(0, 4, 2));
		relations.put(2, Arrays.asList(1, 3));
		relations.put(3, Arrays.asList(2));
		relations.put(4, Arrays.asList(1, 5, 6));
		relations.put(5, Arrays.asList(4));
		relations.put(6, Arrays.asList(4));
		
		int person = 0;
		int linkedFrom = -1;
		List<Integer> countList = new ArrayList<>();
		countList.add(0);
		int countIndex = 0;
		
		
		findSteps(relations, person, linkedFrom, countList, countIndex);
		assertThat("This test starts in the middle, and did not correctly find the highest branch count", Collections.max(countList), is(3));
		assertThat("This test starts in the middle, and did not correctly find the lowest branch count", Collections.min(countList), is(3));
		
	}
	
	/** Tests a longer list starting in the middle
	 * 
	 */
	@Test
	public void testLongerListStartingInMiddle() {
		Map<Integer, List<Integer>> relations = new HashMap<>();
		relations.put(0, Arrays.asList(1));
		relations.put(1, Arrays.asList(0, 4, 2));
		relations.put(2, Arrays.asList(1, 3));
		relations.put(3, Arrays.asList(2));
		relations.put(4, Arrays.asList(1, 5, 6));
		relations.put(5, Arrays.asList(4));
		relations.put(6, Arrays.asList(4));
		
		int person = 1;
		int linkedFrom = -1;
		List<Integer> countList = new ArrayList<>();
		countList.add(0);
		int countIndex = 0;
		
		System.out.println("Starts here:");
		findSteps(relations, person, linkedFrom, countList, countIndex);
		assertThat("This test starts at the end, and did not correctly find the highest branch count", Collections.max(countList), is(2));
		assertThat("This test starts at the end, and did not correctly find the lowest branch count", Collections.min(countList), is(1));
		
	}
	
}
