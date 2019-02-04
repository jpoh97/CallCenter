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

public class Main {

    private static final Dispatcher dispatcher = Dispatcher.getSingletonInstance();

    private static final String OPERATOR_NAME = "Operator #%d";
    private static final int OPERATORS = 7;
    private static final int SUPERVISORS = 2;
    private static final int DIRECTORS = 1;
    private static final String SUPERVISOR_NAME = "Supervisor #%d";
    private static final String DIRECTOR_NAME = "Director #%d";
    private static final int NUMERO_DE_LLAMADAS_CONCURRENTES = 10;

    private static final int NUMERO_DE_LLAMADAS_A_PROBAR = 20;

    public static void main(String... args) throws InterruptedException {
        createEmployees();

        ExecutorService executorService = Executors.newFixedThreadPool(NUMERO_DE_LLAMADAS_CONCURRENTES);
        List<Callable<Void>> calls = createCalls();

        executorService.invokeAll(calls);

        executorService.shutdown();
    }

    private static void createEmployees() {

        for (int i = 1; i <= OPERATORS; i++) {
            dispatcher.addEmployee(EmployeeFactory.getEmployee(String.format(OPERATOR_NAME, i), Charge.OPERATOR));
        }

        for (int i = 1; i <= SUPERVISORS; i++) {
            dispatcher.addEmployee(EmployeeFactory.getEmployee(String.format(SUPERVISOR_NAME, i), Charge.SUPERVISOR));
        }

        for (int i = 1; i <= DIRECTORS; i++) {
            dispatcher.addEmployee(EmployeeFactory.getEmployee(String.format(DIRECTOR_NAME, i), Charge.DIRECTOR));
        }
    }

    private static List<Callable<Void>> createCalls() {
        List<Callable<Void>> calls = new ArrayList<>();
        for (int i = 0; i < NUMERO_DE_LLAMADAS_A_PROBAR; i++) {
            calls.add(new Call(i));
        }
        return calls;
    }
}
