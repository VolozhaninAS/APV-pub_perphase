package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Родительский класс для всех остальных
 */

@Getter @Setter
public class Data {
    private String name;
    private String ref;
}

