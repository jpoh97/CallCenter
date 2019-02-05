package com.almundo.callcenter.model.employees;

/**
 * Enum con todos los cargos del call center, ademas de su prioridad al momento de recibir una llamada.
 */
public enum Charge {
    OPERATOR(1), SUPERVISOR(2), DIRECTOR(3);

    private final Integer priority;

    Integer getPriority() {
        return priority;
    }

    Charge(Integer priority) {
        this.priority = priority;
    }
}
