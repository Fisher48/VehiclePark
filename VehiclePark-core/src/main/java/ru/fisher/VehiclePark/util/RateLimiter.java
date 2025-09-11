package ru.fisher.VehiclePark.util;

import java.util.concurrent.Semaphore;


public class RateLimiter {

    private final Semaphore semaphore;
    private final long timeFrameMs;

    public RateLimiter(int permitsPerMinute) {
        this.semaphore = new Semaphore(permitsPerMinute);
        this.timeFrameMs = 60000; // 1 минута
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
        new Thread(() -> {
            try {
                Thread.sleep(timeFrameMs / semaphore.availablePermits());
            } catch (InterruptedException ignored) {
            } finally {
                semaphore.release();
            }
        }).start();
    }
}
