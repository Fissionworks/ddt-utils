package com.fissionworks.ddtutils.file;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

/**
 * The ProcessRunner is a wrapper for executables that allows them to be run during the execution of test
 * cases (Such as running AutoIt scripts during automated tests).
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
     * Starts the process, and waits until it finishes running before returning.
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
        currentProcess = Runtime.getRuntime().exec(path);
        lastExitCode = currentProcess.waitFor();
        return lastExitCode;
    }

}
