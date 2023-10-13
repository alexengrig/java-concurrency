package dev.alexengrig.executor;

import java.util.concurrent.Executor;

public class ThreadPerRunnableExecutor implements Executor {
    @Override
    public void execute(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
