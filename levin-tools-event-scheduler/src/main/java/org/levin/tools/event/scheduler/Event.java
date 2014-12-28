package org.levin.tools.event.scheduler;

public interface Event {
    public String getName();
    public void process();
}
