package org.example.iec61850.datatypes.protection;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс - Enumerated status
 */

@Getter @Setter
public class ENS extends Data {
    private Attribute<Values> stVal = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();

    private enum Values{}
}
