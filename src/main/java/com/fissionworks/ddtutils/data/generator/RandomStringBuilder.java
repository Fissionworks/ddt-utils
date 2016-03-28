package com.fissionworks.ddtutils.data.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

/**
 * RandomStringBuilder allows the creation of random strings of various composition based on parameters set
 * using the Builder pattern. If {@link #build()} is called without setting any parameters, the default
 * behavior is to generate an alphanumeric string with a length of {@value #DEAFULT_LENGTH}.
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
    public static final int DEAFULT_LENGTH = 10;
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
     * The set of numbers set using {@link #specialCharacters()}.
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

    private int length = DEAFULT_LENGTH;

    private boolean lengthSet = false;

    private boolean lowercase = false;

    private boolean numeric = false;

    private boolean spaces = false;

    private boolean specialCharacters = false;

    private boolean uppercase = false;

    /**
     * Allows building a random string based on the characters passed in as the source string, with a length
     * within the range between the min and max provided. Note that all characters are not guaranteed to be
     * used.
     *
     * @param sourceString
     *            The characters to select from when generating a random string.
     * @param minLength
     *            The minimum length string desired; must be greater than zero.
     * @param maxLength
     *            The maximum length string desired; must be greater than or equal to the min length.
     * @return A random string based on the arguments provided.
     * @since 1.0.0
     */
    public static String buildFromString(final String sourceString, final int minLength, final int maxLength) {
        Validate.notEmpty(sourceString, "sourceCharacters must not be null or empty.");
        Validate.isTrue(minLength > 0, "minLength must be greater than zero.");
        Validate.isTrue(maxLength >= minLength, "maxLength must be greater than or equal to minLength.");
        final int length = minLength + (int) (Math.random() * ((maxLength - minLength) + 1));
        return RandomStringUtils.random(length, 0, sourceString.length(), false, false, sourceString.toCharArray());
    }

    /**
     * Creates a randomized string based on all previously set parameters. If no parameters are set, the
     * default behavior is to generate an alphanumeric string with the a length of {@value #DEAFULT_LENGTH}
     *
     * @return A randomized string based on the parameters that have been set.
     * @since 1.0.0
     */
    public String build() {
        final String sourceString = getSourceString();
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
        lowercase = false;
        uppercase = false;
        length = DEAFULT_LENGTH;
        lengthSet = false;
        numeric = false;
        spaces = false;
        specialCharacters = false;
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
        Validate.isTrue(minLength >= 1, "minLength must be >= 1");
        Validate.isTrue(maxLength >= minLength, "Maxlength must be >= minlength");
        Validate.isTrue(!lengthSet,
                "length(int desiredLength) and/or length(int min,int max) can only be specified once");
        length = minLength + (int) (Math.random() * ((maxLength - minLength) + 1));
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
        this.lowercase = true;
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow numeric characters in the string that is built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder numeric() {
        numeric = true;
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
        spaces = true;
        return this;
    }

    /**
     * Sets the {@link RandomStringBuilder} to allow special characters in the string that is built.
     *
     * @return Returns "this" as part of the builder pattern.
     * @since 1.0.0
     */
    public RandomStringBuilder specialCharacters() {
        specialCharacters = true;
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
        this.uppercase = true;
        return this;
    }

    private String getSourceString() {
        final StringBuilder sb = new StringBuilder();
        if (lowercase) {
            sb.append(LOWERCASE);
        }
        if (uppercase) {
            sb.append(UPPERCASE);
        }
        if (numeric) {
            sb.append(NUMBERS);
        }
        if (spaces) {
            sb.append(" ");
        }
        if (specialCharacters) {
            sb.append(SPECIAL_CHARACTERS);
        }
        if (sb.length() == 0) {
            sb.append(LOWERCASE + UPPERCASE + NUMBERS);
        }
        return sb.toString();
    }

}
