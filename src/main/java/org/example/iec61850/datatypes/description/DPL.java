package org.example.iec61850.datatypes.description;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс - паспортная табличка устройства
 */

@Getter @Setter
public class DPL extends Data {
    private Attribute<String> vendor = new Attribute<>();
}
