package org.example.iec61850.datatypes.measurements;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс для представления выборок мгновенных значений аналоговых сигналов
 */

@Getter @Setter
public class SAV extends Data {
    private AnalogueValue instMag = new AnalogueValue();
    private Quality q  = new Quality();
    private Timestamp t = new Timestamp();
}
