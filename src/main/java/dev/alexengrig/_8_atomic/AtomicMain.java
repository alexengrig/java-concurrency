package dev.alexengrig._8_atomic;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicMain {
    private final AtomicReference<Developer> devRef = new AtomicReference<>(new Developer());

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AtomicMain main = new AtomicMain();
        Callable<?> task = () -> {
            main.swapDev();
            return null;
        };
        executorService.invokeAll(Collections.nCopies(10, task));
    }

    private void swapDev() {
        while (true) {
            Developer current;
            Developer candidate;
            if (devRef.compareAndSet(current = devRef.get(), candidate = new Developer())) {
                System.out.println("Fired " + current + " and hired " + candidate);
                break;
            } else {
                System.out.println("Current " + current + " and not match " + candidate);
            }
        }
    }

    private static class Developer {
        private static int COUNT = 0;
        private final String name = "Dev#" + COUNT++;

        public Developer() {
            System.out.println("Created " + this);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
