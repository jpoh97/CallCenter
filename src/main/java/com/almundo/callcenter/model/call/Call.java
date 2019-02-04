package com.almundo.callcenter.model.call;

import java.util.Random;

public class Call {

    private static final Integer MIN_DURATION = 5;
    private static final Integer MAX_DURATION = 10;

    private final int duration;

    public Call() {
        this.duration = calculateDuration();
    }

    public int getDuration() {
        return duration;
    }

    private int calculateDuration() {
        Random random = new Random();
        return random.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
    }
}
