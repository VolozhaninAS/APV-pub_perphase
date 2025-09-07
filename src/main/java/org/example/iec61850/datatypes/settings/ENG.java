package org.example.iec61850.datatypes.settings;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс - Enumerated Status Setting (строка состояния в виде перечисления)
 */

@Getter @Setter
public class ENG extends Data {
    private Attribute<Enumerated> setVal = new Attribute<>();
    private enum Enumerated{}
}
