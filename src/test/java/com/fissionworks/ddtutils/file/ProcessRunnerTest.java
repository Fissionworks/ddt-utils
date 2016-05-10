package com.fissionworks.ddtutils.file;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessRunnerTest {

    private static final String EXIT_CODE_FORTY_TWO_EXECUTABLE = "exitCodeFortyTwo.exe";

    private static final String SLEEP_TWO_SECONDS_EXECUTABLE = "sleepTwoSeconds.exe";

    @Test
    public void getLastExitCode_shouldUpdateAfterAsynchronousRunCompletion() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder.decode(
                this.getClass().getClassLoader().getResource(ProcessRunnerTest.SLEEP_TWO_SECONDS_EXECUTABLE).getPath(),
                "utf-8")).getPath());
        runner.runAsynchronous();
        Assert.assertEquals(runner.getLastExitCode(), Integer.MIN_VALUE);
        Thread.sleep(2500L);
        Assert.assertEquals(runner.getLastExitCode(), 0);
    }

    @Test
    public void getLastExitCode_withProcessThatHasNeverRun_shouldReturnMinInteger()
            throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(
                new File(
                        URLDecoder.decode(
                                this.getClass().getClassLoader()
                                        .getResource(ProcessRunnerTest.EXIT_CODE_FORTY_TWO_EXECUTABLE).getPath(),
                                "utf-8")).getPath());
        Assert.assertEquals(runner.getLastExitCode(), Integer.MIN_VALUE);
    }

    @Test
    public void getLastExitCode_withValidPath_shouldReturnLastExitCode() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(
                new File(
                        URLDecoder.decode(
                                this.getClass().getClassLoader()
                                        .getResource(ProcessRunnerTest.EXIT_CODE_FORTY_TWO_EXECUTABLE).getPath(),
                                "utf-8")).getPath());
        runner.runSynchronous();
        Assert.assertEquals(runner.getLastExitCode(), 42);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void instatiate_withEmptyPath_shouldThrowExeption() {
        new ProcessRunner("  ");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void instatiate_withNullPath_shouldThrowExeption() {
        new ProcessRunner(null);
    }

    @Test
    public void isProcessRunning_withCompletedProcess_shouldReturnFalse() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(
                new File(
                        URLDecoder.decode(
                                this.getClass().getClassLoader()
                                        .getResource(ProcessRunnerTest.EXIT_CODE_FORTY_TWO_EXECUTABLE).getPath(),
                                "utf-8")).getPath());
        runner.runSynchronous();
        Assert.assertEquals(runner.isProcessRunning(), false);
    }

    @Test
    public void isProcessRunning_withNeverRunProcess_shouldReturnFalse() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(
                new File(
                        URLDecoder.decode(
                                this.getClass().getClassLoader()
                                        .getResource(ProcessRunnerTest.EXIT_CODE_FORTY_TWO_EXECUTABLE).getPath(),
                                "utf-8")).getPath());
        Assert.assertEquals(runner.isProcessRunning(), false);
    }

    @Test
    public void isProcessRunning_withRunningProcess_shouldReturnTrue() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder.decode(
                this.getClass().getClassLoader().getResource(ProcessRunnerTest.SLEEP_TWO_SECONDS_EXECUTABLE).getPath(),
                "utf-8")).getPath());
        runner.runAsynchronous();
        Assert.assertEquals(runner.isProcessRunning(), true);
    }

    @Test
    public void runAsynchronous_withValidPath_shouldRunWithoutWait() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder.decode(
                this.getClass().getClassLoader().getResource(ProcessRunnerTest.SLEEP_TWO_SECONDS_EXECUTABLE).getPath(),
                "utf-8")).getPath());
        final long startTime = System.currentTimeMillis();
        runner.runAsynchronous();
        final long timeAfterAsynchronousRun = System.currentTimeMillis();

        Assert.assertTrue((timeAfterAsynchronousRun - startTime) < 2000L, "runAsynchronous did not run asynchronously");
    }

    @Test
    public void runSynchronous_withValidPath_shouldReturnExitCode() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(
                new File(
                        URLDecoder.decode(
                                this.getClass().getClassLoader()
                                        .getResource(ProcessRunnerTest.EXIT_CODE_FORTY_TWO_EXECUTABLE).getPath(),
                                "utf-8")).getPath());
        Assert.assertEquals(runner.runSynchronous(), 42);
    }

}
