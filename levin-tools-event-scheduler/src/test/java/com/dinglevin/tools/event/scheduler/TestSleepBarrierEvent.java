package com.dinglevin.tools.event.scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestSleepBarrierEvent extends TestSleepEvent implements BarrierEvent {

    public TestSleepBarrierEvent(String eventName, long sleepTime, CountDownLatch countDownLatch) {
        super(eventName, sleepTime, countDownLatch);
    }
    
    public TestSleepBarrierEvent(String eventName, TimeUnit unit, int number, CountDownLatch countDownLatch) {
        super(eventName, unit, number, countDownLatch);
    }

}
