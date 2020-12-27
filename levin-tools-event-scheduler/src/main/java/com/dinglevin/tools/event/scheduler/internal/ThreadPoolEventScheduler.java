package com.dinglevin.tools.event.scheduler.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dinglevin.tools.event.scheduler.EventScheduler;
import com.dinglevin.tools.event.scheduler.BarrierEvent;
import com.dinglevin.tools.event.scheduler.Event;

public class ThreadPoolEventScheduler implements EventScheduler {
    private static final int DEFAULT_CONSUMER_SIZE = 10;
    
    private ExecutorService executorService;
    private int consumerSize;
    private AtomicBoolean started;
    private Thread schedulerThread;
    private BlockingQueue<Event> workingQueue;
    private CompletionService<?> lastCompletionService;
    private int lastTasksCount;
    
    public ThreadPoolEventScheduler(int consumerSize) {
        this.consumerSize = consumerSize;
        started = new AtomicBoolean(false);
        workingQueue = new LinkedBlockingQueue<Event>();
    }
    
    public ThreadPoolEventScheduler() {
        this(DEFAULT_CONSUMER_SIZE);
    }
    
    public int getConsumerSize() {
        return consumerSize;
    }
    
    public boolean isStarted() {
        return started.get();
    }

    @Override
    public void start() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("Already Started");
        }
        
        executorService = Executors.newFixedThreadPool(consumerSize);
        lastCompletionService = new ExecutorCompletionService<Void>(executorService);
        lastTasksCount = 0;
        
        schedulerThread = new Thread(new SchedulerRunner());
        schedulerThread.setName("Event Scheduler Thread");
        schedulerThread.start();
    }

    @Override
    public EventScheduler submit(final Event event) {
        if (!started.get()) {
            throw new IllegalStateException("Not Started");
        }
        
        workingQueue.offer(event);
        
        return this;
    }

    @Override
    public void stop() {
        if (!started.get()) {
            throw new IllegalStateException("Not Started");
        }
        
        started.set(false);
        try {
            schedulerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting for scheulder thread to finish get interrupted", e);
        } finally {
            executorService.shutdown();
        }
    }
    
    private class SchedulerRunner implements Runnable {
        @Override
        public void run() {
            try {
                while (started.get()) {
                    final Event event = workingQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (event == null) {
                        continue;
                    }
                    
                    if (!(event instanceof BarrierEvent)) {
                        lastTasksCount++;
                        lastCompletionService.submit(new Runnable() {
                            @Override
                            public void run() {
                                event.process();
                            }
                        }, null);
                    } else {
                        for (int i = 0; i < lastTasksCount; i++) {
                            lastCompletionService.take();
                        }
                        
                        try {
                            event.process();
                        } finally {
                            lastTasksCount = 0;
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected interrupted", e);
            }
        }
    }

}
