package com.almundo.callcenter.model.employees.implementations;

import com.almundo.callcenter.model.employees.Charge;
import com.almundo.callcenter.model.employees.Employee;

public class Supervisor extends Employee {

    public Supervisor(String name) {
        super(name, Charge.SUPERVISOR);
    }

}
