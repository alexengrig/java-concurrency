package dev.alexengrig.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.random.RandomGenerator;

@SuppressWarnings("DuplicatedCode")
public class ExecutorMain {
    static final int N = 10;
    static final int TIME_TO_SLEEP = 10;
    static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    public static void main(String[] args) {
        runMyExecutor();
//        runExecutorService();
    }

    private static void runMyExecutor() {
        Executor executor = new ThreadPerRunnableExecutor();
        for (int i = 0; i < N; i++) {
            String name = "SleepyRunnable #" + i;
            Runnable runnable = () -> {
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
            };
            executor.execute(runnable);
        }
    }

    private static void runExecutorService() {
        ExecutorService executorService = Executors.newFixedThreadPool(N);
        for (int i = 0; i < N; i++) {
            String name = "SleepyRunnable #" + i;
            Runnable runnable = () -> {
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
            };
            executorService.execute(runnable);
        }
//        executorService.shutdown();
//        executorService.execute(() -> System.out.println("Foobar"));
//        try {
//            if (!executorService.awaitTermination(7, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//        }
        System.out.println("Main finished");
    }
}
