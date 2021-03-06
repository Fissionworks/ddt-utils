package com.fissionworks.ddtutils.data.generator;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * DateTimeUtils wraps the new Java datetime API, and provides useful functionality such as allowing the
 * building of datetimes relative to a given datetime (generally {@link ZonedDateTime#now()}). This allows the
 * generation of data (most notably for testing) for cases when a relative time (i.e. 1 hour from now, 2 days
 * from now, 10 minutes from now, etc.) is needed rather than a fixed time.
 *
 * @since 1.0.0
 */
public final class DateTimeUtils {

    private static final Pattern DATETIME_KEYWORD_PATTERN = Pattern.compile("^\\[ *datetime.*\\]$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern DURATION_MODIFIER_PATTERN = Pattern.compile("(?<=\\{) *[-+]?[0-9]+[hm]{1} *(?<!})");

    private static final Pattern PERIOD_MODIFIER_PATTERN = Pattern.compile("(?<=\\{) *[-+]?[0-9]+[yMwd]{1} *(?<!})");

    private static final Pattern START_MODIFIER_PATTERN = Pattern.compile("(?<=\\{) *start *=[^\\}]*");

    private static final Pattern ZONE_MODIFIER_PATTERN = Pattern.compile("(?<=\\{) *zoneid *=[^\\}]*");

    private DateTimeUtils() throws IllegalAccessException {
        throw new IllegalAccessException("DateTimeUtils is a utility class, and should not be instantiated");
    }

    /**
     * Creates a {@link ZonedDateTime} based on the passed in parameter string, which should include any
     * required modifiers. The following should be understood, since they can impact the generated datetime:
     *
     * <ul>
     * <li>Date and time adjustments are applied in the following order: years, months, weeks, days, hours,
     * minutes. This should be taken into consideration for adjustments that may cross over daylight savings,
     * or leap years.</li>
     * <li>The days modifier deals with conceptual days, which may or may not be 24 hours (in the case of
     * daylight savings). For example, using {+1d} at 18:00 on the night of a time shift for daylight savings
     * will result in a datetime of 18:00 the next day. In use cases where the daylight savings time shift
     * needs to be accounted for, use '24 * number of days needed' with an hours modifier instead (i.e {+24h}
     * instead of {+1d}).</li>
     * <li>The datetime adjustments are cumulative, so using {+1y} twice is equivalent to a using {+2y}</li>
     * <li>The datetime keyword and modifiers have built in tolerance for leading/trailing whitespace where
     * practical, and are generally case insensitive (except the temporal modifiers which through necessity
     * are case sensitive).</li>
     * </ul>
     *
     * The format required to successfully generate a datetime includes the following keyword and modifiers:
     * <ul>
     * <li>[datetime] - The main keyword for generating a datetime, which is the minimum required argument to
     * the generate method and encloses the other modifiers. Passing in [datetime] with no other keywords will
     * return the current system date and time</li>
     * <li>Temporal modifiers - the temporal modifiers can be added inside the [datetime] keyword to make
     * positive or negative adjustments to the current datetime. The format for the temporal modifiers is a
     * positive or negative integer, followed by the units, enclosed in curly braces(i.e {+2y}, {-5h}, {+15M},
     * etc.).Note that while the '+' sign before positive integers is optional, it is recommended for
     * readibility.The following are valid modification units:
     * <ul>
     * <li>y - years</li>
     * <li>M - months</li>
     * <li>w - weeks</li>
     * <li>d - days</li>
     * <li>h - hours</li>
     * <li>m - minutes</li>
     * </ul>
     * </li>
     * <li>{zoneid=xxxxx} - sets the zone desired for the retuned DateTime; Any string value that can be
     * parsed by {@link ZoneId#of(String)} is acceptable (i.e. America/New_York, UTC, Pacific/Honolulu,
     * -07:00, etc.)</li>
     * <li>{start=xxxxx} - sets the starting datetime to generate a datetime relative to, if a starting
     * datetime other than {@link ZonedDateTime#now()} is required. The required format for the start datetime
     * is ISO_ZONED_DATE_TIME.</li>
     * </ul>
     * Examples of [datetime] strings and the resultant {@link ZonedDateTime}:
     * <ul>
     * <li>[datetime] - the current system {@link ZonedDateTime}</li>
     * <li>[datetime{+1y}] - the current system {@link ZonedDateTime} plus one year</li>
     * <li>[datetime{+5M}{-3d}] - the current system {@link ZonedDateTime} plus five months, minus 3 days</li>
     * <li>[datetime{+5m}{-3m}] - the current system {@link ZonedDateTime} plus 2 minutes (adds five minutes,
     * but then subtracts 3 minutes)</li>
     * </ul>
     *
     *
     * @param parameterString
     *            The [datetime] keyword string to generate the {@link ZonedDateTime} from.
     * @return A {@link ZonedDateTime} with all modifications specified in the parameterString.
     * @since 1.0.0
     */
    public static ZonedDateTime generate(final String parameterString) {
        Validate.isTrue(isDatetimeKeyword(parameterString),
                "'" + parameterString + "' must contain the '[datetime]' keyword");
        validateParameterString(parameterString);
        final Period period = getPeriodModification(parameterString);
        final Duration duration = getDurationModification(parameterString);
        final ZonedDateTime startDateTime = getStartDateTime(parameterString);
        return startDateTime.withZoneSameInstant(getZoneId(parameterString)).plus(period).plus(duration);
    }

    /**
     * Checks if the passed in String is a valid parameter string as required by {@link #generate(String)}.
     * Leading/trailing whitespace is ignored, but false is returned if there are any extra characters
     * surrounding the passed in targetString. example: '[datetime{+1y}{-21d}]' would return true, but
     * 'foo[datetime{+5h}]bar' returns false. The structure of the internal modifiers ({+1y}, {-21d}, etc.) is
     * not evaluated.
     *
     * @param targetString
     *            The string to determine status of.
     * @return True if the targetString is a [randstring] usable in {@link #generate(String)}, false
     *         otherwise.
     * @since 1.0.0
     */
    public static boolean isDatetimeKeyword(final String targetString) {
        return DATETIME_KEYWORD_PATTERN.matcher(StringUtils.strip(targetString)).matches();
    }

    private static Duration getDuration(final String group) {
        Duration duration = Duration.ZERO;
        final String temporalModifier = group.trim();
        final int amount = Integer.parseInt(StringUtils.chop(temporalModifier.trim()));
        final String unit = temporalModifier.substring(temporalModifier.length() - 1, temporalModifier.length());
        if ("h".equals(unit)) {
            duration = Duration.ofHours(amount);
        }
        if ("m".equals(unit)) {
            duration = Duration.ofMinutes(amount);
        }
        return duration;
    }

    private static Duration getDurationModification(final String parameterString) {
        Duration duration = Duration.ZERO;
        final Matcher matcher = DURATION_MODIFIER_PATTERN.matcher(parameterString);
        while (matcher.find()) {
            duration = duration.plus(getDuration(matcher.group()));
        }
        return duration;
    }

    private static int getModifierCount(final Matcher matcher) {
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static Period getPeriod(final String group) {
        Period period = Period.ZERO;
        final String temporalModifier = group.trim();
        final int amount = Integer.parseInt(StringUtils.chop(temporalModifier.trim()));
        final String unit = temporalModifier.substring(temporalModifier.length() - 1, temporalModifier.length());
        if ("d".equals(unit)) {
            period = Period.ofDays(amount);
        }
        if ("M".equals(unit)) {
            period = Period.ofMonths(amount);
        }
        if ("w".equals(unit)) {
            period = Period.ofWeeks(amount);
        }
        if ("y".equals(unit)) {
            period = Period.ofYears(amount);
        }

        return period;
    }

    private static Period getPeriodModification(final String parameterString) {
        Period period = Period.ZERO;
        final Matcher matcher = PERIOD_MODIFIER_PATTERN.matcher(parameterString);
        while (matcher.find()) {
            period = period.plus(getPeriod(matcher.group()));
        }
        return period;
    }

    private static ZonedDateTime getStartDateTime(final String parameterString) {
        final Matcher matcher = START_MODIFIER_PATTERN.matcher(parameterString);
        ZonedDateTime start = ZonedDateTime.now();
        if (matcher.find()) {
            final Matcher startDateMatcher = Pattern.compile("(?<==)[^\\}]*").matcher(matcher.group());
            startDateMatcher.find();
            try {
                start = ZonedDateTime.parse(startDateMatcher.group().trim());
            } catch (final DateTimeParseException exception) {
                throw new IllegalArgumentException("'" + startDateMatcher.group() + "' is not a valid start datetime",
                        exception);
            }
        }
        return start;
    }

    private static ZoneId getZoneId(final String parameterString) {
        final Matcher matcher = ZONE_MODIFIER_PATTERN.matcher(parameterString);
        if (matcher.find()) {
            final Matcher zoneIdMatcher = Pattern.compile("(?<==)[^\\}]*").matcher(parameterString);
            zoneIdMatcher.find();
            return ZoneId.of(StringUtils.strip(zoneIdMatcher.group()));
        }
        return ZoneId.systemDefault();
    }

    private static void validateParameterString(final String parameterString) {
        Validate.isTrue(getModifierCount(ZONE_MODIFIER_PATTERN.matcher(parameterString)) < 2,
                "Only one {zoneId} modifier allowed per parameterString");
        Validate.isTrue(getModifierCount(START_MODIFIER_PATTERN.matcher(parameterString)) < 2,
                "Only one {start} modifier allowed per parameterString");
        String reducedString = DURATION_MODIFIER_PATTERN.matcher(parameterString).replaceAll(" ");
        reducedString = PERIOD_MODIFIER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = ZONE_MODIFIER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = START_MODIFIER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = Pattern.compile("\\{ *\\}").matcher(reducedString).replaceAll(" ");
        Validate.isTrue(
                Pattern.compile("^\\[ *datetime *\\]$", Pattern.CASE_INSENSITIVE).matcher(reducedString.trim())
                        .matches(),
                "Invalid parameter string; The following is the parameter string with the valid keywords removed: "
                        + reducedString);
    }
}
