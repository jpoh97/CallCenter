package com.almundo.callcenter.customerservices;

import com.almundo.callcenter.model.call.Call;
import com.almundo.callcenter.model.employees.Employee;
import org.apache.log4j.Logger;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Dispatcher tiene la funcion de coordinar cada las llamadas que entran. Esta clase implementa el patron Singleton.
 * Se utilizo un PriorityBlockingQueue para manejar el orden en el cual se reciben las llamadas dependiendo del cargo.
 * Con employees.poll() se asegura que si no hay empleados disponibles, es decir que los 10 empleados estan ocupados
 * y llega una onceava llamada, el sistema esperara hasta que se encuentre un empleado disponible, ya que el
 * PriorityBlockingQueue bloquea el hilo hasta que exista un empleado a retornar
 */
public class Dispatcher {

    private static final String MENSAJE_LLAMADA_RECIBIDA = "La llamada #%d sera contestada por %s";
    private static final String MENSAJE_LLAMADA_TERMINADA = "Llamada #%d ha terminado y duro %d segundos";

    private PriorityBlockingQueue<Employee> employees = new PriorityBlockingQueue<>();

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
            Employee employee = employees.poll();
            logger.info(String.format(MENSAJE_LLAMADA_RECIBIDA, call.getId(), employee.getName()));
            TimeUnit.SECONDS.sleep(call.getDuration());
            logger.info(String.format(MENSAJE_LLAMADA_TERMINADA, call.getId(), call.getDuration()));
            employees.offer(employee);
        } catch (InterruptedException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
