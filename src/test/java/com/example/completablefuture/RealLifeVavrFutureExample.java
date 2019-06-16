package com.example.completablefuture;

import com.spotify.futures.CompletableFutures;
import io.vavr.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class RealLifeVavrFutureExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        cars().flatMap(cars -> {
            List<Future<Car>> updatedCars = cars.stream()
                    .map(car -> rating(car.manufacturerId).map(r -> {
                        car.setRating(r);
                        return car;
                    })).collect(Collectors.toList());
            return Future.sequence(updatedCars);
        }).onSuccess(cars -> cars.forEach(System.out::println)
        ).onFailure(th -> new RuntimeException(th))
        .toCompletableFuture().join();

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + " ms.");
    }

    static Future<Float> rating(int manufacturer) {
        return Future.of(() -> {
            try {
                simulateDelay();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            switch (manufacturer) {
                case 2:
                    return 4f;
                case 3:
                    return 4.1f;
                case 7:
                    return 4.2f;
                default:
                    return 5f;
            }
        }).recover(th -> -1f);
    }

    static Future<List<Car>> cars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1, 3, "Fiesta", 2017));
        carList.add(new Car(2, 7, "Camry", 2014));
        carList.add(new Car(3, 2, "M2", 2008));

        return Future.of(() -> carList);
    }

    private static void simulateDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}