package com.almundo.callcenter.model.employees.implementations;

import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.Employee;

/**
 * Clase que representa un Director dentro del call center
 */
public class Director extends Employee {

    public Director(String name) {
        super(name, Charge.DIRECTOR);
    }

}
