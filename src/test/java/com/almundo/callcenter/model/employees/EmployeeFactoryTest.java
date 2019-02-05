package com.almundo.callcenter.model.employees;

import com.almundo.callcenter.model.employees.implementations.Director;
import com.almundo.callcenter.model.employees.implementations.Operator;
import com.almundo.callcenter.model.employees.implementations.Supervisor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests para probar la fabrica de empleados
 */
public class EmployeeFactoryTest {

    /**
     * Se verifica que la fabrica retorne un empleado operador
     */
    @Test
    public void testReturnOperator() {
        Employee employee = EmployeeFactory.of("Operator name", Charge.OPERATOR);
        assertTrue("Employee must be operator", employee instanceof Operator);
    }

    /**
     * Se verifica que la fabrica retorne un empleado supervisor
     */
    @Test
    public void testReturnSupervisor() {
        Employee employee = EmployeeFactory.of("Operator name", Charge.SUPERVISOR);
        assertTrue("Employee must be supervisor", employee instanceof Supervisor);
    }

    /**
     * Se verifica que la fabrica retorne un empleado director
     */
    @Test
    public void testReturnDirector() {
        Employee employee = EmployeeFactory.of("Operator name", Charge.DIRECTOR);
        assertTrue("Employee must be director", employee instanceof Director);
    }
}