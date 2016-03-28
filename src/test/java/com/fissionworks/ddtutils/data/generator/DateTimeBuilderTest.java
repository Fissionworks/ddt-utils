package com.fissionworks.ddtutils.data.generator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DateTimeBuilderTest {

    @Test
    public void buildDateTime_withNegativeDayAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int dayAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofDays(dayAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().days(dayAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNegativeHourAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int hourAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofHours(hourAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().hours(hourAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNegativeMinuteAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int minuteAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofMinutes(minuteAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().minutes(minuteAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNegativeMonthAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int monthAdjustment = -137;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofMonths(monthAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().months(monthAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNegativeWeekAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int weekAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofWeeks(weekAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().weeks(weekAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNegativeYearAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int yearAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofYears(yearAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().years(yearAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withNoParametersSet_shouldReturnCurrentDateTime() throws InterruptedException {
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now();
        final ZonedDateTime actual = new DateTimeBuilder().buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveDayAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int dayAdjustment = 45;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofDays(dayAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().days(dayAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveHourAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int hourAdjustment = 45;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofHours(hourAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().hours(hourAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveMinuteAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int minuteAdjustment = 45;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofMinutes(minuteAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().minutes(minuteAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveMonthAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int monthAdjustment = 45;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofMonths(monthAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().months(monthAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveWeekAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int weekAdjustment = 45;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofWeeks(weekAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().weeks(weekAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void buildDateTime_withPositiveYearAdjustment_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int yearAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofYears(yearAdjustment));
        final ZonedDateTime actual = new DateTimeBuilder().years(yearAdjustment).buildDateTime();
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    // waits for time to roll to new minute if close to doing so to avoid intermittent errors when asserting
    // datetime values
    private void waitForCleanTime() throws InterruptedException {
        if (LocalDateTime.now().getSecond() > 57) {
            Thread.sleep(3000);
        }
    }
}
