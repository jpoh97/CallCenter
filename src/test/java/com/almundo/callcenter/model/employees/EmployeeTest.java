package com.almundo.callcenter.model.employees;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests para probar las validaciones de la clase empleado
 */
public class EmployeeTest {

    /**
     * Se verifica que no se puede crear un empleado sin nombre
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutName() {
        Employee employee = new Employee(null, Charge.OPERATOR) {};
        Assert.fail("Employee with null name");
    }

    /**
     * Se verifica que no se puede crear un empleado sin cargo
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutCharge() {
        Employee employee = new Employee("Employee name", null) {};
        Assert.fail("Employee with null charge");
    }

    /**
     * Se verifica que se puede crear un empleado si tiene nombre y cargo
     */
    @Test
    public void testConstructorWithArguments() {
        Employee employee = new Employee("Employee name", Charge.OPERATOR) {};
        assertNotNull("Employee has all arguments", employee);
    }

    /**
     * Se verifica que 2 empleados tienen la misma prioridad al contestar lklamadas si tienen el mismo cargo
     */
    @Test
    public void testCompareTwoOperators() {
        Employee employee1 = new Employee("Employee name", Charge.OPERATOR) {};
        Employee employee2 = new Employee("Employee name", Charge.OPERATOR) {};
        assertEquals("Operators have the same priority", 0, employee1.compareTo(employee2));

        employee1 = new Employee("Employee name", Charge.SUPERVISOR) {};
        employee2 = new Employee("Employee name", Charge.SUPERVISOR) {};
        assertEquals("Operators have the same priority", 0, employee1.compareTo(employee2));

        employee1 = new Employee("Employee name", Charge.DIRECTOR) {};
        employee2 = new Employee("Employee name", Charge.DIRECTOR) {};
        assertEquals("Operators have the same priority", 0, employee1.compareTo(employee2));
    }

    /**
     * Se verifica que un operador tiene una prioridad menor que el supervisor, por lo tanto el operador contesta
     * llamadas antes que el supervisor
     */
    @Test
    public void testCompareOperatorAndSupervisor() {
        Employee employee1 = new Employee("Employee name", Charge.OPERATOR) {};
        Employee employee2 = new Employee("Employee name", Charge.SUPERVISOR) {};
        assertEquals("Supervisor have more priority than operator", -1, employee1.compareTo(employee2));
    }

    /**
     * Se verifica que un supervisor tiene una prioridad menor que el director, por lo tanto el supervisor contesta
     * llamadas antes que el director
     */
    @Test
    public void testCompareDirectorAndSupervisor() {
        Employee employee1 = new Employee("Employee name", Charge.DIRECTOR) {};
        Employee employee2 = new Employee("Employee name", Charge.SUPERVISOR) {};
        assertEquals("Director have more priority than supervisor", 1, employee1.compareTo(employee2));
    }

}