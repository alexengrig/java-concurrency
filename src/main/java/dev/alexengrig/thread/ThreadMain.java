package dev.alexengrig.thread;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class ThreadMain {
    static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
//        runNThreads();
//        runNDaemons();
//        runAndWaitNDaemons();
    }

    private static void runNThreads() {
        for (int i = 0; i < N; i++) {
            Thread thread = new SleepyThread(i);
            thread.start();
        }
    }

    private static void runNDaemons() {
        for (int i = 0; i < N; i++) {
            Thread thread = new SleepyThread(i);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private static void runAndWaitNDaemons() throws InterruptedException {
        List<Thread> threads = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Thread thread = new SleepyThread(i);
            threads.add(thread);
            thread.setDaemon(true);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
