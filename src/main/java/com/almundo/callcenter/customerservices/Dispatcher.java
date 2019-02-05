package com.almundo.callcenter.customerservices;

import com.almundo.callcenter.model.call.Call;
import com.almundo.callcenter.model.employees.Employee;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dispatcher tiene la funcion de coordinar cada las llamadas que entran. Esta clase implementa el patron Singleton.
 * Se utilizo un PriorityBlockingQueue para manejar el orden en el cual se reciben las llamadas dependiendo del cargo.
 * Con employees.poll() se asegura que si no hay empleados disponibles, es decir que los 10 empleados estan ocupados
 * y llega una onceava llamada, el sistema esperara hasta que se encuentre un empleado disponible, ya que el
 * PriorityBlockingQueue bloquea el hilo hasta que exista un empleado a retornar
 */
public class Dispatcher {

    private static final String MESSAGE_CALL_RECEIVED = "La llamada #%d sera contestada por %s";
    private static final String MESSAGE_CALL_FINISHED = "Llamada #%d ha terminado y duro %d segundos";
    private static final String WAITING_MESSAGE = "Todos los empleados estan ocupados, la llamada #%d sera contestada cuando se encuentre alguno disponible";

    private PriorityBlockingQueue<Employee> employees = new PriorityBlockingQueue<>();
    private AtomicInteger numberOfConcurrentCalls = new AtomicInteger(0);

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

    Collection<Employee> getEmployees() {
        return Collections.unmodifiableCollection(employees);
    }

    void clearEmployeesQueue() {
        numberOfConcurrentCalls = new AtomicInteger(0);
        this.employees.clear();
    }

    public void dispatchCall(Call call) throws InterruptedException {
        numberOfConcurrentCalls.incrementAndGet();
        if (numberOfConcurrentCalls.get() > 10) {
            logger.info(String.format(WAITING_MESSAGE, call.getId()));
        }
        Employee employee = employees.take();
        logger.info(String.format(MESSAGE_CALL_RECEIVED, call.getId(), employee.getName()));
        TimeUnit.SECONDS.sleep(call.getDuration());
        logger.info(String.format(MESSAGE_CALL_FINISHED, call.getId(), call.getDuration()));
        employees.offer(employee);
        numberOfConcurrentCalls.decrementAndGet();
    }
}
