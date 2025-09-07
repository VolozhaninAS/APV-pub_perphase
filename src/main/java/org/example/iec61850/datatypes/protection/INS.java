package org.example.iec61850.datatypes.protection;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс - Integer status - целочисленное состояние
 */

@Getter @Setter
public class INS  extends Data {
    private Attribute<Integer> stVal = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();
}
