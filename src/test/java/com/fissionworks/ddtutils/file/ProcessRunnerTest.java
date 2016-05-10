package com.fissionworks.ddtutils.file;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProcessRunnerTest {

    private String exitCodeFortyTwoExecutable;

    private String sleepTwoSecondsExecutable;

    @Test
    public void getLastExitCode_shouldUpdateAfterAsynchronousRunCompletion() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(sleepTwoSecondsExecutable).getPath(), "utf-8"))
                        .getPath());
        runner.runAsynchronous();
        Assert.assertEquals(runner.getLastExitCode(), Integer.MIN_VALUE);
        Thread.sleep(2500L);
        Assert.assertEquals(runner.getLastExitCode(), 0);
    }

    @Test
    public void getLastExitCode_withProcessThatHasNeverRun_shouldReturnMinInteger()
            throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(exitCodeFortyTwoExecutable).getPath(), "utf-8"))
                        .getPath());
        Assert.assertEquals(runner.getLastExitCode(), Integer.MIN_VALUE);
    }

    @Test
    public void getLastExitCode_withValidPath_shouldReturnLastExitCode() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(exitCodeFortyTwoExecutable).getPath(), "utf-8"))
                        .getPath());
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
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(exitCodeFortyTwoExecutable).getPath(), "utf-8"))
                        .getPath());
        runner.runSynchronous();
        Assert.assertEquals(runner.isProcessRunning(), false);
    }

    @Test
    public void isProcessRunning_withNeverRunProcess_shouldReturnFalse() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(exitCodeFortyTwoExecutable).getPath(), "utf-8"))
                        .getPath());
        Assert.assertEquals(runner.isProcessRunning(), false);
    }

    @Test
    public void isProcessRunning_withRunningProcess_shouldReturnTrue() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(sleepTwoSecondsExecutable).getPath(), "utf-8"))
                        .getPath());
        runner.runAsynchronous();
        Assert.assertEquals(runner.isProcessRunning(), true);
    }

    @Test
    public void runAsynchronous_withValidPath_shouldRunWithoutWait() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(sleepTwoSecondsExecutable).getPath(), "utf-8"))
                        .getPath());
        final long startTime = System.currentTimeMillis();
        runner.runAsynchronous();
        final long timeAfterAsynchronousRun = System.currentTimeMillis();

        Assert.assertTrue((timeAfterAsynchronousRun - startTime) < 2000L, "runAsynchronous did not run asynchronously");
    }

    @Test
    public void runSynchronous_withValidPath_shouldReturnExitCode() throws IOException, InterruptedException {
        final ProcessRunner runner = new ProcessRunner(new File(URLDecoder
                .decode(this.getClass().getClassLoader().getResource(exitCodeFortyTwoExecutable).getPath(), "utf-8"))
                        .getPath());
        Assert.assertEquals(runner.runSynchronous(), 42);
    }

    @BeforeClass
    public void setup() {
        if (StringUtils.containsIgnoreCase(System.getProperty("os.name"), "windows")) {
            exitCodeFortyTwoExecutable = "exitCodeFortyTwo.exe";
            sleepTwoSecondsExecutable = "sleepTwoSeconds.exe";
        } else {
            exitCodeFortyTwoExecutable = "exitCodeFortyTwo.sh";
            sleepTwoSecondsExecutable = "sleepTwoSeconds.sh";
        }
    }

}
