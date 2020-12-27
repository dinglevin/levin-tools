package com.dinglevin.tools.corejava.sets.utils;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadTester {
    private final long sleepBefore;
    private final int threadCount;
    private final ExecutorService executor;
    
    public MultiThreadTester(long sleepBefore, int threadCount) {
        this.sleepBefore = sleepBefore;
        this.threadCount = threadCount;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }
    
    public void execute(TestTask testTask) throws Exception {
        CompletionService<Void> completionService = new ExecutorCompletionService<Void>(executor);
        for (int i = 0; i < threadCount; i++) {
            completionService.submit(new InnerRunnable(testTask, i), null);
        }
        for (int i = 0; i < threadCount; i++) {
            completionService.take().get();
        }
    }
    
    public static interface TestTask {
        public void run(int threadIndex);
    }
    
    private class InnerRunnable implements Runnable {
        private final TestTask proxy;
        private final int threadIndex;
        
        public InnerRunnable(TestTask proxy, int threadIndex) {
            this.proxy = proxy;
            this.threadIndex = threadIndex;
        }
        
        public void run() {
            if (proxy != null) {
                if (sleepBefore > 0) {
                    try {
                        Thread.sleep(sleepBefore);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                proxy.run(threadIndex);
            } else {
                throw new IllegalStateException("proxy is null");
            }
        }
    }
}
