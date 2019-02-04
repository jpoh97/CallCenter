package com.almundo.callcenter.customerservices;

import com.almundo.callcenter.model.call.Call;
import com.almundo.callcenter.model.employees.Employee;
import org.apache.log4j.Logger;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private static final String MENSAJE_LLAMADA_RECIBIDA = "La llamada #%d sera contestada por %s";
    private static final String MENSAJE_LLAMADA_TERMINADA = "Llamada #%d ha terminado y duro %d segundos";

    private volatile PriorityBlockingQueue<Employee> employees = new PriorityBlockingQueue<>();

    private static Dispatcher dispatcher;

    private final Logger logger = Logger.getLogger(Dispatcher.class);

    private Dispatcher() {}

    public static Dispatcher getSingletonInstance() {
        if (dispatcher == null) dispatcher = new Dispatcher();
        return dispatcher;
    }

    public void addEmployee(Employee employee) {
        this.employees.offer(employee);
    }

    public void dispatchCall(Call call) {
        try {
            Employee employee = employees.take();
            logger.info(String.format(MENSAJE_LLAMADA_RECIBIDA, call.getId(), employee.getName()));
            TimeUnit.SECONDS.sleep(call.getDuration());
            logger.info(String.format(MENSAJE_LLAMADA_TERMINADA, call.getId(), call.getDuration()));
            employees.offer(employee);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
