package com.almundo.callcenter;

import com.almundo.callcenter.customerservices.Dispatcher;
import com.almundo.callcenter.model.call.Call;
import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.EmployeeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase principal que contiene el metodo main, esta utiliza un executorService para crear 10 hilos que representa el
 * maximo numero de llamadas concurrentes que puede atender el call center. Se prueba con 20 llamadas. Las primeras 10
 * llamadas se atienden concurrentemente, las siguientes 10 esperan hasta que exista un hilo activo disponible
 */
public class Main {

    private static final Dispatcher DISPATCHER = Dispatcher.getSingletonInstance();

    private static final String OPERATOR_NAME = "Operator #%d";
    private static final String SUPERVISOR_NAME = "Supervisor #%d";
    private static final String DIRECTOR_NAME = "Director #%d";
    private static final int OPERATORS = 7;
    private static final int SUPERVISORS = 2;
    private static final int DIRECTORS = 1;

    private static final int NUMBER_OF_CONCURRENT_CALLS = 10;
    private static final int NUMBER_OF_TEST_CALLS = 20;

    public static void main(String... args) throws InterruptedException {
        createEmployees();

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);
        List<Callable<Void>> calls = createCalls();

        executorService.invokeAll(calls);

        executorService.shutdown();
    }

    private static void createEmployees() {

        for (int i = 1; i <= OPERATORS; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.getEmployee(String.format(OPERATOR_NAME, i), Charge.OPERATOR));
        }

        for (int i = 1; i <= SUPERVISORS; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.getEmployee(String.format(SUPERVISOR_NAME, i), Charge.SUPERVISOR));
        }

        for (int i = 1; i <= DIRECTORS; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.getEmployee(String.format(DIRECTOR_NAME, i), Charge.DIRECTOR));
        }
    }

    private static List<Callable<Void>> createCalls() {
        List<Callable<Void>> calls = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TEST_CALLS; i++) {
            calls.add(new Call(i));
        }
        return calls;
    }
}
