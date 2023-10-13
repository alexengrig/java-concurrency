package dev.alexengrig.runnable;

public class RunnableMain {
    static final int N = 10;

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            SleepyRunnable runnable = new SleepyRunnable(i);
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}
