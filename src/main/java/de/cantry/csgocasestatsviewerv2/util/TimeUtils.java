package de.cantry.csgocasestatsviewerv2.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static DateTimeFormatter stringToLongDateConverter = DateTimeFormatter.ofPattern("d MMM, yyyy h:mma", Locale.ENGLISH);

    public static SimpleDateFormat longToStringDateConverter;

    static {
        longToStringDateConverter = new SimpleDateFormat("d MMM yyyy h:mma", Locale.ENGLISH);
        longToStringDateConverter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

}
