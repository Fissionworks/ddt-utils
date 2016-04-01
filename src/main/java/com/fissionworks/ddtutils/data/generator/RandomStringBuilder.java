package com.fissionworks.ddtutils.data.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * RandomStringBuilder allows the creation of random strings of various composition based on parameters set
 * using the Builder pattern, or more commonly from a keyword string that specifies the desired random string
 * composition. If {@link #build()} is called without setting any parameters, the default behavior is to
 * generate an alphanumeric string with a length of {@value #DEFAULT_LENGTH}.
 *
 * @since 1.0.0
 *
 */
public final class RandomStringBuilder {
    /**
     * The string length created if neither {@link #length(int)} or {@link #length(int, int)} are called prior
     * to using {@link #build()}.
     *
     * @since 1.0.0
     */
    public static final int DEFAULT_LENGTH = 10;
    /**
     * The set of lowercase letters set using {@link #lowercase()}.
     *
     * @since 1.0.0
     */
    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    /**
     * The set of numbers set using {@link #numeric()}.
     *
     * @since 1.0.0
     */
    public static final String NUMBERS = "0123456789";
    /**
     * The set of numbers set using {@link #specialChars()}.
     *
     * @since 1.0.0
     */
    public static final String SPECIAL_CHARACTERS = "~!@#$%^&*()_+`-={}|[]\\:\";'<>?,./";

    /**
     * The set of uppercase letters set using {@link #uppercase()}.
     *
     * @since 1.0.0
     */
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Pattern LENGTH_PARAMETER_PATTERN = Pattern.compile("\\{ *length *= *[0-9]+ *(- *[0-9]+)? * *}",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern LENGTH_VALUE_PATTERN = Pattern.compile("[0-9]+ *(- *[0-9]+)?",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern LOWERCASE_PARAMETER_PATTERN = Pattern.compile("\\{ *lowercase *}",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern NUMERIC_PARAMETER_PATTERN = Pattern.compile("\\{ *numeric *}",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern RANDSTRING_KEYWORD_PATTERN = Pattern.compile("^\\[ *randstring.*\\]$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern SOURCE_CHARS_PARAMETER = Pattern.compile("\\{ *include *= *\\[.*\\] *}",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern SPACES_PARAMETER_PATTERN = Pattern.compile("\\{ *spaces *}", Pattern.CASE_INSENSITIVE);

    private static final Pattern SPECIAL_CHARS_PARAMETER_PATTERN = Pattern.compile("\\{ *specialchars *}",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern UPPERCASE_PARAMETER_PATTERN = Pattern.compile("\\{ *uppercase *}",
            Pattern.CASE_INSENSITIVE);

    private int length = DEFAULT_LENGTH;

    private boolean lengthSet = false;

    private final StringBuilder sourceChars = new StringBuilder();

    /**
     * Generates a random string based on a specifically formatted keyword string that would generally be
     * stored in a spreadsheet, database, etc. as part of a data driven test set. The keyword string mirrors
     * the parameters that can be set when using the RandomStringBuilder with the builder pattern and
     * {@link #build()}. The format for the parameterString argument is to include the main '[randstring]'
     * keyword enclosing any number of other keywords to specify the desired configuration of the random
     * string to be generated (i.e [randstring{uppercase}{length=27}], [randstring{numeric}{specialchars}]).
     * All keywords are case insensitive, and have built in tolerance for leading/trailing whitespace. The
     * following details the parameterString keywords:
     * <ul>
     * <li>[randstring] - The main keyword for generating a random string, which is the minimum required
     * argument to the generate method and encloses the other formatting keywords. Passing in [randstring]
     * with no other keywords is equivalent to calling {@link #build()} without setting any parameters first.
     * </li>
     * <li>{uppercase} - includes uppercase letters in the list of characters to use when generating the
     * random string; equivalent to calling {@link #uppercase()} in the builder pattern. example usage:
     * [randstring{uppercase}]</li>
     * <li>{lowercase} - includes lowercase letters in the list of characters to use when generating the
     * random string; equivalent to calling {@link #lowercase()} in the builder pattern. example usage:
     * [randstring{lowercase}]</li>
     * <li>{numeric} - includes numbers(0-9) in the list of characters to use when generating the random
     * string; equivalent to calling {@link #numeric()} in the builder pattern. example usage:
     * [randstring{numeric}]</li>
     * <li>{spaces} - includes spaces in the list of characters to use when generating the random string;
     * equivalent to calling {@link #spaces()} in the builder pattern. example usage: [randstring{spaces}]
     * </li>
     * <li>{specialchars} - includes special characters in the list of characters to use when generating the
     * random string; equivalent to calling {@link #specialChars()} in the builder pattern. example usage:
     * [randstring{specialchars}]</li>
     * <li>{length=x},{length=x-x} - sets the desired length of the random string either as a specific length
     * or length range, depending on the value provided. equivalent to calling {@link #length(int)} or
     * {@link #length(int, int)} in the builder pattern. The {length} keyword can only be used once in any
     * given parameterString. example usage: [randstring{length=55}] or [randstring{length=3-9}]</li>
     * <li>{include=[xxx]} - includes all characters between the square brackets in the list of characters to
     * use when generating the random string; equivalent to calling {@link #include(String)} in the builder
     * pattern. example usage: [randstring{include=[abc123]}]</li>
     * </ul>
     *
     * @param parameterString
     *            A correctly formatted [randstring] with any number of enclosed keywords.
     * @return A random string with a format based on the passed in parameterString.
     * @since 1.0.0
     */
    public static String generate(final String parameterString) {
        Validate.isTrue(isRandomStringKeyword(parameterString),
                "'" + parameterString + "' must contain the '[randstring]' keyword");
        validateParameterString(parameterString);
        final String strippedString = StringUtils.strip(parameterString);
        final RandomStringBuilder randStringBuilder = new RandomStringBuilder();
        randStringBuilder.sourceChars
                .append(UPPERCASE_PARAMETER_PATTERN.matcher(strippedString).find() ? UPPERCASE : "");
        randStringBuilder.sourceChars
                .append(LOWERCASE_PARAMETER_PATTERN.matcher(strippedString).find() ? LOWERCASE : "");
        randStringBuilder.sourceChars.append(NUMERIC_PARAMETER_PATTERN.matcher(strippedString).find() ? NUMBERS : "");
        randStringBuilder.sourceChars.append(SPACES_PARAMETER_PATTERN.matcher(strippedString).find() ? " " : "");
        randStringBuilder.sourceChars
                .append(SPECIAL_CHARS_PARAMETER_PATTERN.matcher(strippedString).find() ? SPECIAL_CHARACTERS : "");
        randStringBuilder.sourceChars
                .append(SOURCE_CHARS_PARAMETER.matcher(strippedString).find() ? getSourceChars(strippedString) : "");
        randStringBuilder.length = getLength(strippedString);
        return randStringBuilder.build();
    }

    /**
     * Checks if the passed in String is a valid parameter string as required by {@link #generate(String)}.
     * Leading/trailing whitespace is ignored, but false is returned if there are any extra characters
     * surrounding the passed in targetString. example: '[randstring{uppercase}{spaces}]' would return true,
     * but 'foo[randstring{lowercase}]bar' returns false. The structure of the internal keywords ({uppercase},
     * {numeric}, etc.) is not evaluated.
     *
     * @param targetString
     *            The string to determine status of.
     * @return True if the targetString is a [randstring] usable in {@link #generate(String)}, false
     *         otherwise.
     * @since 1.0.0
     */
    public static boolean isRandomStringKeyword(final String targetString) {
        return RANDSTRING_KEYWORD_PATTERN.matcher(StringUtils.strip(targetString)).matches();
    }

    private static int calculateLength(final int minLength, final int maxLength) {
        Validate.isTrue(minLength >= 1, "minLength must be >= 1");
        Validate.isTrue(maxLength >= minLength, "Maxlength must be >= minlength");
        return minLength + (int) (Math.random() * ((maxLength - minLength) + 1));
    }

    private static int getLength(final String strippedString) {
        final Matcher matcher = LENGTH_PARAMETER_PATTERN.matcher(strippedString);
        if (matcher.find()) {
            final Matcher valueMatcher = LENGTH_VALUE_PATTERN.matcher(matcher.group());
            valueMatcher.find();
            final String[] lengths = valueMatcher.group().split("-");
            if (lengths.length == 1) {
                return Integer.parseInt(lengths[0].trim());
            } else {
                return calculateLength(Integer.parseInt(lengths[0].trim()), Integer.parseInt(lengths[1].trim()));
            }
        }
        return DEFAULT_LENGTH;
    }

    private static int getLengthParameterCount(final Matcher matcher) {
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static String getSourceChars(final String strippedString) {
        final Matcher matcher = SOURCE_CHARS_PARAMETER.matcher(strippedString);
        final Pattern sourceStringPattern = Pattern.compile("(?<=\\[)(.*)(?=\\] *})");
        matcher.find();
        final Matcher sourceStringMatcher = sourceStringPattern.matcher(matcher.group());
        sourceStringMatcher.find();
        Validate.isTrue(sourceStringMatcher.group().length() > 0,
                "sourcechars keyword must contain at least one character in source character set");
        return sourceStringMatcher.group();

    }

    private static void validateParameterString(final String parameterString) {
        if (getLengthParameterCount(LENGTH_PARAMETER_PATTERN.matcher(parameterString)) > 1) {
            throw new IllegalArgumentException("only one length parameter is allowed per [randstring]");
        }
        String reducedString = LENGTH_PARAMETER_PATTERN.matcher(parameterString).replaceAll(" ");
        reducedString = LOWERCASE_PARAMETER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = NUMERIC_PARAMETER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = SPACES_PARAMETER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = SPECIAL_CHARS_PARAMETER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = UPPERCASE_PARAMETER_PATTERN.matcher(reducedString).replaceAll(" ");
        reducedString = SOURCE_CHARS_PARAMETER.matcher(reducedString).replaceAll(" ");
        if (!Pattern.compile("^\\[ *randstring *\\]$", Pattern.CASE_INSENSITIVE).matcher(reducedString).matches()) {
            throw new IllegalArgumentException(
                    "Invalid parameter string; The following is the parameter string with the valid keywords removed: "
                            + reducedString);
        }
    }

    /**
     * Creates a randomized string based on all previously set parameters. If no parameters are set, the
     * default behavior is to generate an alphanumeric string with the a length of {@value #DEFAULT_LENGTH}
     *
     * @return A randomized string based on the parameters that have been set.
     * @since 1.0.0
     */
    public String build() {
        final String sourceString = sourceChars.length() == 0 ? LOWERCASE + UPPERCASE + NUMBERS
                : sourceChars.toString();
        return RandomStringUtils.random(length, 0, sourceString.length(), false, false, sourceString.toCharArray());
    }

    /**
     * Convenience method that clears all set parameters after building the random string.
     *
     * @return A randomized string based on the parameters that have been set.
     * @since 1.0.0
     */
    public String buildAndClear() {
        final String randomString = build();
        clear();
        return randomString;
    }

    /**
     * Clears all previously set parameters ({@link #lowercase()}, {@link #numeric()}, etc.), restoring the
     * RandomStringBuilder to its initial default state.
     *
     * @since 1.0.0
     */
    public void clear() {
        sourceChars.setLength(0);
        lengthSet = false;
    }

    /**
     * Adds the passed in characters to the list of available characters to use when generating a random
     * string. Note that 'include' implies these characters will only be included as possible characters in
     * the final string, and there is no guarantee that any/all of these characters will appear in the
     * generated string.
     *
     * @param sourceCharacters
     *            The characters desired to possibly appear in the generated random string.
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder include(final String sourceCharacters) {
        sourceChars.append(sourceCharacters);
        return this;
    }

    /**
     * Sets an exact length for the random string to be generated. Length must be greater than zero and it
     * cannot be set multiple times, or after already specifying a length range using
     * {@link #length(int, int)}.
     *
     * @param desiredLength
     *            The exact length desired for the random string.
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder length(final int desiredLength) {
        Validate.isTrue(desiredLength > 0, "desiredLength must be > 0");
        Validate.isTrue(!lengthSet,
                "length(int desiredLength) and/or length(int min,int max) can only be specified once");
        length = desiredLength;
        lengthSet = true;
        return this;
    }

    /**
     * Sets a length range between which the length of the generated string will randomly fall. The minimum
     * length must be greater than or equal to one, and the max length must be greater than or equal to the
     * min length . This method will generate an {@link IllegalArgumentException} if called multiple times or
     * after already setting an exact length using {@link #length(int)}.
     *
     * @param minLength
     *            The minimum length desired for the random string. Must be greater than or equal to 1.
     * @param maxLength
     *            The maximum length desired for the random string. Must be greater than or equal to the min
     *            length.
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder length(final int minLength, final int maxLength) {
        Validate.isTrue(!lengthSet,
                "length(int desiredLength) and/or length(int min,int max) can only be specified once");
        length = calculateLength(minLength, maxLength);
        lengthSet = true;
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow lowercase alphabetic characters in the string that is
     * built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder lowercase() {
        sourceChars.append(LOWERCASE);
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow numeric characters in the string that is built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder numeric() {
        sourceChars.append(NUMBERS);
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow spaces in the string that is built. Note that the
     * generated string may begin or end with spaces so for cases in which the string desired should not begin
     * or end with blank space a trim operation on the returned string is required.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder spaces() {
        sourceChars.append(" ");
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow special characters in the string that is built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder specialChars() {
        sourceChars.append(SPECIAL_CHARACTERS);
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow uppercase alphabetic characters in the string that is
     * built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder uppercase() {
        sourceChars.append(UPPERCASE);
        return this;
    }

}
