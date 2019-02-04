package com.almundo.callcenter.model.employees;

import com.almundo.callcenter.model.employees.implementations.Director;
import com.almundo.callcenter.model.employees.implementations.Operator;
import com.almundo.callcenter.model.employees.implementations.Supervisor;

/**
 * Implementacion del patron factory para obtener un nuevo objeto dependiendo del cargo solicitado
 */
public class EmployeeFactory {

    public static Employee getEmployee(String name, Charge charge) {
        Employee employee = null;
        switch (charge) {
            case OPERATOR:
                employee = new Operator(name);
                break;
            case SUPERVISOR:
                employee = new Supervisor(name);
                break;
            case DIRECTOR:
                employee = new Director(name);
                break;
        }
        return employee;
    }
}
