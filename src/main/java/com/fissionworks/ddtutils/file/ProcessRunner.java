package com.fissionworks.ddtutils.file;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;

/**
 * The ProcessRunner is a simple wrapper for executables that allows them to be run during the execution of
 * test cases (Such as running AutoIt scripts during automated tests).
 *
 */
public final class ProcessRunner {

    private Process currentProcess;

    private int lastExitCode = Integer.MIN_VALUE;

    private final String path;

    /**
     * Creates a ProcessRunner for the file argument provided.
     *
     * @param filePath
     *            The path to the executable file.
     */
    public ProcessRunner(final String filePath) {
        Validate.notBlank(filePath, "filePath cannot be null or empty");

        this.path = filePath.trim();
    }

    /**
     * Gets the exit code returned the last time the process completed running. If the process has never run,
     * the returned value is {@link Integer#MIN_VALUE}. If the process is still running, the value returned is
     * the exit code from the last run.
     *
     * @return The exit code returned the last time the process completed running.
     * @since 1.0.0
     */
    public int getLastExitCode() {
        if (!isProcessRunning() && !(currentProcess == null)) {
            lastExitCode = currentProcess.exitValue();
        }
        return lastExitCode;
    }

    /**
     * Is the process currently running?
     *
     * @return True if the process is currently running, false otherwise.
     * @since 1.0.0
     */
    public boolean isProcessRunning() {
        if (currentProcess == null) {
            return false;
        }
        return currentProcess.isAlive();
    }

    /**
     * Starts the process, but does not wait for it to complete before returning.
     *
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This class is the general class of
     *             exceptions produced by failed or interrupted I/O operations.
     * @since 1.0.0
     */
    public void runAsynchronous() throws IOException {
        currentProcess = Runtime.getRuntime().exec(path);
    }

    /**
     * Starts the process, and waits until it finishes running before returning. This method will wait
     * essentially indefinitely for the process to complete, so {@link #runSynchronous(long, TimeUnit)} is
     * recommended if no external measures for hung up processes is taken.
     *
     * @return The exit code returned when the process completes.
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This class is the general class of
     *             exceptions produced by failed or interrupted I/O operations.
     * @throws InterruptedException
     *             Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is
     *             interrupted, either before or during the activity.
     * @since 1.0.0
     */
    public int runSynchronous() throws IOException, InterruptedException {
        return runSynchronous(Long.MAX_VALUE, TimeUnit.HOURS);
    }

    /**
     * Starts the process, and waits until it finishes running or the specified time limit expires before
     * returning. If the process does not finish before the time limit, it is killed and the exit code is set
     * to '1'.
     *
     * @param timeLimit
     *            The quantity of time to wait before killing the process and returning.
     * @param timeUnit
     *            The {@link TimeUnit} desired for the time limit period.
     * @return The exit code returned from the process, or '1' if the process time limit is exceeded.
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This class is the general class of
     *             exceptions produced by failed or interrupted I/O operations.
     * @throws InterruptedException
     *             Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is
     *             interrupted, either before or during the activity.
     * @since 1.0.0
     */
    public int runSynchronous(final long timeLimit, final TimeUnit timeUnit) throws IOException, InterruptedException {
        currentProcess = Runtime.getRuntime().exec(path);
        System.out.println("wait: " + timeLimit + " " + timeUnit);
        System.out.println("start: " + System.currentTimeMillis());
        final boolean finished = currentProcess.waitFor(timeLimit, timeUnit);
        System.out.println("finish: " + System.currentTimeMillis());
        System.out.println("isFinished: " + finished);
        if (!finished) {
            currentProcess.destroyForcibly();
            lastExitCode = 1;
        } else {
            lastExitCode = currentProcess.exitValue();
        }
        return lastExitCode;
    }

}
