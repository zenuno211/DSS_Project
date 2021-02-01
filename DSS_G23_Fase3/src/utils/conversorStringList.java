package utils;

import java.util.List;
import java.util.Arrays;


public class conversorStringList {

    private static String SPLIT_REGEX = "#!&";
    

    public static List<String> stringToList(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return Arrays.asList(databaseValue.split(SPLIT_REGEX));
    }

    public static String listToString(List<String> entityProperty) {
        if (entityProperty == null) {
            return null;
        }

        StringBuilder  stringBuilder = new StringBuilder();

        for (String string: entityProperty) {
            stringBuilder.append(string).append(SPLIT_REGEX);
        }

        return stringBuilder.toString();
    }
}
	
