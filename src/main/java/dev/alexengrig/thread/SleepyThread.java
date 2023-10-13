package dev.alexengrig.thread;

import java.util.random.RandomGenerator;

@SuppressWarnings("DuplicatedCode")
public class SleepyThread extends Thread {
    private final static long TIME_TO_SLEEP = 5_000L;
    private final static RandomGenerator RANDOM = RandomGenerator.getDefault();

    public SleepyThread(int number) {
        setName("SleepyThread #" + number);
    }

    @Override
    public void run() {
        String name = getName();
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
