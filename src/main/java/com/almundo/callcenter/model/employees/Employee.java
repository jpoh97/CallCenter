package com.almundo.callcenter.model.employees;

/**
 * Clase abstracta que representa un empleado. Esta clase es heredada por otras. Implementa la interfaz Comparable ya
 * que se necesita para que el PriorityBlockingQueue sepa como organizar cada empleado dentro de la cola dependiendo de
 * la prioridad del cargo
 */
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

    public int compareTo(Employee employee) {
        return this.getCharge().getPriority().compareTo(employee.getCharge().getPriority());
    }
}
