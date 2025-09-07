package org.example.iec61850.datatypes.settings;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс - integer status setting - установка состояния целочисленная
 */

@Getter @Setter
public class ING extends Data {
    private Attribute<Integer> setVal = new Attribute<>();
}
