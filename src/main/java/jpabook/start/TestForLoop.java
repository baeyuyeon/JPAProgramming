package jpabook.start;

import java.util.ArrayList;
import java.util.List;

public class TestForLoop {

    static final int initialSize = 100000;

    public static void main(String[] args) {
        List<Integer> streamList = new ArrayList<>(initialSize);
        List<Integer> forList = new ArrayList<>(initialSize);

        for (int i = 0; i < initialSize; i++) {
            streamList.add(i);
            forList.add(i);
        }
        long startTime = System.nanoTime();
        streamList.stream().forEach(a -> System.out.print(a));
        long endTime = System.nanoTime();
        System.out.println();
        System.out.println("stream 수행 시간 : " + (endTime - startTime));
        System.out.println("--------------------------------------------");

        long startTime2 = System.nanoTime();
        for (int i = 0; i < forList.size(); i++) {
            System.out.print(forList.get(i));
        }
        long endTime2 = System.nanoTime();
        System.out.println();
        System.out.println("for loop 수행 시간 : " + (endTime2 - startTime2));
    }

}
