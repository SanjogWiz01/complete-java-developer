# 7.5 Concurrency and Multithreading in Java

Concurrency means multiple tasks make progress during overlapping time periods. Multithreading uses multiple threads within a process.

## Important concepts
- `Thread`
- `Runnable` / `Callable`
- thread lifecycle
- `start()` vs `run()`
- race condition
- critical section
- `synchronized`
- `volatile`
- atomic classes
- locks
- `ExecutorService`
- thread pools
- `Future` / `CompletableFuture`
- concurrent collections
- deadlock
- starvation
- visibility and happens-before

## Best practice
Prefer high-level concurrency utilities and bounded executor pools over manually creating many threads.

## Real application
`OrderProcessingApp.java` simulates an online store processing many independent orders concurrently. A fixed thread pool limits concurrency and `Future` collects results.

## Key warning
Shared mutable state is the main source of race conditions. Make state immutable where practical and synchronize only the required critical sections.
