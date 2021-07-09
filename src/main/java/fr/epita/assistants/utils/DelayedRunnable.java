package fr.epita.assistants.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DelayedRunnable implements Runnable {
    private CompletableFuture<Void> future = null;
    private final Consumer<Void> consumer;

    public DelayedRunnable(final Consumer<Void> consumer)
    {
        this.consumer = consumer;
    }

    public DelayedRunnable runLater(long ms)
    {
        future = CompletableFuture.runAsync(this, CompletableFuture.delayedExecutor(ms, TimeUnit.MILLISECONDS));
        return this;
    }

    public void cancel()
    {
        future.cancel(true);
    }

    @Override
    public void run()
    {
        consumer.accept(null);
    }
}
