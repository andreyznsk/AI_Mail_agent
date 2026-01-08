package andreyz.agent.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class MailDateUtils {

    // Приватный конструктор — класс только для утилит
    private MailDateUtils() {}

    /**
     * Конвертирует java.util.Date в ZonedDateTime.
     * Если дата null, возвращает null.
     * 
     * @param date java.util.Date
     * @return ZonedDateTime в системной временной зоне
     */
    public static ZonedDateTime toZonedDateTime(Date date) {
        return toZonedDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Конвертирует internalDate (epoch ms) Gmail API в ZonedDateTime
     * @param internalDateLong значение internalDate
     * @param zone желаемая временная зона
     * @return ZonedDateTime или null если internalDateLong == null
     */
    public static ZonedDateTime fromGmailInternalDate(Long internalDateLong, ZoneId zone) {
        if (internalDateLong == null) return null;
        return Instant.ofEpochMilli(internalDateLong).atZone(zone);
    }

    /**
     * Overload с системной временной зоной
     */
    public static ZonedDateTime fromGmailInternalDate(Long internalDateLong) {
        return fromGmailInternalDate(internalDateLong, ZoneId.systemDefault());
    }

    /**
     * Конвертирует java.util.Date в ZonedDateTime с заданной зоной.
     * Если дата null, возвращает null.
     * 
     * @param date java.util.Date
     * @param zone желаемая временная зона
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zone) {
        if (date == null) return null;
        Instant instant = date.toInstant();
        return instant.atZone(zone);
    }
}
