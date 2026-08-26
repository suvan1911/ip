import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Converts user-supplied text into {@link LocalDateTime} values and formats
 * them back for display.
 */
public class DateTimeParser {
    // Formats that include a time, tried in order when reading user input.
    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
    );

    // Date-only formats; the time defaults to midnight.
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = List.of(
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    /** Format used whenever a date-time is shown to the user. */
    private static final DateTimeFormatter DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Parses user input into a date-time, accepting any of the supported
     * formats, e.g. "2/12/2019 1800" or "2019-12-02".
     *
     * @throws LenZaBotException If the input matches none of the supported formats.
     */
    public static LocalDateTime parse(String input) throws LenZaBotException {
        String trimmedInput = input.trim();
        for (DateTimeFormatter formatter : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(trimmedInput, formatter);
            } catch (DateTimeParseException e) {
                // Not this format; try the next one.
            }
        }

        for (DateTimeFormatter formatter : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(trimmedInput, formatter).atStartOfDay();
            } catch (DateTimeParseException e) {
                // Not this format; try the next one.
            }
        }

        throw new LenZaBotException(
            "`/by`, `/from`, and `/to` must be a date/time like `2/12/2019 1800` "
            + "or `2019-12-02 1800`."
        );
    }

    /**
     * Returns the given date-time formatted for display,
     * e.g. "Dec 2, 2019, 6:00 PM".
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMATTER);
    }
}
