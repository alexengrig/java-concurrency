package dev.alexengrig._9_lock;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockMain {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<?> callable = () -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ": Lock");
                for (int i = 0; i < 3; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ignore) {
                        System.out.println(Thread.currentThread().getName() + ": Skip interrupted by sleeping");
                    }
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + ": Unlock");
                lock.unlock();
            }
            return null;
        };
        executorService.invokeAll(Collections.nCopies(2, callable), 1, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }

}
