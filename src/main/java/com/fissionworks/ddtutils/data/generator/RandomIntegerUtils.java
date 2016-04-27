package com.fissionworks.ddtutils.data.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * RandomIntegerUtils allows the creation of a random integer, wrapping common random number generation
 * calculations for cleaner code.
 *
 * @since 1.0.0
 *
 */
public final class RandomIntegerUtils {

    private static final Pattern RANDINT_KEYWORD_PATTERN = Pattern.compile("^\\[ *randint.*\\]$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern RANGE_MODIFIER_PATTERN = Pattern.compile("(?<=\\{) *range *=[^\\}]*",
            Pattern.CASE_INSENSITIVE);

    private RandomIntegerUtils() throws IllegalAccessException {
        throw new IllegalAccessException("RandomIntegerUtils is a utility class, and should not be instantiated");
    }

    /**
     * Creates a randomized integer between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}.
     *
     * @return A randomized integer from somewhere within the full integer range.
     * @since 1.0.0
     */
    public static int generate() {
        return (int) (Integer.MIN_VALUE + (Math.random() * (getRange(Integer.MIN_VALUE, Integer.MAX_VALUE)) + 1));
    }

    /**
     * Creates a randomized integer between the specified minimum and maximum values(inclusive).
     *
     * @param minValue
     *            The minimum value desired for the random integer.
     * @param maxValue
     *            The maximum value desired for the random integer.
     * @return A randomized integer from somewhere within the specified range.
     * @since 1.0.0
     */
    public static int generate(final int minValue, final int maxValue) {
        Validate.isTrue(maxValue > minValue, "The maxValue must be greater than the minValue");
        return (int) (minValue + (long) (Math.random() * ((getRange(minValue, maxValue) + 1))));
    }

    public static int generate(final String parameterString) {
        final String rangeString = getRangeString(parameterString);
        final String[] minMaxArray = rangeString.split(":");
        return RandomIntegerUtils.generate(Integer.parseInt(minMaxArray[0].trim()),
                Integer.parseInt(minMaxArray[1].trim()));
    }

    /**
     * Creates a randomized even integer between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}.
     *
     * @return A randomized even integer from somewhere within the full integer range.
     * @since 1.0.0
     */
    public static int generateEven() {
        return generateEven(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a randomized even integer between the specified minimum and maximum values(inclusive). If the
     * minimum number provided is odd, the minimum used will be minimum + 1. If the maximum number is odd, the
     * the maximum used will be maximum - 1.
     *
     * @param minValue
     *            The minimum value desired for the random integer.
     * @param maxValue
     *            The maximum value desired for the random integer.
     * @return A randomized even integer from somewhere within the specified range.
     * @since 1.0.0
     */
    public static int generateEven(final int minValue, final int maxValue) {
        final int min = minValue % 2 != 0 ? minValue + 1 : minValue;
        final int max = maxValue % 2 != 0 ? maxValue - 1 : maxValue;

        return (int) (min + 2 * (long) (Math.random() * ((getRange(min, max) / 2 + 1))));
    }

    /**
     * Creates a randomized odd integer between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}.
     *
     * @return A randomized odd integer from somewhere within the full integer range.
     * @since 1.0.0
     */
    public static int generateOdd() {
        return generateOdd(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a randomized odd integer between the specified minimum and maximum values(inclusive). If the
     * minimum number provided is even, the minimum used will be minimum + 1. If the maximum number is even,
     * the the maximum used will be maximum - 1.
     *
     * @param minValue
     *            The minimum value desired for the random integer.
     * @param maxValue
     *            The maximum value desired for the random integer.
     * @return A randomized even integer from somewhere within the specified range.
     * @since 1.0.0
     */
    public static int generateOdd(final int minValue, final int maxValue) {
        final int min = minValue % 2 == 0 ? minValue + 1 : minValue;
        final int max = maxValue % 2 == 0 ? maxValue - 1 : maxValue;

        return (int) (min + 2 * (long) (Math.random() * ((getRange(min, max) / 2 + 1))));
    }

    /**
     * Checks if the passed in String is a valid parameter string as required by {@link #generate(String)}.
     * Leading/trailing whitespace is ignored, but false is returned if there are any extra characters
     * surrounding the passed in targetString. example: '[randint{range=5:560}]' would return true, but
     * 'foo[randint{range=5:560}]bar' returns false.
     *
     * @param targetString
     *            The string to determine status of.
     * @return True if the targetString is a [randstring] usable in {@link #generate(String)}, false
     *         otherwise.
     * @since 1.0.0
     */
    public static boolean isRandomIntegerKeyword(final String targetString) {
        return RANDINT_KEYWORD_PATTERN.matcher(StringUtils.strip(targetString)).matches();
    }

    private static long getRange(final long minValue, final long maxValue) {
        return maxValue - minValue;
    }

    private static String getRangeString(final String parameterString) {
        final Matcher rangeModifierMatcher = RANGE_MODIFIER_PATTERN.matcher(parameterString);
        if (rangeModifierMatcher.find()) {
            final Matcher rangeMatcher = Pattern.compile("[-]?[0-9]+ *: *[-]?[0-9]+")
                    .matcher(rangeModifierMatcher.group());
            Validate.isTrue(rangeMatcher.find(), "invalid range modifier format in " + parameterString);
            return rangeMatcher.group();
        }
        return Integer.MIN_VALUE + ":" + Integer.MAX_VALUE;
    }
}
