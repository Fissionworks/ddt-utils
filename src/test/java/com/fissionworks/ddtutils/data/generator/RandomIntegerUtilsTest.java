package com.fissionworks.ddtutils.data.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RandomIntegerUtilsTest {

    @Test
    public void generate_shouldReturnAnyRandomInteger() {
        final int actual = RandomIntegerUtils.generate();
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "generate should create a integer from full range of integers");
    }

    @Test
    public void generate_withKeywordNoModifiers_shouldReturnAnyRandomInteger() {
        final int actual = RandomIntegerUtils.generate("[randint]");
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "generate should create a integer from full range of integers");
    }

    @Test
    public void generate_withKeywordNoModifiersAndInternalWhitespace_shouldReturnAnyRandomInteger() {
        final int actual = RandomIntegerUtils.generate("[    randint     ]");
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "generate should create a integer from full range of integers");
    }

    @Test
    public void generate_withKeywordNoModifiersAndLeadingTrailingWhitespace_shouldReturnAnyRandomInteger() {
        final int actual = RandomIntegerUtils.generate("     [randint]     ");
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "generate should create a integer from full range of integers");
    }

    @Test
    public void generate_withKeywordNoModifiersAndMixedCase_shouldReturnAnyRandomInteger() {
        final int actual = RandomIntegerUtils.generate("[RaNdInT]");
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "generate should create a integer from full range of integers");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withMinValueGreaterThanMaxValue_shouldThrowException() {
        RandomIntegerUtils.generate(10, 9);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withMissingColon_shouldThrowException() {
        RandomIntegerUtils.generate(String.format("[randint{range=5}]"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void generate_withNonNumericRangeValues_shouldThrowException() {
        RandomIntegerUtils.generate(String.format("[randint{range=chicken:turtle}]"));
    }

    @Test
    public void generate_withValidRange_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = RandomIntegerUtils.generate(min, max);
            Assert.assertTrue(actual >= min && actual <= max, actual + ".");
        }
    }

    @Test
    public void generate_withValidRangeModifier_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = RandomIntegerUtils.generate(String.format("[randint{range=%s:%s}]", min, max));
            Assert.assertTrue(actual >= min && actual <= max, "|" + actual + "| is not in the specified range");
        }
    }

    @Test
    public void generate_withValidRangeModifierAndInternalWhitespace_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = RandomIntegerUtils.generate(String.format("[randint{ range  =  %s  :  %s }]", min, max));
            Assert.assertTrue(actual >= min && actual <= max, "|" + actual + "| is not in the specified range");
        }
    }

    @Test
    public void generate_withValidRangeModifierInternalWhitespace_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = RandomIntegerUtils.generate(String.format("[  randint  {   range  =  %s  :  %s }]", min, max));
            Assert.assertTrue(actual >= min && actual <= max, "|" + actual + "| is not in the specified range");
        }
    }

    @Test
    public void generate_withValidRangeModifierMixedCase_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = RandomIntegerUtils.generate(String.format("[randint{RaNgE=%s:%s}]", min, max));
            Assert.assertTrue(actual >= min && actual <= max, "|" + actual + "| is not in the specified range");
        }
    }

    @Test
    public void generateEven_shouldReturnAnyRandomEvenInteger() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateEven();
            Assert.assertTrue(actual % 2 == 0);
        }
    }

    @Test
    public void generateEven_withEvenRangeNumbers_shouldReturnRandomEvenIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateEven(-6, 4);
            Assert.assertTrue(actual % 2 == 0);
        }
    }

    @Test
    public void generateEven_withOddRangeNumbers_shouldReturnRandomEvenIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateEven(-3, 9);
            Assert.assertTrue(actual % 2 == 0);
        }
    }

    @Test
    public void generateOdd_shouldReturnAnyRandomOddInteger() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateOdd();
            Assert.assertTrue(actual % 2 != 0);
        }
    }

    @Test
    public void generateOdd_withEvenRangeNumbers_shouldReturnRandomOddIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateOdd(-4, 6);
            Assert.assertTrue(actual % 2 != 0);
        }
    }

    @Test
    public void generateOdd_withOddRangeNumbers_shouldReturnRandomOddIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 100; i++) {
            actual = RandomIntegerUtils.generateOdd(-3, 9);
            Assert.assertTrue(actual % 2 != 0);
        }
    }

    @Test
    public void isRandomIntegerKeyword_withKeywordStringWithInternalWhitespace_shouldReturnTrue() {
        Assert.assertTrue(RandomIntegerUtils.isRandomIntegerKeyword("    [    randint   {xxxxx}]   "));
    }

    @Test
    public void isRandomIntegerKeyword_withKeywordStringWithLeadingTrailingWhitespace_shouldReturnTrue() {
        Assert.assertTrue(RandomIntegerUtils.isRandomIntegerKeyword("    [randint{xxxxx}]   "));
    }

    @Test
    public void isRandomIntegerKeyword_withKeywordStringWithMixedCase_shouldReturnTrue() {
        Assert.assertTrue(RandomIntegerUtils.isRandomIntegerKeyword("[RaNdInT{xxxxx}]"));
    }

    @Test
    public void isRandomIntegerKeyword_withNonKeywordString_shouldReturnFalse() {
        Assert.assertFalse(RandomIntegerUtils.isRandomIntegerKeyword("[notarandint{xxxxx}]"));
    }

    @Test
    public void isRandomIntegerKeyword_withSimpleKeywordString_shouldReturnTrue() {
        Assert.assertTrue(RandomIntegerUtils.isRandomIntegerKeyword("[randint{xxxxx}]"));
    }

    @Test
    public void RandomIntegerUtils_shouldHaveInaccessibleConstructor() {
        final Constructor<?>[] constructors = RandomIntegerUtils.class.getDeclaredConstructors();
        final Constructor<?> constructor = constructors[0];
        Assert.assertFalse(constructor.isAccessible(), "Constructor should be inaccessible");
    }

    @Test
    public void RandomIntegerUtils_shouldHaveOneConstructor() {
        final Constructor<?>[] constructors = RandomIntegerUtils.class.getDeclaredConstructors();
        Assert.assertEquals(constructors.length, 1);
    }

    @Test(expectedExceptions = InvocationTargetException.class)
    public void RandomIntegerUtilsInstantiationThroughReflection_shouldThrowException() throws Exception {
        final Constructor<?>[] constructors = RandomIntegerUtils.class.getDeclaredConstructors();
        final Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
