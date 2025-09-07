package org.example.iec61850.datatypes.controls;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс дублированного управления и состояния
 */

@Getter @Setter
public class DPC extends Data {
    private Attribute<Values> stVal = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();

    public enum Values{
        INTERMEDIATE_STATE, OFF, ON, BAD_STATE
    }
}
