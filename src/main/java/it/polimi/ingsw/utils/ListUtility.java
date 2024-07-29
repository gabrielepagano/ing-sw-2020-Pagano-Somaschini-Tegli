package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines some utility methods for lists of generic elements
 */
public class ListUtility {

    private ListUtility() {
        // Private constructor will prevent the instantiation of this class
    }

    /**
     * Utility method to have an Array.toString-type (without enclosing brackets) String representation of a
     * list of generic elements
     * @param list The list to be represented as a String
     * @return A string of comma-separated String representations of the elements of the specified list
     */
    public static String listToString(List<?> list) {

        if(list == null) {
            throw new IllegalArgumentException();
        }

        if (list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("");

        for(Object element : list) {
            if(element == null) {
                sb.append("null");
            } else {
                sb.append(element.toString());
            }
            sb.append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), "");

        return sb.toString();

    }

    /**
     * Utility method for right-shifting a list of generic elements for the specified number of times nPos.
     * A negative value for nPos is interpreted as a left-shift. nPos is normalized modulo the size of the list
     * @param list The list to be right-shifted
     * @param nPos The number of times the specified list has to be right-shifted
     * @param <T> The generic type of the list elements
     */
    public static <T> void shiftRight(List<T> list, int nPos) {

        if(list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }
        if(list.isEmpty()) {
            return;
        }

        int size = list.size();
        nPos = ((nPos % size) + size) % size;

        if(nPos == 0) {
            return;
        }

        List<T> subList = new ArrayList<>();
        for(int i = size - 1; i >= size - nPos; i--) {
            subList.add(list.remove(i));
        }
        for(int i = 0; i < nPos; i++) {
            list.add(0, subList.get(i));
        }

    }

}
