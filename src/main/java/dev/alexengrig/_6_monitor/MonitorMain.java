package dev.alexengrig._6_monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SuppressWarnings("DuplicatedCode")
public class MonitorMain {
    public static void main(String[] args) throws InterruptedException {
//        runDataChannel(new SingleDataChannel());
        runDataChannel(new ArrayDataChannel());
    }

    private static void runDataChannel(DataChannel channel) throws InterruptedException {
        int numberOfTasks = 10000;
        AtomicInteger counter = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // receiving
        executorService.execute(() -> {
            while (counter.get() < numberOfTasks) {
                try {
                    String data = channel.receive();
                    System.out.println("Data: " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        // sending
        IntStream.range(0, numberOfTasks)
                .mapToObj(i -> (Runnable) () -> {
                    try {
                        channel.send("Test data#" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        counter.incrementAndGet();
                    }
                })
                .forEach(executorService::execute);
        // waiting
        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
        }
    }

}
