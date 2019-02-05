package com.almundo.callcenter.model.call;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests para la clase que representa una llamada dentro del sistema.
 */
public class CallTest {

    /**
     * Se verifica que la duracion de una llamada se encuentre entre 5 y 10 segundos
     */
    @Test
    public void testGetDurationBetweenFiveAndTen() {
        Call call = new Call(1);
        assertTrue("Call duration must be greater or equal than 5 seconds", call.getDuration() >= 5);
        assertTrue("Call duration must be less or equal than 10 seconds", call.getDuration() <= 10);
    }
}