package com.almundo.callcenter.customerservices;

import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.EmployeeFactory;
import com.almundo.callcenter.model.employees.implementations.Supervisor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

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
        assertNotNull(Dispatcher.getSingletonInstance());
    }

    @Test
    public void testGetSingletonInstanceConsistent() {
        Dispatcher dispatcher1 = Dispatcher.getSingletonInstance();
        Dispatcher dispatcher2 = Dispatcher.getSingletonInstance();
        assertEquals("Dispatcher must be same instance because implement singleton pattern", dispatcher1, dispatcher2);
    }

    @Test
    public void testAddEmployeeSevenOperatorsTwoSupervisorAndOneDirector() {
        final int OPERATORS = 7;
        final int SUPERVISORS = 2;
        final int DIRECTORS = 1;

        addOperators(OPERATORS);
        addSupervisor(SUPERVISORS);
        addDirectors(DIRECTORS);

        assertEquals("Employees array must have 10 elements", OPERATORS + SUPERVISORS + DIRECTORS, DISPATCHER.getEmployees().size());

        assertEquals("Employees array must have 7 operators", OPERATORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.OPERATOR)).collect(Collectors.toList()).size());
        assertEquals("Employees array must have 2 supervisors", SUPERVISORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.SUPERVISOR)).collect(Collectors.toList()).size());
        assertEquals("Employees array must have 1 director", DIRECTORS, DISPATCHER.getEmployees().stream()
                .filter(e -> e.getCharge().equals(Charge.DIRECTOR)).collect(Collectors.toList()).size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEmployeesImmutableCollection() {
        DISPATCHER.getEmployees().clear();
        Assert.fail();
    }

    @Test
    public void dispatchCall() {
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
}