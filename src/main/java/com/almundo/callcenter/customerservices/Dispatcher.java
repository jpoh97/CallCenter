package com.almundo.callcenter.customerservices;

public class Dispatcher {

    private static Dispatcher dispatcher;

    private Dispatcher() {}

    public static Dispatcher getSingletonInstance() {
        if (dispatcher == null) dispatcher = new Dispatcher();
        return dispatcher;
    }


    public void dispatchCall() {
    }
}
