package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс содержит значение переменной любого типа
 */

@Getter @Setter
public class Attribute<T> extends Data {
    private T value;

    public Attribute() {
    }

    public Attribute(T value) {
    }
}
