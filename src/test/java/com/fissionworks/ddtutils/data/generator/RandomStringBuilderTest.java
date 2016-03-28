package com.fissionworks.ddtutils.data.generator;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RandomStringBuilderTest {

    @Test
    public void build_withLengthSet_shouldCreateAlphanumericStringOfGivenLength() {
        final int expectedLength = 42;
        final String actualString = new RandomStringBuilder().length(expectedLength).build();
        Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
        Assert.assertEquals(actualString.length(), expectedLength);
    }

    @Test
    public void build_withLowercaseSet_shouldCreateDefaultLengthLowercaseAlphaString() {
        final String actualString = new RandomStringBuilder().lowercase().build();
        Assert.assertTrue(StringUtils.isAlpha(actualString), actualString);
        Assert.assertTrue(StringUtils.isAllLowerCase(actualString), actualString);
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);
    }

    @Test
    public void build_withMinLengthZeroAndNonZeroMaxLength_shouldGenerateAlphanumericStringInRange() {
        final int minLength = 1;
        final int maxLength = 4;
        // Generate and test multiple strings to increase probability that all length possibilities
        // created/tested
        for (int i = 0; i < 20; i++) {
            final String actualString = new RandomStringBuilder().length(minLength, maxLength).build();
            Assert.assertTrue(StringUtils.isAlphanumeric(actualString) || StringUtils.isEmpty(actualString));
            Assert.assertTrue(actualString.length() >= minLength && actualString.length() <= maxLength,
                    "Generated string was outside the the minLength/maxLength range");
        }
    }

    @Test
    public void build_withNoParametersSet_shouldCreateDefaultLengthAlphanumericString() {
        final String actualString = new RandomStringBuilder().build();
        Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);
    }

    @Test
    public void build_withNumericSet_shouldCreateDefaultLengthNumericString() {
        final String actualString = new RandomStringBuilder().numeric().build();
        Assert.assertTrue(StringUtils.isNumeric(actualString), actualString);
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);
    }

    @Test
    public void build_withSpacesSet_shouldCreateDefaultLengthStringOfSpaces() {
        final String actualString = new RandomStringBuilder().spaces().build();
        Assert.assertTrue(StringUtils.isBlank(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);
    }

    @Test
    public void build_withSpecialCharactersSet_shouldCreateDefaultLengthSpecialCharacterString() {
        final String actualString = new RandomStringBuilder().specialCharacters().build();
        Assert.assertTrue(StringUtils.containsOnly(actualString, RandomStringBuilder.SPECIAL_CHARACTERS));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);
    }

    @Test
    public void build_withUppercaseSet_shouldCreateDefaultLengthLowercaseAlphaString() {
        final String actualString = new RandomStringBuilder().uppercase().build();
        Assert.assertTrue(StringUtils.isAlpha(actualString), actualString);
        Assert.assertTrue(StringUtils.isAllUpperCase(actualString), actualString);
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEAFULT_LENGTH);

    }

    @Test
    public void buildAndClear_withParametersSet_shouldClearAllSetParamters() {
        final int initialLength = 20;
        final int secondLength = 12;
        final RandomStringBuilder builder = new RandomStringBuilder().lowercase().uppercase().length(initialLength);
        final String firstString = builder.buildAndClear();
        Assert.assertTrue(StringUtils.isAlpha(firstString), firstString);
        Assert.assertEquals(firstString.length(), initialLength);
        final String secondString = builder.numeric().length(secondLength).build();
        Assert.assertTrue(StringUtils.isNumeric(secondString), secondString);
        Assert.assertEquals(secondString.length(), secondLength);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildFromString_withEmptySourceString_shouldThrowException() {
        RandomStringBuilder.buildFromString("", 5, 10);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildFromString_withMaxLengthLessThanMinLength_shouldThrowException() {
        RandomStringBuilder.buildFromString("abc123", 11, 10);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildFromString_withNegativeMinLength_shouldThrowException() {
        RandomStringBuilder.buildFromString("xys", -1, 2);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void buildFromString_withNullSourceString_shouldThrowException() {
        RandomStringBuilder.buildFromString(null, 5, 10);
    }

    @Test
    public void buildFromString_withValidArguments_shouldGenerateStringUsingSpecifiedValues() {
        final int min = 3;
        final int max = 7;
        final String sourceString = "abc123!@#";
        String actual = "";
        // Generate and test multiple strings to increase probability that all length/character possibilities
        // are created/tested
        for (int i = 0; i < 20; i++) {
            actual = RandomStringBuilder.buildFromString(sourceString, min, max);
            Assert.assertNotNull(actual);
            Assert.assertTrue(min <= actual.length() && actual.length() <= max,
                    "Generated string length should be between min and max values");
            Assert.assertTrue(StringUtils.containsOnly(actual, sourceString),
                    "Generated string contains unspecified characters");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildFromString_withZeroMinLength_shouldThrowException() {
        RandomStringBuilder.buildFromString("xys", 0, 2);
    }

    @Test
    public void clear_afterSettingParameters_shouldClearAllSetParameters() {
        final int initialLength = 20;
        final int secondLength = 12;
        final RandomStringBuilder builder = new RandomStringBuilder().uppercase().lowercase().length(initialLength);
        final String firstString = builder.build();
        Assert.assertTrue(StringUtils.isAlpha(firstString), firstString);
        Assert.assertEquals(firstString.length(), initialLength);
        builder.clear();
        final String secondString = builder.numeric().length(secondLength).build();
        Assert.assertTrue(StringUtils.isNumeric(secondString), secondString);
        Assert.assertEquals(secondString.length(), secondLength);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withExactLengthAfterLengthRangeCall_shouldThrowException() {
        new RandomStringBuilder().length(1, 5).length(2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withExactLengthMultipleCalls_shouldThrowException() {
        new RandomStringBuilder().length(1).length(2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withLengthRangeAfterExactLengthCall_shouldThrowException() {
        new RandomStringBuilder().length(1).length(2, 100);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withLengthRangeMultipleCalls_shouldThrowException() {
        new RandomStringBuilder().length(1, 5).length(5, 10);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withMinLengthGreaterThanMaxLength_shouldThrowException() {
        new RandomStringBuilder().length(5, 4);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withMinLengthZero_shouldThrowException() {
        new RandomStringBuilder().length(0, 5);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withNegativeLength_shouldThrowException() {
        new RandomStringBuilder().length(-1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withNegativeMinLength_shouldThrowException() {
        new RandomStringBuilder().length(-1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void length_withZeroLength_shouldThrowException() {
        new RandomStringBuilder().length(0);
    }

}
