package dev.alexengrig.runnable;

import java.util.random.RandomGenerator;

@SuppressWarnings("DuplicatedCode")
public class SleepyRunnable implements Runnable {

    private final static RandomGenerator RANDOM = RandomGenerator.getDefault();
    private final static long TIME_TO_SLEEP = 100L;

    private final String name;

    public SleepyRunnable(int number) {
        this.name = "SleepyRunnable #" + number;
    }

    @Override
    public void run() {
        System.out.println(name + ": I'm starting...");
        if (RANDOM.nextBoolean()) {
            System.out.println(name + ": I decided to sleep!");
            try {
                Thread.sleep(TIME_TO_SLEEP);
                System.out.println(name + ": I've slept enough!");
            } catch (InterruptedException ignore) {
                System.out.println(name + ": InterruptedException?!");
            }
        }
        System.out.println(name + ": I finished.");
    }
}
