package com.marccarre;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SynchronousVsAsynchronous {
    public static void main(final String[] args) {
        stopwatch(() -> {
            // Synchronous:
            int x1 = compute(1);
            int x2 = add(x1, 1);
            int y = compute(2);
            int r = add(x2, y);
            println("Result: " + r);
        });
        // Output:
        // [main]: Computing 1...
        // [main]: Computed 1.
        // [main]: Adding 1 and 1...
        // [main]: Added 1 and 1.
        // [main]: Computing 2...
        // [main]: Computed 2.
        // [main]: Adding 2 and 2...
        // [main]: Added 2 and 2.
        // [main]: Result: 4
        // Elapsed time: 7 seconds.

        stopwatch(() -> {
            // Asynchronous:
            CompletableFuture<Integer> x1 = CompletableFuture.supplyAsync(() -> compute(1));
            CompletableFuture<Integer> x2 = x1.thenApply(x -> add(x, 1));
            CompletableFuture<Integer> y = CompletableFuture.supplyAsync(() -> compute(2));
            CompletableFuture<Integer> r = x2.thenCombine(y, (i, j) -> add(i, j));
            // Block and wait for results (avoid this in production code!):
            try { println("Result: " + r.get()); } catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
        });
        // Output:
        // [ForkJoinPool.commonPool-worker-1]: Computing 1...
        // [ForkJoinPool.commonPool-worker-2]: Computing 2...
        // [ForkJoinPool.commonPool-worker-1]: Computed 1.
        // [ForkJoinPool.commonPool-worker-1]: Adding 1 and 1...
        // [ForkJoinPool.commonPool-worker-2]: Computed 2.
        // [ForkJoinPool.commonPool-worker-1]: Added 1 and 1.
        // [ForkJoinPool.commonPool-worker-1]: Adding 2 and 2...
        // [ForkJoinPool.commonPool-worker-1]: Added 2 and 2.
        // [main]: Result: 4
        // Elapsed time: 5 seconds.
    }

    private static int compute(final int x) {
        println("Computing " + x + "...");
        sleep(x, SECONDS);
        println("Computed " + x + ".");
        return x;
    }

    private static int add(final int x, final int y) {
        println("Adding " + x + " and " + y + "...");
        sleep(2, SECONDS);
        final int r = x + y;
        println("Added " + x + " and " + y + ".");
        return r;
    }

    private static void sleep(final int duration, final TimeUnit unit) {
        try { unit.sleep(duration); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private static void println(final String message) {
        System.out.println("[" + Thread.currentThread().getName() + "]: " + message);
    }

    private static void stopwatch(final Runnable action) {
        final long begin = System.currentTimeMillis();
        action.run();
        System.out.println("Elapsed time: " + (System.currentTimeMillis() - begin) / 1000 + " seconds.");
    }
}
