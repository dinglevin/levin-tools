package com.dinglevin.tools.event.scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestSleepEvent implements Event {
    private final String eventName;
    private long sleepTime;
    
    private CountDownLatch countDownLatch;
    
    public TestSleepEvent(String eventName, long sleepTime, CountDownLatch countDownLatch) {
        this.eventName = eventName;
        this.sleepTime = sleepTime;
        this.countDownLatch = countDownLatch;
    }
    
    public TestSleepEvent(String eventName, TimeUnit unit, int number, CountDownLatch countDownLatch) {
        this.eventName = eventName;
        this.sleepTime = unit.toMillis(number);
        this.countDownLatch = countDownLatch;
    }
    
    public String getName() {
        return eventName;
    }
    
    public long getSleepTime() {
        return sleepTime;
    }

    @Override
    public void process() {
        System.out.println(Thread.currentThread().getName() + " - Event: " + getName() + " started...");
        try {
            System.out.println(Thread.currentThread().getName() + " - Event: " + getName() + " Sleeping for " + sleepTime + "ms");
            Thread.sleep(sleepTime);
            System.out.println(Thread.currentThread().getName() + " - Event: " + getName() + " Sleep finished");
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt while sleeping");
        }
        System.out.println(Thread.currentThread().getName() + " - Event: " + getName() + " finished.");
        
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
