package org.example.iec61850.datatypes.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс для настройки кривой
 */

@Getter @Setter
public class CURVE extends Data {
    private Attribute<Float> setParA = new Attribute<>();
    private Attribute<Float> setParB = new Attribute<>();
    private Attribute<Float> setParC = new Attribute<>();
    private Attribute<Float> setParD = new Attribute<>();
    private Attribute<Float> setParE = new Attribute<>();
    private Attribute<Float> setParF = new Attribute<>();
}
