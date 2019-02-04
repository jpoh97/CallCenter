package com.almundo.callcenter.employees.implementations;

import com.almundo.callcenter.employees.Charge;
import com.almundo.callcenter.employees.Employee;

public class Operator extends Employee {

    public Operator(String name) {
        super(name, Charge.OPERATOR);
    }

}
