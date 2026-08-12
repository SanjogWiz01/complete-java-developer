package unit7.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafetyCounter {
    private final AtomicInteger successfulOrders = new AtomicInteger();

    public void markSuccess() {
        successfulOrders.incrementAndGet();
    }

    public int getSuccessfulOrders() {
        return successfulOrders.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafetyCounter counter = new ThreadSafetyCounter();

        Thread a = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.markSuccess();
        });
        Thread b = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.markSuccess();
        });

        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("Expected: 2000");
        System.out.println("Actual:   " + counter.getSuccessfulOrders());
    }
}
