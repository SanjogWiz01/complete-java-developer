package unit7.capstone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderService implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public Future<String> processOrder(long orderId, double amount) {
        return executor.submit(() ->
                "Order " + orderId + " processed for Rs." + amount
                + " by " + Thread.currentThread().getName());
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    public static void main(String[] args) throws Exception {
        try (OrderService service = new OrderService()) {
            Future<String> result = service.processOrder(5001, 4500);
            System.out.println(result.get());
        }
    }
}
