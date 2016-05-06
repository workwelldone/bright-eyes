package contests;

import java.util.HashSet;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		Set<Integer> set1 = new HashSet<>();
		set1.add(45);
		
		Set<Integer> set2 = new HashSet<>();
		set2.add(76859);
		
		System.out.println(set1);
		System.out.println(set2);
		
		set1 = set2;
		System.out.println(set1);
		System.out.println(set2);

		changeSet(set1);
		System.out.println(set1);
		System.out.println(set2);
		

		
	}

	static void changeSet(Set s) {
		Set<Integer> set3 = new HashSet<>();
		set3.add(-999999);
		s = set3;
	}
}
