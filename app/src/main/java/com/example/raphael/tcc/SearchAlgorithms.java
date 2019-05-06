package com.example.raphael.tcc;

/**
 * Created by rapha on 09-Oct-16.
 */

public class SearchAlgorithms {
    public static int binarySearch(int key,int core,int clockLevels[][]) {
        int size = clockLevels[core].length;
        int low = 0;
        int high = size - 1;
        while(high >= low) {
            int middle = (low + high) / 2;
            if(clockLevels[core][middle] == key) {
                return middle;
            }
            if(clockLevels[core][middle] < key) {
                low = middle + 1;
            }
            if(clockLevels[core][middle] > key) {
                high = middle - 1;
            }
        }
        return -1;
    }
}
