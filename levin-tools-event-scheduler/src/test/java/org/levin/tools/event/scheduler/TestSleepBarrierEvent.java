package org.levin.tools.event.scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.levin.tools.event.scheduler.BarrierEvent;

public class TestSleepBarrierEvent extends TestSleepEvent implements BarrierEvent {

    public TestSleepBarrierEvent(String eventName, long sleepTime, CountDownLatch countDownLatch) {
        super(eventName, sleepTime, countDownLatch);
    }
    
    public TestSleepBarrierEvent(String eventName, TimeUnit unit, int number, CountDownLatch countDownLatch) {
        super(eventName, unit, number, countDownLatch);
    }

}
