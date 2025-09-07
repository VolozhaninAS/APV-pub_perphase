package org.example.iec61850.datatypes.protection;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс - Single point status - недублированное состояние
 */

@Getter @Setter
public class SPS extends Data {
    private Attribute<Boolean> stVal = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();
}
