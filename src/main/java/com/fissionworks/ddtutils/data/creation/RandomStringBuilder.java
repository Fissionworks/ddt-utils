package com.fissionworks.ddtutils.data.creation;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

public class RandomStringBuilder {

	public static final int DEAFULT_LENGTH = 10;

	private int length = DEAFULT_LENGTH;

	private boolean lengthSet = false;

	public String build() {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	public RandomStringBuilder length(final int desiredLength) {
		Validate.isTrue(desiredLength > 0, "desiredLength must be > 0");
		Validate.isTrue(!lengthSet,
				"length(int desiredLength) and/or length(int min,int max) can only be specified once");
		length = desiredLength;
		lengthSet = true;
		return this;
	}

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
