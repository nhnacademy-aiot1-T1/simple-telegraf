package com.nhnacademy.aiotone.node;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public abstract class RunnableNode extends BaseNode implements Runnable {
    @Setter
    private long intervalMs = 300;
    private final Thread thread;

    protected RunnableNode() {
        this.thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void run() {
        preProcess();

        while (!thread.isInterrupted()) {
            try {
                process();
                Thread.sleep(intervalMs);

            } catch (InterruptedException e) {
                thread.interrupt();

                log.error(e.getMessage());
            }
        }

        postProcess();
    }

    public void preProcess() {
    }

    public abstract void process();

    public void postProcess() {
    }
}
