package com.almundo.callcenter.model.employees.implementations;

import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.Employee;

/**
 * Clase que representa un Operador dentro del call center
 */
public class Operator extends Employee {

    public Operator(String name) {
        super(name, Charge.OPERATOR);
    }

}
