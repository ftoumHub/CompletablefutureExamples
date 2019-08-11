package app.example;

import app.data.A;
import app.task.Task;
import app.util.Util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Example2 {

    public static void show() throws ExecutionException, InterruptedException {

        int noTasks = 5;
        ExecutorService executor = Executors.newFixedThreadPool(noTasks);
        long startTime = System.currentTimeMillis();

        //----------------------------------------------------------
        //COMBINATION
        //parallel, runs as long as the longest running single task
        CompletableFuture<A> combined = CompletableFuture.supplyAsync(() -> Task.doTask(500L, new A("[cf 0]")));

        for(int i = 1; i < noTasks; i++){
            final int nr = i;
            CompletableFuture<A> newTask = CompletableFuture.supplyAsync(() -> Task.doTask(Util.random(), new A(nr)), executor);
            combined = combined.thenCombine(newTask, A::reducer);
        }

        //----------------------------------------------------------
        String result = combined.get().val;
        long endTime = System.currentTimeMillis();
        System.out.printf("Got [%s] in [%d] ms\n", result, endTime - startTime);

        executor.shutdown();
    }

}