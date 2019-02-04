package com.almundo.callcenter.employees;

import com.almundo.callcenter.employees.implementations.Director;
import com.almundo.callcenter.employees.implementations.Operator;
import com.almundo.callcenter.employees.implementations.Supervisor;

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
