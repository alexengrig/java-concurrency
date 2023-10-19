package dev.alexengrig._8_atomic;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RaceCondition {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        check(new SimpleCounter());
        check(new VolatileCounter());
        check(new SynchronizedMethodCounter());
        check(new SynchronizedStatementsCounter());
        check(new LockCounter());
        check(new AtomicCounter());
        check(new AtomicIntegerFieldUpdaterCounter());
        check(new LongAccumulatorCounter());
        check(new LongAdderCounter());
    }

    private static void check(Counter counter) throws ExecutionException, InterruptedException {
        System.out.printf("Start check %s%n", counter.getClass().getSimpleName());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int max = 10_000_000;
        AtomicInteger atomicInteger = new AtomicInteger();
        Callable<Integer> callback1 = new CountCallback(max, counter, atomicInteger);
        Callable<Integer> callable2 = new CountCallback(max, counter, atomicInteger);
        long startTime = new Date().getTime();
        Future<Integer> future1 = executorService.submit(callback1);
        Future<Integer> future2 = executorService.submit(callable2);
        Integer value1 = future1.get();
        Integer value2 = future2.get();
        System.out.printf("Future 1: %d / %d\t(missing %d)%n", value1, max, max - value1);
        System.out.printf("Future 2: %d / %d\t(missing %d)%n", value2, max, max - value2);
        System.out.printf("AtomicInteger: %s / %d (missing %d)%n", atomicInteger, max, max - atomicInteger.get());
        long finishTime = new Date().getTime();
        System.out.printf("Time spent: %d%n", finishTime - startTime);
        executorService.shutdown();
        System.out.printf("Finish check %s\n%n", counter.getClass().getSimpleName());
    }
}

class CountCallback implements Callable<Integer> {
    private final int max;
    private final Counter counter;
    private final AtomicInteger atomicInteger;

    CountCallback(int max, Counter counter, AtomicInteger atomicInteger) {
        this.max = max;
        this.counter = counter;
        this.atomicInteger = atomicInteger;
    }

    @Override
    public Integer call() {
        while (atomicInteger.get() < max) {
            atomicInteger.incrementAndGet();
            counter.inc();
        }
        return counter.get();
    }
}

interface Counter {
    void inc();

    int get();
}

class SimpleCounter implements Counter {
    private int count;

    @Override
    public void inc() {
        count++;
    }

    @Override
    public int get() {
        return count;
    }
}

class VolatileCounter implements Counter {
    private volatile int count;

    @Override
    public void inc() {
        //noinspection NonAtomicOperationOnVolatileField
        count++;
    }

    @Override
    public int get() {
        return count;
    }
}

class SynchronizedMethodCounter implements Counter {
    private int count;

    @Override
    public synchronized void inc() {
        count++;
    }

    @Override
    public int get() {
        return count;
    }
}

class SynchronizedStatementsCounter implements Counter {
    private static final Object LOCKER = new Object();

    private int count;

    @Override
    public void inc() {
        synchronized (LOCKER) {
            count++;
        }
    }

    @Override
    public int get() {
        return count;
    }
}

class LockCounter implements Counter {
    private static final Lock LOCKER = new ReentrantLock();

    private int count = 0;

    @Override
    public void inc() {
        LOCKER.lock();
        count++;
        LOCKER.unlock();
    }

    @Override
    public int get() {
        return count;
    }
}

class AtomicCounter implements Counter {
    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void inc() {
        count.incrementAndGet();
    }

    @Override
    public int get() {
        return count.get();
    }
}

class AtomicIntegerFieldUpdaterCounter implements Counter {
    private static final AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterCounter> countUpdater
            = AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterCounter.class, "count");
    private volatile int count;

    @Override
    public void inc() {
        countUpdater.incrementAndGet(this);
    }

    @Override
    public int get() {
        return count;
    }
}

class LongAccumulatorCounter implements Counter {
    private final LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);

    @Override
    public void inc() {
        accumulator.accumulate(1);
    }

    @Override
    public int get() {
        return (int) accumulator.get();
    }
}

class LongAdderCounter implements Counter {
    private final LongAdder adder = new LongAdder();

    @Override
    public void inc() {
        adder.increment();
    }

    @Override
    public int get() {
        return (int) adder.sum();
    }
}
