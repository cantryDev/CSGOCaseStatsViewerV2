package de.cantry.csgocasestatsviewerv2.util;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {


    public static DateTimeFormatter euStringToLongDateConverter = DateTimeFormatter.ofPattern("d MMM, yyyy h:mma", Locale.ENGLISH);
    public static DateTimeFormatter usStringToLongDateConverter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mma", Locale.ENGLISH);
    private static DateTimeFormatter[] stringToLongConverters;

    public static SimpleDateFormat longToStringDateConverter;

    static {
        longToStringDateConverter = new SimpleDateFormat("d MMM yyyy h:mma", Locale.ENGLISH);
        longToStringDateConverter.setTimeZone(TimeZone.getDefault());
        stringToLongConverters = new DateTimeFormatter[]{usStringToLongDateConverter, euStringToLongDateConverter};
    }

    public static long getTimeFromString(String timeString) {
        for (DateTimeFormatter formatter : stringToLongConverters) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(timeString, formatter.withZone(ZoneId.systemDefault()));
                return dateTime.toInstant().getEpochSecond();
            } catch (DateTimeParseException e) {
            }
        }
        return 0;
    }

}
