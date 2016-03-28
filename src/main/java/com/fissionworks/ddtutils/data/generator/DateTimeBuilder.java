package com.fissionworks.ddtutils.data.generator;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

/**
 * The DateTimeBuilder wraps the new Java datetime API, and allows building datetimes relative to a given
 * datetime (generally {@link ZonedDateTime#now()}). This allows the generation of data (most notably for
 * testing) for cases when a relative time (i.e. 1 hour from now, 2 days from now, 10 minutes from now, etc.)
 * is needed rather than a fixed time. The following should be understood, since they can impact the generated
 * datetime:
 *
 * <ul>
 * <li>date and time adjustments are stored, and only applied once {@link #buildDateTime()} is called.</li>
 * <li>date and time adjustments are applied in the following order: years, months, weeks, days, hours,
 * minutes. This should be taken into consideration for adjustments that may cross over daylight savings, or
 * leap years.</li>
 * <li>The {@link #days(int)} adjustment deals with conceptual days, which may or may not be 24 hours (in the
 * case of daylight savings). For example, calling {@link #days(int)} at 18:00 and passing in '1' on the night
 * of a time shift for daylight savings will result in a datetime of 18:00 the next day. In use cases where
 * the daylight savings time shift needs to be accounted for, call {@link #hours(int)} with '24 * number of
 * days needed' instead.</li>
 * <li>The datetime adjustments are cumulative, so calling {@link #years(int)} twice and passing in '2' both
 * times is equivalent to a single call to {@link #years(int)} and passing in '4'</li>
 * </ul>
 *
 * @since 1.0.0
 */
public final class DateTimeBuilder {

    private Duration durationOffset = Duration.ZERO;

    private Period periodOffset = Period.ZERO;

    /**
     * Builds a {@link ZonedDateTime} with all date and time adjustments applied; The date and time
     * adjustments are applied in the following order: years, months, weeks, days, hours, and minutes.
     *
     * @return A {@link ZonedDateTime} equal to {@link ZonedDateTime#now()} with all adjustments applied.
     * @since 1.0.0
     */
    public ZonedDateTime buildDateTime() {
        return ZonedDateTime.now().plus(periodOffset).plus(durationOffset);
    }

    /**
     * Adds/Subtracts the passed in number of days from the final datetime.
     *
     * @param daysAdjustment
     *            The number of days to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder days(final int daysAdjustment) {
        periodOffset = periodOffset.plus(Period.ofDays(daysAdjustment));
        return this;
    }

    /**
     * Adds/Subtracts the passed in number of hours from the final datetime.
     *
     * @param hoursAdjustment
     *            The number of hours to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder hours(final int hoursAdjustment) {
        durationOffset = durationOffset.plus(Duration.ofHours(hoursAdjustment));
        return this;
    }

    /**
     * Adds/Subtracts the passed in number of minutes from the final datetime.
     *
     * @param minutesAdjustment
     *            The number of minutes to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder minutes(final int minutesAdjustment) {
        durationOffset = durationOffset.plus(Duration.ofMinutes(minutesAdjustment));
        return this;
    }

    /**
     * Adds/Subtracts the passed in number of months from the final datetime.
     *
     * @param monthsAdjustment
     *            The number of months to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder months(final int monthsAdjustment) {
        periodOffset = periodOffset.plus(Period.ofMonths(monthsAdjustment));
        return this;
    }

    /**
     * Adds/Subtracts the passed in number of weeks from the final datetime.
     *
     * @param weeksAdjustment
     *            The number of weeks to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder weeks(final int weeksAdjustment) {
        periodOffset = periodOffset.plus(Period.ofWeeks(weeksAdjustment));
        return this;
    }

    /**
     * Adds/Subtracts the passed in number of years from the final datetime.
     *
     * @param yearsAdjustment
     *            The number of years to add/subtract from the datetime.
     * @return returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public DateTimeBuilder years(final int yearsAdjustment) {
        periodOffset = periodOffset.plus(Period.ofYears(yearsAdjustment));
        return this;
    }

}
