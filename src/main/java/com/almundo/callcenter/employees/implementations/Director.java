package com.almundo.callcenter.employees.implementations;

import com.almundo.callcenter.employees.Charge;
import com.almundo.callcenter.employees.Employee;

public class Director extends Employee {

    public Director(String name) {
        super(name, Charge.DIRECTOR);
    }

}
