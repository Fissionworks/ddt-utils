package com.fissionworks.ddtutils.data.creation;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

/**
 * RandomStringBuilder allows the creation of random strings of various composition based on parameters set using the
 * Builder pattern. If {@link #build()} is called without setting any parameters, the default behavior is to generate an
 * alphanumeric string with a length of {@value #DEAFULT_LENGTH}.
 *
 * @since 1.0.0
 *
 */
public final class RandomStringBuilder {
	/**
	 * The string length created if neither {@link #length(int)} or {@link #length(int, int)} are called.
	 *
	 * @since 1.0.0
	 */
	public static final int DEAFULT_LENGTH = 10;

	private boolean alphabetic = false;

	private int length = DEAFULT_LENGTH;

	private boolean lengthSet = false;

	/**
	 * Sets the {@link RandomStringBuilder} to include alphabetic characters in the string that {@link #build()}
	 * returns.
	 *
	 * @return Returns "this" as part of the builder pattern.
	 * @since 1.0.0
	 */
	public RandomStringBuilder alphabetic() {
		alphabetic = true;
		return this;

	}

	/**
	 * Creates a randomized string based on all previously set parameters. If no parameters are set, the default
	 * behavior is to generate an alphanumeric string with the a length of {@value #DEAFULT_LENGTH}
	 *
	 * @return A randomized string based on the parameters that have been set.
	 * @since 1.0.0
	 */
	public String build() {
		String randomString;
		if (alphabetic) {
			randomString = RandomStringUtils.randomAlphabetic(length);
		} else {
			randomString = RandomStringUtils.randomAlphanumeric(length);
		}
		return randomString;
	}

	/**
	 * Sets an exact length for the random string to be generated. Length must be greater than zero and cannot be set
	 * multiple times, or after already specifying a length range using {@link #length(int, int)}.
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
	 * Sets a length range between which the length of the generated string will randomly fall. The minimum length must
	 * be greater than or equal to zero, and the max length must be greater than or equal to the min length (or greater
	 * than the min length if the min length is zero). This method will generate an {@link IllegalArgumentException} if
	 * called multiple times or after already setting an exact length using {@link #length(int)}.
	 *
	 * @param minLength
	 *            The minimum length desired for the random string. Must be greater than or equal to 0.
	 * @param maxLength
	 *            The maximum length desired for the random string. Must be greater than or equal to the min length, or
	 *            greater than min length if min length is 0.
	 * @return Returns "this" as part of the builder pattern.
	 * @since 1.0.0
	 */
	public RandomStringBuilder length(final int minLength, final int maxLength) {
		Validate.isTrue(maxLength >= minLength, "Maxlength must be >= minlength");
		Validate.isTrue(minLength >= 0 && maxLength > 0, "minLength must be >= 0 and maxLength must be > 0");
		Validate.isTrue(!lengthSet,
				"length(int desiredLength) and/or length(int min,int max) can only be specified once");
		length = minLength + (int) (Math.random() * ((maxLength - minLength) + 1));
		lengthSet = true;
		return this;
	}

}
