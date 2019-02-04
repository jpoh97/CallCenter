package com.almundo.callcenter.model.employees.implementations;

import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.Employee;

public class Operator extends Employee {

    public Operator(String name) {
        super(name, Charge.OPERATOR);
    }

}
