package de.cantry.csgocasestatsviewerv2.util;

public class FormatUtils {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String format(String input, int toLength, boolean inFront) {
        StringBuilder inputBuilder = new StringBuilder(input);
        while (inputBuilder.length() < toLength) {
            if (inFront) {
                inputBuilder.insert(0, " ");
            } else {
                inputBuilder.append(" ");
            }

        }
        input = inputBuilder.toString();
        return input;
    }

}
