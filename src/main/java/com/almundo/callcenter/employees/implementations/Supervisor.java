package com.almundo.callcenter.employees.implementations;

import com.almundo.callcenter.employees.Charge;
import com.almundo.callcenter.employees.Employee;

public class Supervisor extends Employee {

    public Supervisor(String name) {
        super(name, Charge.SUPERVISOR);
    }

}
