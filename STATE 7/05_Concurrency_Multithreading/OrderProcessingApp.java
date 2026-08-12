package unit7.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OrderProcessingApp {
    record Order(int id, double amount) {}

    public static void main(String[] args) throws Exception {
        List<Order> orders = List.of(
                new Order(101, 2500),
                new Order(102, 1800),
                new Order(103, 4200),
                new Order(104, 900),
                new Order(105, 3200)
        );

        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            List<Future<String>> jobs = new ArrayList<>();

            for (Order order : orders) {
                jobs.add(pool.submit(() -> process(order)));
            }

            for (Future<String> job : jobs) {
                System.out.println(job.get());
            }
        } finally {
            pool.shutdown();
        }
    }

    private static String process(Order order) throws InterruptedException {
        Thread.sleep(300); // simulate payment/inventory work
        return "Order " + order.id() + " processed by "
                + Thread.currentThread().getName()
                + " for Rs." + order.amount();
    }
}
