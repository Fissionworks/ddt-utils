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

    private static final Pattern EVEN_MODIFIER_PATTERN = Pattern.compile("\\{ *even *\\}", Pattern.CASE_INSENSITIVE);

    private static final Pattern ODD_MODIFIER_PATTERN = Pattern.compile("\\{ *odd *\\}", Pattern.CASE_INSENSITIVE);

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

    /**
     * Creates a random integer based on any modifiers included in the passed in parameters string. The
     * randint keyword and modifiers have built in tolerance for leading/trailing whitespace where practical,
     * and are generally case insensitive. The format required to generate a random integer includes the
     * following keyword and optional modifiers:
     *
     * <ul>
     * <li>[randint] - The main keyword for generating a random integer which is the minimum required argument
     * to the generate method and encloses the other modifiers. Passing in [randint] with no other modifiers
     * generates a random integer between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}</li>
     * <li>{range=x:y} - Range modifier that specifies a range between which the generated random integers
     * should fall with 'x' being the minimum desired value and 'y' being the maximum desired value. (example
     * usage: [randint{range=-20:347}]</li>
     * <li>{even} - Modifier that the generated random number should be even (example usage: [randint{even}];
     * can be used with the {range} modifier, but cannot be used with the {odd} modifier.</li>
     * <li>{odd} - Modifier that the generated random number should be odd (example usage: [randint{odd}]; can
     * be used with the {range} modifier, but cannot be used with the {even} modifier.</li>
     * </ul>
     * Examples:
     * <ul>
     * <li>[randint{range=7-15}] - generates a random number between 7 and 15 (inclusive)</li>
     * <li>[randint{range=7-15}{even}] - generates an even random number between 7 and 15 (inclusive if the
     * range limit is even)</li>
     * <li>[randint{range=1-18}{odd}] - generates an odd random number between 1 and 18 (inclusive if the
     * range limit is odd)</li>
     * </ul>
     *
     *
     * @param parameterString
     *            The parameter string with desired random integer modifier(s).
     * @return A random integer based on the passed in parameter string.
     * @since 1.0.0
     */
    public static int generate(final String parameterString) {
        final String rangeString = getRangeString(parameterString);
        final String[] minMaxArray = rangeString.split(":");
        final boolean isEven = EVEN_MODIFIER_PATTERN.matcher(parameterString).find();
        final boolean isOdd = ODD_MODIFIER_PATTERN.matcher(parameterString).find();
        Validate.isTrue(!(isEven && isOdd), "Cannot use both {even} and {odd} in same parameter string");
        if (isEven) {
            return RandomIntegerUtils.generateEven(Integer.parseInt(minMaxArray[0].trim()),
                    Integer.parseInt(minMaxArray[1].trim()));
        }
        if (isOdd) {
            return RandomIntegerUtils.generateOdd(Integer.parseInt(minMaxArray[0].trim()),
                    Integer.parseInt(minMaxArray[1].trim()));
        }
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
