package com.dinglevin.tools.event.scheduler.internal;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.dinglevin.tools.event.scheduler.EventScheduler;
import com.dinglevin.tools.event.scheduler.TestSleepBarrierEvent;
import com.dinglevin.tools.event.scheduler.TestSleepEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ThreadPoolEventSchedulerTest {
    private EventScheduler eventScheduler;
    private CountDownLatch countDownLatch;
    
    private Random random;
    
    @Before
    public void setup() {
        eventScheduler = new ThreadPoolEventScheduler();
        eventScheduler.start();
        
        random = new Random();
        random.setSeed(System.currentTimeMillis());
    }
    
    @After
    public void clearup() throws InterruptedException {
        countDownLatch.await();
        eventScheduler.stop();
    }
    
    @Test
    public void testWithOneNonBarrierEvent() {
        countDownLatch = new CountDownLatch(1);
        eventScheduler.submit(new TestSleepEvent("OneNonBarrierEvent", TimeUnit.SECONDS, 1, countDownLatch));
    }
    
    @Test
    public void testWithOneBarrierEvent() {
        countDownLatch = new CountDownLatch(1);
        eventScheduler.submit(new TestSleepBarrierEvent("OneBarrierEvent", TimeUnit.SECONDS, 1, countDownLatch));
    }
    
    @Test
    public void testThreeEventOneBarrierEventAfter() {
        countDownLatch = new CountDownLatch(4);
        for (int i = 0; i < 3; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
        
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent", TimeUnit.SECONDS, 5, countDownLatch));
    }
    
    @Test
    public void testNormalEventAfterBarrierEvent() {
        countDownLatch = new CountDownLatch(7);
        for (int i = 0; i < 3; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
        
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent", TimeUnit.SECONDS, 5, countDownLatch));
        
        for (int i = 3; i < 7; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
    }
    
    @Test
    public void testFourEventWithoutBarrierEvent() {
        countDownLatch = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
    }
    
    @Test
    public void testTwoBarrierEventTogether() {
        countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 3; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
        
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent1", TimeUnit.SECONDS, 2, countDownLatch));
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent2", TimeUnit.SECONDS, 2, countDownLatch));
    }
    
    @Test
    public void testTowBarrierEventSeparately() {
        countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 2; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
        
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent1", TimeUnit.SECONDS, 2, countDownLatch));
        
        for (int i = 0; i < 2; i++) {
            eventScheduler.submit(new TestSleepEvent("Event[" + i + "]", getRandom(), countDownLatch));
        }
        
        eventScheduler.submit(new TestSleepBarrierEvent("BarrierEvent2", TimeUnit.SECONDS, 2, countDownLatch));
    }
    
    private int getRandom() {
        return (int) Math.abs(random.nextLong() % 10000);
    }
}
