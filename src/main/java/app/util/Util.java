package app.util;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Util {

    public static void printThreadDiagnostics(long timeout, String info) {
        System.out.printf("[Thread-%s][%d][%s]%s\n", Thread.currentThread().getId(), timeout, Thread.currentThread().getName(), info);
    }

    public static void printThreadDiagnostics(String info) {
        System.out.printf("[Thread-%s][%s]\n", Thread.currentThread().getId(), info);
    }

    public static void printThreadIdAndName() {
        System.out.printf("[Thread-%s]:[%s]\n", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    public static void assertAndPrintIsDone(CompletableFuture<?> cf, String name) {
        assert (cf.isDone());
        System.out.printf("[CF %s isDone?-%b]\n", name, cf.isDone());
    }

    public static long random(){
        float generatedFloat = new Random().nextFloat() * 3000;
        return (long) generatedFloat;
    }

}