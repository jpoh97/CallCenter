package com.almundo.callcenter.model.call;

import com.almundo.callcenter.customerservices.Dispatcher;

import java.util.Random;
import java.util.concurrent.Callable;

public class Call implements Callable<Void> {

    private static final Integer MIN_DURATION = 5;
    private static final Integer MAX_DURATION = 10;

    private final int id;
    private final int duration;

    public Call(int id) {
        this.id = id;
        this.duration = calculateDuration();
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    private int calculateDuration() {
        Random random = new Random();
        return random.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
    }

    public Void call() {
        Dispatcher.getSingletonInstance().dispatchCall(this);
        return null;
    }
}
