package com.fissionworks.ddtutils.data.generator;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RandomStringBuilderTest {

    @Test
    public void build_withIncludeSpecified_shouldCreateDefaultLengthStringFromSourceChars() {
        final String sourceChars = "abc123[]\\";
        final String actualString = new RandomStringBuilder().include(sourceChars).build();
        Assert.assertTrue(StringUtils.containsOnly(actualString, sourceChars));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

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
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void build_withMinLengthOneAndGreaterMaxLength_shouldGenerateAlphanumericStringInRange() {
        final int minLength = 1;
        final int maxLength = 4;
        // Generate and test multiple strings to increase probability that all length possibilities
        // created/tested
        for (int i = 0; i < 20; i++) {
            final String actualString = new RandomStringBuilder().length(minLength, maxLength).build();
            Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
            Assert.assertTrue(actualString.length() >= minLength && actualString.length() <= maxLength,
                    "Generated string was outside the the minLength/maxLength range");
        }
    }

    @Test
    public void build_withNoParametersSet_shouldCreateDefaultLengthAlphanumericString() {
        final String actualString = new RandomStringBuilder().build();
        Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void build_withNumericSet_shouldCreateDefaultLengthNumericString() {
        final String actualString = new RandomStringBuilder().numeric().build();
        Assert.assertTrue(StringUtils.isNumeric(actualString), actualString);
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void build_withSpacesSet_shouldCreateDefaultLengthStringOfSpaces() {
        final String actualString = new RandomStringBuilder().spaces().build();
        Assert.assertTrue(StringUtils.isBlank(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void build_withSpecialCharactersSet_shouldCreateDefaultLengthSpecialCharacterString() {
        final String actualString = new RandomStringBuilder().specialChars().build();
        Assert.assertTrue(StringUtils.containsOnly(actualString, RandomStringBuilder.SPECIAL_CHARACTERS));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void build_withUppercaseSet_shouldCreateDefaultLengthLowercaseAlphaString() {
        final String actualString = new RandomStringBuilder().uppercase().build();
        Assert.assertTrue(StringUtils.isAlpha(actualString), actualString);
        Assert.assertTrue(StringUtils.isAllUpperCase(actualString), actualString);
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);

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

    @Test
    public void generate_withAllKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder
                .generate("[randstring{uppercase}{lowercase}{numeric}{specialchars}{spaces}{length=22-75}]");
        Assert.assertTrue(22 <= actual.length() && 75 >= actual.length());
    }

    @Test
    public void generate_withEmptyParameterString_shouldCreateDefaultLengthAlphanumericString() {
        final String actualString = RandomStringBuilder.generate("[randString]");
        Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withEmptySourceCharsKeyword_shouldThrowException() {
        RandomStringBuilder.generate("[randstring{sourcechars=[]}]");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withInvalidCharacters_shouldThrowException() {
        RandomStringBuilder.generate("[randstring{lowercase}x{uppercase}]");
    }

    @Test
    public void generate_withLengthKeywordExactLength_shouldSetLengthParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{length=19}]");
        Assert.assertTrue(StringUtils.isAlphanumeric(actual));
        Assert.assertEquals(actual.length(), 19);
    }

    @Test
    public void generate_withLengthKeywordExactLengthInternalWhitespace_shouldSetLengthParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{  length   =   19  }]");
        Assert.assertTrue(StringUtils.isAlphanumeric(actual));
        Assert.assertEquals(actual.length(), 19);
    }

    @Test
    public void generate_withLengthKeywordExactLengthMixedCase_shouldSetLengthParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{LeNgTh=19}]");
        Assert.assertTrue(StringUtils.isAlphanumeric(actual));
        Assert.assertEquals(actual.length(), 19);
    }

    @Test
    public void generate_withLengthKeywordLengthRange_shouldSetLengthParameter() {
        // Generate and test multiple strings to increase probability that all length possibilities
        // created/tested
        for (int i = 0; i < 20; i++) {
            final String actualString = RandomStringBuilder.generate("[randstring{length=1-4}]");
            Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
            Assert.assertTrue(actualString.length() >= 1 && actualString.length() <= 4,
                    "Generated string was outside the the minLength/maxLength range");
        }
    }

    @Test
    public void generate_withLengthKeywordLengthRangeInternalWhitespace_shouldSetLengthParameter() {
        // Generate and test multiple strings to increase probability that all length possibilities
        // created/tested
        for (int i = 0; i < 20; i++) {
            final String actualString = RandomStringBuilder.generate("[randstring{  length  =  1 -   4 }]");
            Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
            Assert.assertTrue(actualString.length() >= 1 && actualString.length() <= 4,
                    "Generated string was outside the the minLength/maxLength range");
        }
    }

    @Test
    public void generate_withLengthKeywordLengthRangeMixedCase_shouldSetLengthParameter() {
        // Generate and test multiple strings to increase probability that all length possibilities
        // created/tested
        for (int i = 0; i < 20; i++) {
            final String actualString = RandomStringBuilder.generate("[randstring{LeNgTh=1-4}]");
            Assert.assertTrue(StringUtils.isAlphanumeric(actualString));
            Assert.assertTrue(actualString.length() >= 1 && actualString.length() <= 4,
                    "Generated string was outside the the minLength/maxLength range");
        }
    }

    @Test
    public void generate_withLowercaseKeyword_shouldSetLowercaseParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{lowercase}]");
        Assert.assertTrue(StringUtils.isAlpha(actual));
        Assert.assertTrue(StringUtils.isAllLowerCase(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withLowercaseKeywordLeadingTrailingWhitespace_shouldSetLowercaseParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{   lowercase  }]");
        Assert.assertTrue(StringUtils.isAlpha(actual));
        Assert.assertTrue(StringUtils.isAllLowerCase(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withLowercaseKeywordMixedCase_shouldSetLowercaseParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{lOwErCaSe}]");
        Assert.assertTrue(StringUtils.isAlpha(actual));
        Assert.assertTrue(StringUtils.isAllLowerCase(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withMultipleKeyword_shouldGenerateUsingKeywords() {
        final String sourceChars = "[]{}";
        final String actual = RandomStringBuilder.generate(
                "[randstring{include=[" + sourceChars + "]}{lowercase}{uppercase}{numeric}{spaces}{length=17}]");
        Assert.assertTrue(StringUtils.containsAny(actual, " " + sourceChars + RandomStringBuilder.LOWERCASE
                + RandomStringBuilder.UPPERCASE + RandomStringBuilder.NUMBERS));
        Assert.assertEquals(actual.length(), 17);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withMultipleLengthKeywords_shouldThrowException() {
        RandomStringBuilder.generate("[randstring{length=1}{length=1-9}]");
    }

    @Test
    public void generate_withMultipleLowercaseKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder.generate("[randstring{lowercase}{lowercase}{uppercase}{lowercase}]");
        Assert.assertTrue(StringUtils.isAlpha(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withMultipleNumericKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder.generate("[randstring{numeric}{lowercase}{numeric}{uppercase}]");
        Assert.assertTrue(StringUtils.isAlphanumeric(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withMultipleSpaceKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder
                .generate("[randstring{spaces}{lowercase}{spaces}{numeric}{uppercase}]");
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withMultipleSpecialCharKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder
                .generate("[randstring{specialchars}{lowercase}{specialchars}{numeric}{uppercase}]");
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withMultipleUppercaseKeywords_shouldGenerateSucessfully() {
        final String actual = RandomStringBuilder.generate("[randstring{uppercase}{lowercase}{uppercase}{uppercase}]");
        Assert.assertTrue(StringUtils.isAlpha(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withNumericKeyword_shouldSetNumericParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{numeric}]");
        Assert.assertTrue(StringUtils.isNumeric(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withNumericKeywordLeadingTrailingWhitespace_shouldSetNumericParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{   numeric  }]");
        Assert.assertTrue(StringUtils.isNumeric(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withNumericKeywordMixedCase_shouldSetNumericParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{nUmErIc}]");
        Assert.assertTrue(StringUtils.isNumeric(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withoutRandStringKeyword_shouldThrowExeption() {
        RandomStringBuilder.generate("[notarandstring]");
    }

    @Test
    public void generate_withSourceCharsKeyword_shouldGenerateUsingSourceChars() {
        final String sourceChars = "abc123]}";
        final String actual = RandomStringBuilder.generate("[randstring{include=[" + sourceChars + "]}]");
        Assert.assertTrue(StringUtils.containsOnly(actual, sourceChars));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpacesKeyword_shouldSetSpacesParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{spaces}]");
        Assert.assertTrue(StringUtils.isBlank(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpacesKeywordLeadingTrailingWhitespace_shouldSetSpacesParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{   spaces  }]");
        Assert.assertTrue(StringUtils.isBlank(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpacesKeywordMixedCase_shouldSetSpacesParameter() {
        final String actual = RandomStringBuilder.generate("[randstring{sPaCeS}]");
        Assert.assertTrue(StringUtils.isBlank(actual));
        Assert.assertEquals(actual.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpecialCharsKeyword_shouldSetSpecialCharsParameter() {
        final String actualString = RandomStringBuilder.generate("[randstring{specialchars}]");
        Assert.assertTrue(StringUtils.containsOnly(actualString, RandomStringBuilder.SPECIAL_CHARACTERS));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpecialCharsKeywordLeadingTrailingWhitespace_shouldSetSpecialCharactersParameter() {
        final String actualString = RandomStringBuilder.generate("[randstring{   specialchars  }]");
        Assert.assertTrue(StringUtils.containsOnly(actualString, RandomStringBuilder.SPECIAL_CHARACTERS));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withSpecialCharsKeywordMixedCase_shouldSetSpecialCharsParameter() {
        final String actualString = RandomStringBuilder.generate("[randstring{sPeCiAlChArS}]");
        Assert.assertTrue(StringUtils.containsOnly(actualString, RandomStringBuilder.SPECIAL_CHARACTERS));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
    }

    @Test
    public void generate_withUppercaseKeywordInParameterString_shouldCreateDefaultLengthUppercaseString() {
        final String actualString = RandomStringBuilder.generate("[randString{uppercase}]");
        Assert.assertTrue(StringUtils.isAlpha(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
        Assert.assertTrue(StringUtils.isAllUpperCase(actualString));
    }

    @Test
    public void generate_withUppercaseKeywordLeadingTrailingWhitespace_shouldSetUppercaseParameter() {
        final String actualString = RandomStringBuilder.generate("[randstring{   uppercase  }]");
        Assert.assertTrue(StringUtils.isAlpha(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
        Assert.assertTrue(StringUtils.isAllUpperCase(actualString));
    }

    @Test
    public void generate_withUppercaseKeywordMixedCase_shouldSetUppercaseParameter() {
        final String actualString = RandomStringBuilder.generate("[randstring{uPpErCaSe}]");
        Assert.assertTrue(StringUtils.isAlpha(actualString));
        Assert.assertEquals(actualString.length(), RandomStringBuilder.DEFAULT_LENGTH);
        Assert.assertTrue(StringUtils.isAllUpperCase(actualString));
    }

    @Test
    public void isRandomStringKeyword_withKeywordStringWithInternalWhitespace_shouldReturnTrue() {
        Assert.assertTrue(RandomStringBuilder.isRandomStringKeyword("    [    randstring   {xxxxx}]   "));
    }

    @Test
    public void isRandomStringKeyword_withKeywordStringWithLeadingTrailingWhitespace_shouldReturnTrue() {
        Assert.assertTrue(RandomStringBuilder.isRandomStringKeyword("    [randstring{xxxxx}]   "));
    }

    @Test
    public void isRandomStringKeyword_withKeywordStringWithMixedCase_shouldReturnTrue() {
        Assert.assertTrue(RandomStringBuilder.isRandomStringKeyword("[RaNdStRiNg{xxxxx}]"));
    }

    @Test
    public void isRandomStringKeyword_withNonKeywordString_shouldReturnFalse() {
        Assert.assertFalse(RandomStringBuilder.isRandomStringKeyword("[notarandstring{xxxxx}]"));
    }

    @Test
    public void isRandomStringKeyword_withSimpleKeywordString_shouldReturnTrue() {
        Assert.assertTrue(RandomStringBuilder.isRandomStringKeyword("[randstring{xxxxx}]"));
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
