package com.example.completablefuture;

import io.vavr.collection.List;
import io.vavr.concurrent.Future;

import static io.vavr.API.*;


public class RealLifeVavrFutureExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        cars().flatMap(cars -> {
            List<Future<Car>> updatedCars = cars
                    .map(car -> rating(car.manufacturerId)
                            .map(r -> car.withRating(r))).toList();
            return Future.sequence(updatedCars);
        })
        .toCompletableFuture().whenComplete((cars, th) -> {
            if (th == null) {
                cars.forEach(System.out::println);
            } else {
                throw new RuntimeException(th);
            }
        }).join();

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
            return Match(manufacturer).of(
                    Case($(2), 4f),
                    Case($(3), 4.1f),
                    Case($(7), 4.2f),
                    Case($(), 5f));
        }).recover(th -> -1f);
    }

    static Future<List<Car>> cars() {
        return Future.of(() -> List.of(
            new Car(1, 3, "Fiesta", 2017),
            new Car(2, 7, "Camry", 2014),
            new Car(3, 2, "M2", 2008)));
    }

    private static void simulateDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}