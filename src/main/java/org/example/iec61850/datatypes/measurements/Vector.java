package org.example.iec61850.datatypes.measurements;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс для описания векторных значений (амплитуда + фаза)
 */

@Getter @Setter
public class Vector extends Data {

    private AnalogueValue mag = new AnalogueValue();
    private AnalogueValue ang = new AnalogueValue();
}
