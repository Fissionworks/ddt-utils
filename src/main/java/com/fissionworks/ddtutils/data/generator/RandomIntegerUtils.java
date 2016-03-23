package com.fissionworks.ddtutils.data.generator;

import org.apache.commons.lang3.Validate;

/**
 * RandomIntegerUtils allows the creation of a random integer, wrapping common random number generation
 * calculations for cleaner code.
 *
 * @since 1.0.0
 *
 */
public final class RandomIntegerUtils {

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

    private static long getRange(final long minValue, final long maxValue) {
        return maxValue - minValue;
    }
}
