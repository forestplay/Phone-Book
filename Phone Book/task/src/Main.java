import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            String directoryFile = "C:\\Users\\Bob\\IdeaProjects\\Phone Book\\directoryMedium.txt";
            Scanner directoryScanner = new Scanner(new File(directoryFile));
            ArrayList<String> unsortedDirectory = new ArrayList<>();
            while (directoryScanner.hasNext()) {
                String input = directoryScanner.nextLine();
                String number = input.replaceAll("\\D*", "");
                String name = input.replaceFirst("\\d+ ", "");
                unsortedDirectory.add(name + " " + number);
            }
            directoryScanner.close();

            String findFile = "C:\\Users\\Bob\\IdeaProjects\\Phone Book\\findMedium.txt";
            Scanner findScanner = new Scanner(new File(findFile));
            ArrayList<String> findNames = new ArrayList<>();
            while (findScanner.hasNext()) {
                findNames.add(findScanner.nextLine());
            }

            ArrayList<String> sortedDirectory;
            int found;
            LocalTime sortAndSearchStart;
            LocalTime sortStart;
            LocalTime sortFinish;
            LocalTime searchStart;
            LocalTime searchFinish;
            LocalTime sortAndSearchFinish;

            System.out.println("Start searching (linear search)...");
            searchStart = LocalTime.now();
            found = 0;
            for (String name : findNames)
                found += linearSearch(unsortedDirectory, name) >= 0 ? 1 : 0;
            searchFinish = LocalTime.now();
            System.out.println("Found " + found + " / " + findNames.size() + ". "
                    + "Time taken: " + timeBetween(searchStart, searchFinish));
            System.out.println();

            System.out.println("Start searching (bubble sort + jump search)...");
            sortAndSearchStart = LocalTime.now();
            sortStart = LocalTime.now();
            sortedDirectory = bubbleSort((ArrayList<String>) unsortedDirectory.clone());
            sortFinish = LocalTime.now();

            searchStart = LocalTime.now();
            found = 0;
            for (String name : findNames)
                found += jumpSearch(sortedDirectory, name) >= 0 ? 1 : 0;
            searchFinish = LocalTime.now();
            sortAndSearchFinish = LocalTime.now();

            System.out.println("Found " + found + " / " + findNames.size() + ". "
                    + "Time taken: " + timeBetween(sortAndSearchStart, sortAndSearchFinish));
            System.out.println("Sorting time: " + timeBetween(sortStart, sortFinish));
            System.out.println("Searching time: " + timeBetween(searchStart, searchFinish));
            System.out.println();

            System.out.println("Start searching (quick sort + binary search)...");
            sortAndSearchStart = LocalTime.now();
            sortStart = LocalTime.now();
            sortedDirectory = quickSort((ArrayList<String>) unsortedDirectory.clone(), 0, unsortedDirectory.size() - 1);
            sortFinish = LocalTime.now();

            searchStart = LocalTime.now();
            found = 0;
            for (String name : findNames)
                found += binarySearch(sortedDirectory, name, 0, sortedDirectory.size() - 1) >= 0 ? 1 : 0;
            searchFinish = LocalTime.now();
            sortAndSearchFinish = LocalTime.now();

            System.out.println("Found " + found + " / " + findNames.size() + ". "
                    + "Time taken: " + timeBetween(sortAndSearchStart, sortAndSearchFinish));
            System.out.println("Sorting time: " + timeBetween(sortStart, sortFinish));
            System.out.println("Searching time: " + timeBetween(searchStart, searchFinish));
            System.out.println();

            System.out.println("Start searching (hash table)...");
            sortAndSearchStart = LocalTime.now();
            sortStart = LocalTime.now();

            int dirSize = unsortedDirectory.size();
            HashTable hashTable = new HashTable(dirSize * 2);

            for (String input : unsortedDirectory) {
                String number = input.replaceAll("\\D*", "");
                String name = input.replaceAll("\\d+", "").trim();
                hashTable.put(name, number);
            }
            sortFinish = LocalTime.now();

            searchStart = LocalTime.now();
            found = 0;
            for (String name : findNames)
                found += hashTable.get(name) != null ? 1 : 0;
            searchFinish = LocalTime.now();
            sortAndSearchFinish = LocalTime.now();

            System.out.println("Found " + found + " / " + findNames.size() + ". "
                    + "Time taken: " + timeBetween(sortAndSearchStart, sortAndSearchFinish));
            System.out.println("Creating time: " + timeBetween(sortStart, sortFinish));
            System.out.println("Searching time: " + timeBetween(searchStart, searchFinish));
            System.out.println();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int linearSearch(ArrayList<String> directory, String target) {
//            System.out.print(target);
        boolean found = false;
        int index;
        for (index = 0; index < directory.size(); index++) {
            if (directory.get(index).matches(target + ".*")) {
                found = true;
                break;
            }
        }
//            System.out.println(found ? "  + found" : "  - not found");
        return found ? index : -1;
    }

    public static ArrayList<String> bubbleSort(ArrayList<String> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            for (int j = 0; j < array.size() - i - 1; j++) {
                /* if a pair of adjacent elements has the wrong order it swaps them */
                if (array.get(j).compareTo(array.get(j + 1)) > 0) {
                    String temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                }
            }
        }
        return array;
    }

    public static ArrayList<String> quickSort(ArrayList<String> array, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(array, left, right); // the pivot is already on its place
            quickSort(array, left, pivotIndex - 1);  // sort the left subarray
            quickSort(array, pivotIndex + 1, right); // sort the right subarray
            try {
                if (left % 5 == 0)   Thread.sleep(1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return array;
    }

    private static int partition(ArrayList<String> array, int left, int right) {
        String pivot = array.get(right);  // choose the rightmost element as the pivot
        int partitionIndex = left; // the first element greater than the pivot

        /* move large values into the right side of the array */
        for (int i = left; i < right; i++) {
            if (array.get(i).compareTo(pivot) < 0) { // may be used '<' as well
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(array, partitionIndex, right); // put the pivot on a suitable position

        return partitionIndex;
    }

    private static void swap(ArrayList<String> array, int i, int j) {
        String temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    public static int jumpSearch(ArrayList<String> array, String target) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (array.size() == 0) {
            return -1;
        }

        /* Check the first element */
        if (array.get(currentRight).matches(target + ".*")) {
            return 0;
        }

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(array.size());

        /* Finding a block where the element may be present */
        while (currentRight < array.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(array.size() - 1, currentRight + jumpLength);

            if (array.get(currentRight).compareTo(target) >= 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == array.size() - 1) && target.compareTo(array.get(currentRight)) > 0) {
            return -1;
        }

        /* Doing linear search in the found block */
        return backwardSearch(array, target, prevRight, currentRight);
    }

    public static int backwardSearch(ArrayList<String> array, String target, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (array.get(i).matches(target + ".*")) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(ArrayList<String> array, String elem, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2; // the index of the middle element

            if (array.get(mid).matches(elem + ".*")) {
                return mid; // the element is found, return its index
            } else if (elem.compareTo(array.get(mid)) < 0) {
                right = mid - 1; // go to the left subarray
            } else {
                left = mid + 1;  // go the the right subarray
            }
        }
        return -1; // the element is not found
    }

    public static String timeBetween(LocalTime start, LocalTime finish) {
        long elapsedMinutes = Duration.between(start, finish).toMinutes();
        long elapsedSeconds = Duration.between(start, finish).toSeconds() % 60;
        long elapsedMS = Duration.between(start, finish).toMillis() % 1000;
        return elapsedMinutes + " min. " + elapsedSeconds + " sec. " + elapsedMS + "ms.";
    }

    private static class TableEntry {
        private final String name;
        private final String number;

        public TableEntry(String name, String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }
    }

    private static class HashTable {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(String name, String number) {
            int idx = findKey(name);
            if (idx == -1) {
                return false;
            }
            table[idx] = new TableEntry(name, number);
            return true;
        }

        public String get(String name) {
            int idx = findKey(name);

            if (idx == -1 || table[idx] == null) {
                return null;
            }
            return table[idx].getNumber();
        }

        private int findKey(String name) {
            int hash = Math.abs(name.hashCode()) % size;
            while (!(table[hash] == null || table[hash].getName().equals(name))) {
                hash = (hash + 1) % size;
                if (hash == name.hashCode() % size) {
                    rehash();
                    hash = Math.abs(name.hashCode()) % size;
                }
            }
            return hash;
        }

        private void rehash() {
            this.size = this.size * 2;
            TableEntry[] oldTable = table;
            table = new TableEntry[this.size];
            for (TableEntry te : oldTable) {
                if (te != null) {
                    this.put(te.getName(), te.getNumber());
                }
            }
        }

    }

}
