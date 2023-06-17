package de.cantry.csgocasestatsviewerv2.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static String regexFindFirst(String pattern, String text) throws Exception {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        throw new Exception("RegexFindFirst, pattern not found. Searched: " + pattern);
    }

    public static List<String> regexFindAll(String pattern, String text) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }


}
