package org.levin.tools.event.scheduler;

public interface EventScheduler {
    public void start();
    public EventScheduler submit(Event event);
    public void stop();
}
