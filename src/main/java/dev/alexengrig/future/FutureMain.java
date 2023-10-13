package dev.alexengrig.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("DuplicatedCode")
public class FutureMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runRunnable();
//        runCallable();
    }

    private static void runRunnable() throws ExecutionException, InterruptedException {
        int count = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        List<Future<Integer>> futures = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int number = i;
            Runnable runnable = () -> System.out.println("Set number#" + number);
            Future<Integer> future = executorService.submit(runnable, number);
            futures.add(future);
        }
        for (Future<Integer> future : futures) {
            Integer number = future.get();
            System.out.println("Get number#" + number);
        }
        executorService.shutdown();
    }

    private static void runCallable() throws ExecutionException, InterruptedException {
        int count = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        List<Future<Integer>> futures = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int number = i;
            Future<Integer> future = executorService.submit(() -> {
                System.out.println("Set number#" + number);
                return number;
            });
            futures.add(future);
        }
        for (Future<Integer> future : futures) {
            Integer number = future.get();
            System.out.println("Get number#" + number);
        }
        executorService.shutdown();
    }
}
