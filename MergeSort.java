import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

    // Overloaded mergeSort method for sorting by name
    public static void mergeSort(List<Question> list) {
        mergeSort(list, Comparator.comparing(Question::getName));
    }

    // Overloaded mergeSort method for sorting by difficulty
    public static void mergeSortByDifficulty(List<Question> list) {
        mergeSort(list, Comparator.comparingInt(Question::getDifficulty));
    }

    // Generic mergeSort method using Comparator
    private static void mergeSort(List<Question> list, Comparator<Question> comparator) {
        if (list.size() < 2) {
            return; // Base case: list is already sorted
        }

        int mid = list.size() / 2;
        List<Question> leftHalf = new ArrayList<>(list.subList(0, mid));
        List<Question> rightHalf = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(leftHalf, comparator); // Recursively sort the left half
        mergeSort(rightHalf, comparator); // Recursively sort the right half
        merge(list, leftHalf, rightHalf, comparator); // Merge the sorted halves
    }

    // Generic merge method using Comparator
    private static void merge(List<Question> list, List<Question> leftHalf, List<Question> rightHalf, Comparator<Question> comparator) {
        int leftIndex = 0, rightIndex = 0, listIndex = 0;

        while (leftIndex < leftHalf.size() && rightIndex < rightHalf.size()) {
            if (comparator.compare(leftHalf.get(leftIndex), rightHalf.get(rightIndex)) < 0) {
                list.set(listIndex++, leftHalf.get(leftIndex++));
            } else {
                list.set(listIndex++, rightHalf.get(rightIndex++));
            }
        }

        // Copy the remaining elements of leftHalf and rightHalf if any
        while (leftIndex < leftHalf.size()) {
            list.set(listIndex++, leftHalf.get(leftIndex++));
        }
        while (rightIndex < rightHalf.size()) {
            list.set(listIndex++, rightHalf.get(rightIndex++));
        }
    }

    // Specific mergeSort method for sorting Test objects by name
    public static void mergeSortTests(List<Test> list) {
        if (list.size() < 2) {
            return;
        }

        int mid = list.size() / 2;
        List<Test> left = new ArrayList<>(list.subList(0, mid));
        List<Test> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSortTests(left); // Sort the left half
        mergeSortTests(right); // Sort the right half

        mergeTests(list, left, right); // Merge the sorted halves
    }

    // Helper method to merge two halves of a Test list
    private static void mergeTests(List<Test> result, List<Test> left, List<Test> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getName().compareToIgnoreCase(right.get(j).getName()) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        // Copy the remaining elements
        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }

}
