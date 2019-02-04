package com.almundo.callcenter.employees;

public enum Charge {
    OPERATOR(1), SUPERVISOR(2), DIRECTOR(3);

    private Integer priority;

    Integer getPriority() {
        return priority;
    }

    Charge(Integer priority) {
        this.priority = priority;
    }
}
