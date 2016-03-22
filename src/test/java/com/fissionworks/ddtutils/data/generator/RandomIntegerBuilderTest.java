package com.fissionworks.ddtutils.data.generator;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RandomIntegerBuilderTest {

    @Test
    public void build_withNoParametersSet_shouldReturnAnyRandomInteger() {
        final int actual = new RandomIntegerBuilder().build();
        // hmmmm, not much to check here!
        Assert.assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE,
                "default build should be any integer");
    }

    @Test
    public void build_withRangeSet_shouldReturnRandomIntegerInDesiredRange() {
        int actual = Integer.MIN_VALUE;
        final int min = -5;
        final int max = 6;
        // perform multiple checks to generate range of random numbers to test due dynamic nature of the
        // random numbers
        for (int i = 0; i < 20; i++) {
            actual = new RandomIntegerBuilder().range(min, max).build();
            Assert.assertTrue(actual >= min && actual <= max);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void range_withMinValueGreaterThanMaxValue_shouldThrowException() {
        new RandomIntegerBuilder().range(10, 9);
    }
}
