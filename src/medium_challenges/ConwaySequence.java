package medium_challenges;

import java.util.*;
import java.util.stream.*;

class ConwaySequence {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt();
        int L = in.nextInt();

        List<Integer> sequence = new ArrayList<>();
        sequence.add(R);
        
        for (int i = 0; i < L; i++) {
            sequence = findConway(sequence);
            System.out.println(sequence.stream().map(s -> s.toString()).collect(Collectors.joining(" ")));
        }
    }
    
    /** Returns the resulting conway sequence */
    private static List<Integer> findConway(List<Integer> list) {
        List<Integer> conway = new ArrayList<>();
        
        while (list.size() != 0) {
            if (list.size() == 1) {
                conway.add(1);
                conway.add(list.get(0));
                list.clear();
            } else {
                int count = 1;
                while (count < list.size() && list.get(count) == list.get(0)) count++;
                conway.add(count);
                conway.add(list.get(0));
                list.subList(0, count).clear();
            }
        }
        return conway;
    }
}