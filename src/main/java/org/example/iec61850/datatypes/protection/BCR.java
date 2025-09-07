package org.example.iec61850.datatypes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс для считывания значений двоичного счетчика
 */

@Getter @Setter
public class BCR extends Data {
    private Attribute<Integer> actVal = new Attribute<>();
    private Attribute<Integer> frVal = new Attribute<>();
    private Timestamp frTm = new Timestamp();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();
}
