package com.almundo.callcenter.customerservices;

import com.almundo.callcenter.model.call.Call;
import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.EmployeeFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests para probar la clase Dispatcher. Utiliza el patron 3A (Arrange, Act, Assert).
 */
public class DispatcherTest {

    private static final String OPERATOR_NAME = "Operator #%d";
    private static final String SUPERVISOR_NAME = "Supervisor #%d";
    private static final String DIRECTOR_NAME = "Director #%d";

    private static Dispatcher DISPATCHER;

    @Before
    public void setUp() {
        DISPATCHER = Dispatcher.getSingletonInstance();
    }

    @After
    public void tearDown() {
        DISPATCHER = null;
    }

    @Test
    public void testGetSingletonInstanceNonNullity() {
        assertNotNull("Singleton can not return null", Dispatcher.getSingletonInstance());
    }

    @Test
    public void testGetSingletonInstanceConsistent() {
        Dispatcher dispatcher1 = Dispatcher.getSingletonInstance();
        Dispatcher dispatcher2 = Dispatcher.getSingletonInstance();
        assertEquals("Dispatcher must be same instance because implement singleton pattern", dispatcher1, dispatcher2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEmployeesImmutableCollection() {
        DISPATCHER.getEmployees().clear();
        Assert.fail();
    }

    @Test
    public void testAddEmployeeSevenOperatorsTwoSupervisorAndOneDirector() {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);

        // Assert
        assertEquals("Employees array must have 10 elements", OPERATORS + SUPERVISORS + DIRECTORS, DISPATCHER.getEmployees().size());

        assertEquals("Employees array must have 7 operators", OPERATORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.OPERATOR)).collect(Collectors.toList()).size());
        assertEquals("Employees array must have 2 supervisors", SUPERVISORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.SUPERVISOR)).collect(Collectors.toList()).size());
        assertEquals("Employees array must have 1 director", DIRECTORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.DIRECTOR)).collect(Collectors.toList()).size());
    }

    @Test
    public void testDispatchCallTenConcurrentCalls() throws InterruptedException, ExecutionException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int CALLS = 10;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        List<Future<Call>> futureCalls = executorService.invokeAll(calls);

        // Assert
        assertEquals("Concurrent calls must be 10", NUMBER_OF_CONCURRENT_CALLS, futureCalls.size());
        for (Future<Call> futureCall : futureCalls) {
            assertNotNull("Future cannot return null", futureCall.get());
        }
    }

    /**
     * PUNTO EXTRA
     * Debido a lo importante que es contestar todas las llamadas que lleguen, el algoritmo tiene la capacidad de recibir
     * todas las llamadas que lleguen, asi sobrepase la capacidad maxima de 10 llamadas concurrentes. En este caso los
     * 10 empleados tomaran las primeras 10 llamadas de manera concurrente, y a medida que vaya terminando un empleado
     * continuara con las llamadas que esten encoladas.
     * Esto se logro gracias a la estructura de datos seleccionada para los empleados, ya que estos se almacenan en una
     * PriorityBlockingQueue que bloquea la llamada cuando se invoca al metodo dispatchCall en caso de no haber empleados
     * disponibles para contestar la llamada. Esta estructura de datos viene del paquete java.util.concurrent
     * @throws InterruptedException Excepcion que se puede provocar al llamar el metodo invokeAll del executorService
     * @throws ExecutionException Excepcion se puede provacar al hacer futureCall.get()
     */
    @Test
    public void testDispatchCallFifteenCallsWithTenConcurrentCalls() throws InterruptedException, ExecutionException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int NUMBER_CALLS = 15;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(NUMBER_CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        List<Future<Call>> futureCalls = executorService.invokeAll(calls);

        // Assert
        assertEquals("Number of calls must be 15", NUMBER_CALLS, futureCalls.size());
        for (Future<Call> futureCall : futureCalls) {
            assertNotNull("Future cannot return null", futureCall.get());
        }
    }

    @Test
    public void testDispatchCallTenConcurrentCallsAllEmployeesBusy() throws InterruptedException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int NUMBER_CALLS = 10;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(NUMBER_CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        for (Callable<Call> call : calls) {
            executorService.submit(call);
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // Assert
        assertEquals("All employees are busy, none is available",0, DISPATCHER.getEmployees().size());
    }


    @Test
    public void testDispatchCallSevenConcurrentCallsAllOperatorsBusy() throws InterruptedException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int NUMBER_CALLS = 7;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(NUMBER_CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        for (Callable<Call> call : calls) {
            executorService.submit(call);
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // Assert
        assertEquals("2 supervisors and 1 director are available",3, DISPATCHER.getEmployees().size());
        assertEquals("There are not operators available",0, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.OPERATOR)).collect(Collectors.toList()).size());
        assertEquals("There is two supervisors available",2, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.SUPERVISOR)).collect(Collectors.toList()).size());
        assertEquals("There is one director available",1, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.DIRECTOR)).collect(Collectors.toList()).size());
    }

    @Test
    public void testDispatchCallEightConcurrentCallsAllOperatorsAndOneSupervisorBusy() throws InterruptedException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int NUMBER_CALLS = 8;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(NUMBER_CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        for (Callable<Call> call : calls) {
            executorService.submit(call);
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // Assert
        assertEquals("1 supervisors and 1 director are available",2, DISPATCHER.getEmployees().size());
        assertEquals("There are not operators available",0, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.OPERATOR)).collect(Collectors.toList()).size());
        assertEquals("There is one supervisor available",1, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.SUPERVISOR)).collect(Collectors.toList()).size());
        assertEquals("There is one director available",1, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.DIRECTOR)).collect(Collectors.toList()).size());
    }

    @Test
    public void testDispatchCallNineConcurrentCallsAllOperatorsAndSupervisorsBusy() throws InterruptedException {
        // Arrange
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;
        final int NUMBER_CALLS = 9;
        final int NUMBER_OF_CONCURRENT_CALLS = 10;

        // Act
        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);
        List<Callable<Call>> calls = createCalls(NUMBER_CALLS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CALLS);

        for (Callable<Call> call : calls) {
            executorService.submit(call);
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // Assert
        assertEquals("1 supervisors and 1 director are available",1, DISPATCHER.getEmployees().size());
        assertEquals("There are not operators available",0, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.OPERATOR)).collect(Collectors.toList()).size());
        assertEquals("There are not supervisors available",0, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.SUPERVISOR)).collect(Collectors.toList()).size());
        assertEquals("There is one director available",1, DISPATCHER.getEmployees().stream()
                .filter(employee -> employee.getCharge().equals(Charge.DIRECTOR)).collect(Collectors.toList()).size());
    }

    private void addOperators(int quantity) {
        for (int i = 0; i < quantity; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.of(String.format(OPERATOR_NAME, i), Charge.OPERATOR));
        }
    }

    private void addSupervisor(int quantity) {
        for (int i = 0; i < quantity; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.of(String.format(SUPERVISOR_NAME, i), Charge.SUPERVISOR));
        }
    }

    private void addDirectors(int quantity) {
        for (int i = 0; i < quantity; i++) {
            DISPATCHER.addEmployee(EmployeeFactory.of(String.format(DIRECTOR_NAME, i), Charge.DIRECTOR));
        }
    }

    private static List<Callable<Call>> createCalls(int quantity) {
        List<Callable<Call>> calls = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            calls.add(new Call(i));
        }
        return calls;
    }
}