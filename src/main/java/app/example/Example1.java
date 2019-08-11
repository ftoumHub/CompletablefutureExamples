package app.example;

import app.task.Task;
import app.util.Util;
import app.data.A;
import app.data.B;
import app.data.C;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Example1 {

    public static void show() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        //----------------------------------------------------------

        // Ces 3 tâches vont être exécuté en parallèle dans le ForkJoinPool
        CompletableFuture<A> aCF = CompletableFuture.supplyAsync(() -> Task.doTask(1000L, new A("AAA")));
        CompletableFuture<B> bCF = CompletableFuture.supplyAsync(() -> Task.doTask(500L, new B("BBB")));
        CompletableFuture<C> cCF = CompletableFuture.supplyAsync(() -> Task.doTask(500L, new C("CCC")));

        //a barrier at which all the tasks synchronize
        //and do not trespass when "joining" it
        CompletableFuture<Void> barrier = CompletableFuture.allOf(aCF, bCF, cCF);

        Util.printThreadIdAndName();
        //----------------------------------------------------------

        barrier.join();
        //all threads synchronize here and they are not active afterwards
        Util.assertAndPrintIsDone(aCF, "aCF"); //check that threads are done
        Util.assertAndPrintIsDone(bCF, "bCF");
        Util.assertAndPrintIsDone(cCF, "cCF");
        long endTime1 = System.currentTimeMillis();
        //----------------------------------------------------------

        System.out.printf("[A-result] %s\n", aCF.get()); //immediate return because threads are done
        System.out.printf("[B-result] %s\n", bCF.get());
        System.out.printf("[C-result] %s\n", cCF.get());

        long endTime2 = System.currentTimeMillis();
        //----------------------------------------------------------

        System.out.printf("[end1 - start] %d\n", endTime1 - startTime);
        System.out.printf("[end2 - start] %d\n", endTime2 - startTime);

        Util.printThreadIdAndName();
        //----------------------------------------------------------

    }
}