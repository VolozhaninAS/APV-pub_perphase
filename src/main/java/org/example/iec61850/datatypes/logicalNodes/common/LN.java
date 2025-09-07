package org.example.iec61850.datatypes.logicalNodes.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Базовый класс для всех логических узлов
 */

@Getter @Setter
public abstract class LN {
    private String pref; // Пишется в названии перед узлом (название для релейщика)
    private String clazz;
    private int inst;

    public abstract void process();
}
