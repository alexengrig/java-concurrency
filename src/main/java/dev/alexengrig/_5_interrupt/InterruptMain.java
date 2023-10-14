package dev.alexengrig._5_interrupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class InterruptMain {
    public static void main(String[] args) throws InterruptedException {
        interruptTask();
//        interruptThread();
    }

    private static void interruptTask() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            for (int step = 1; step <= 5; step++) {
                System.out.println("Step#" + step);
                try {
                    TimeUnit.SECONDS.sleep(step);
                } catch (InterruptedException e) {
                    System.out.println("STOP!");
//                    break;
                }
            }
        });
        executorService.shutdown();

//        try {
//            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//        }

//        TimeUnit.SECONDS.sleep(3L);
//        future.cancel(false);
//        future.cancel(true);
    }

    private static void interruptThread() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("Starting");
            doInterruptedWork();
            if (Thread.interrupted()) {
                System.out.println(
                        "Mark as interrupted, because was is - " +
                        Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
            }
            System.out.println("Finished");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(3);
        thread.interrupt();
        thread.join();
        System.out.println("Interrupted? -" + thread.isInterrupted());
    }

    private static void doInterruptedWork() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Break work");
                break;
            }
            for (int i = 0; i < 100; i++) {
                System.out.print(".");
            }
            System.out.println();
        }
    }
}
