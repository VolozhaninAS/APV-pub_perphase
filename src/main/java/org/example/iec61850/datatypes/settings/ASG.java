package org.example.iec61850.datatypes.settings;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Unit;
import org.example.iec61850.datatypes.measurements.AnalogueValue;
/**
 * Класс для задания значения аналогового сигнала
 */
@Getter @Setter
public class ASG extends Data {
    private AnalogueValue setMag = new AnalogueValue();
    private Unit units = new Unit();
    private AnalogueValue minVal = new AnalogueValue();
    private AnalogueValue maxVal = new AnalogueValue();
    private AnalogueValue stepSize = new AnalogueValue();
}
