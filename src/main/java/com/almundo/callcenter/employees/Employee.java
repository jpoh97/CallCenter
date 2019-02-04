package com.almundo.callcenter.employees;

public abstract class Employee implements Comparable<Employee>  {

    private final String name;
    private final Charge charge;

    public Employee(String name, Charge charge) {
        this.name = name;
        this.charge = charge;
    }

    public String getName() {
        return name;
    }

    private Charge getCharge() {
        return charge;
    }

    @Override
    public int compareTo(Employee employee) {
        return this.getCharge().getPriority().compareTo(employee.getCharge().getPriority());
    }
}
