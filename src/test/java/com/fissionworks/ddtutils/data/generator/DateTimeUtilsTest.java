package com.fissionworks.ddtutils.data.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DateTimeUtilsTest {

    @Test
    public void DateTimeUtils_shouldHaveInaccessibleConstructor() {
        final Constructor<?>[] constructors = DateTimeUtils.class.getDeclaredConstructors();
        final Constructor<?> constructor = constructors[0];
        Assert.assertFalse(constructor.isAccessible(), "Constructor should be inaccessible");
    }

    @Test
    public void DateTimeUtils_shouldHaveOneConstructor() {
        final Constructor<?>[] constructors = DateTimeUtils.class.getDeclaredConstructors();
        Assert.assertEquals(constructors.length, 1);
    }

    @Test(expectedExceptions = InvocationTargetException.class)
    public void DateTimeUtilsInstantiationThroughReflection_shouldThrowException() throws Exception {
        final Constructor<?>[] constructors = DateTimeUtils.class.getDeclaredConstructors();
        final Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void generate_withAllModifiers_shouldReturnCurrentDateTimeWithAdjusments() throws InterruptedException {
        final int yearAdjustment = 2;
        final int monthAdjustment = -4;
        final int weekAdjustment = 3;
        final int dayAdjustment = -12;
        final int hourAdjustment = 5;
        final int minuteAdjustment = -7;
        final String start = "2016-04-12T13:06:04.157-04:00[Africa/Djibouti]";
        final String zoneId = "Africa/Djibouti";
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.parse(start).plus(Period.ofYears(yearAdjustment))
                .plus(Period.ofMonths(monthAdjustment)).plus(Period.ofWeeks(weekAdjustment))
                .plus(Period.ofDays(dayAdjustment)).plus(Duration.ofHours(hourAdjustment))
                .plus(Duration.ofMinutes(minuteAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format(
                "  [  datetime { zoneid = %s } {  +%sy } { %sM } {  +%sw }  { %sd } { +%sh } { %sm } {start=" + start
                        + "}]",
                zoneId, yearAdjustment, monthAdjustment, weekAdjustment, dayAdjustment, hourAdjustment,
                minuteAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withDatetimeKeywordNoModifiers_shouldReturnCurrentDatetime() throws InterruptedException {
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now();
        final ZonedDateTime actual = DateTimeUtils.generate("[datetime]");
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withInvalidCharacters_shouldThrowException() {
        DateTimeUtils.generate("[datetime{+1y}x{zoneid=UTC}]");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withInvalidStart_shouldThrowException() {
        DateTimeUtils.generate("[datetime{start=xxx}]");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withMultipleStartModifiers_shouldThrowException() throws InterruptedException {
        DateTimeUtils.generate(
                "[datetime{start=2016-04-12T12:19:07.549-04:00[America/New_York]}{start=2016-04-12T12:19:07.549-04:00[UTC]}]");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withMultipleZoneModifiers_shouldThrowException() throws InterruptedException {
        DateTimeUtils.generate("[datetime{zoneid=UTC}{zoneid=America/New_York}]");
    }

    @Test
    public void generate_withNegativeDayModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int dayAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofDays(dayAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sd}]", dayAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withNegativeHourModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int hourAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofHours(hourAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sh}]", hourAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void generate_withNegativeMinuteModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int minuteAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofMinutes(minuteAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sm}]", minuteAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withNegativeMonthModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int monthAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofMonths(monthAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sM}]", monthAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void generate_withNegativeWeekModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int weekAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofWeeks(weekAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sw}]", weekAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withNegativeYearModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int yearAdjustment = -42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofYears(yearAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{%sy}]", yearAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withoutDatetimeKeyword_shouldThrowException() {
        DateTimeUtils.generate("[notadatetime]");
    }

    @Test
    public void generate_withPositiveDayModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int dayAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofDays(dayAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sd}]", dayAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());

    }

    @Test
    public void generate_withPositiveHourModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int hourAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofHours(hourAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sh}]", hourAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withPositiveMinuteModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int minuteAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Duration.ofMinutes(minuteAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sm}]", minuteAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withPositiveMonthModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int monthAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofMonths(monthAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sM}]", monthAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withPositiveWeekModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int weekAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofWeeks(weekAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sw}]", weekAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withPositiveYearModifier_shouldReturnCurrentDateTimeWithAdjusment()
            throws InterruptedException {
        final int yearAdjustment = 42;
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().plus(Period.ofYears(yearAdjustment));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{+%sy}]", yearAdjustment));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withStartModifierOnly_shouldReturnStartDate() throws InterruptedException {
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().minusHours(5);
        final ZonedDateTime actual = DateTimeUtils
                .generate("[datetime{start=" + ZonedDateTime.now(ZoneId.of("UTC")).minusHours(5) + "}]");
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withStartModifierWithInternalWhitespace_shouldReturnStartDate() throws InterruptedException {
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now().minusHours(5);
        final ZonedDateTime actual = DateTimeUtils
                .generate("[datetime{  start  =  " + ZonedDateTime.now(ZoneId.of("UTC")).minusHours(5) + "   }  ]");
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void generate_withZoneModifier_shouldReturnCurrentDateTimeWithAdjusment() throws InterruptedException {
        final String zoneId = "Africa/Djibouti";
        waitForCleanTime();
        final ZonedDateTime expected = ZonedDateTime.now(ZoneId.of(zoneId));
        final ZonedDateTime actual = DateTimeUtils.generate(String.format("[datetime{zoneid=%s}]", zoneId));
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.getHour(), expected.getHour());
        Assert.assertEquals(actual.getMinute(), expected.getMinute());
        Assert.assertEquals(actual.getDayOfMonth(), expected.getDayOfMonth());
        Assert.assertEquals(actual.getMonthValue(), expected.getMonthValue());
        Assert.assertEquals(actual.getYear(), expected.getYear());
        Assert.assertEquals(actual.getZone(), expected.getZone());
    }

    @Test
    public void isDatetimeKeyword_withKeywordStringWithInternalWhitespace_shouldReturnTrue() {
        Assert.assertTrue(DateTimeUtils.isDatetimeKeyword("    [    datetime   {xxxxx}]   "));
    }

    @Test
    public void isDatetimeKeyword_withKeywordStringWithLeadingTrailingWhitespace_shouldReturnTrue() {
        Assert.assertTrue(DateTimeUtils.isDatetimeKeyword("    [datetime{xxxxx}]   "));
    }

    @Test
    public void isDatetimeKeyword_withKeywordStringWithMixedCase_shouldReturnTrue() {
        Assert.assertTrue(DateTimeUtils.isDatetimeKeyword("[DaTeTiMe{xxxxx}]"));
    }

    @Test
    public void isDatetimeKeyword_withNonKeywordString_shouldReturnFalse() {
        Assert.assertFalse(DateTimeUtils.isDatetimeKeyword("[notadatetime{xxxxx}]"));
    }

    @Test
    public void isDatetimeKeyword_withSimpleKeywordString_shouldReturnTrue() {
        Assert.assertTrue(DateTimeUtils.isDatetimeKeyword("[datetime{xxxxx}]"));
    }

    // waits for time to roll to new minute if close to doing so to avoid intermittent errors when asserting
    // datetime values
    private void waitForCleanTime() throws InterruptedException {
        if (LocalDateTime.now().getSecond() > 57) {
            Thread.sleep(3000);
        }
    }
}
