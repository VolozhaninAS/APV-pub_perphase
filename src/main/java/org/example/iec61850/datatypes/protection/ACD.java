package org.example.iec61850.datatypes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.Timestamp;

/**
 * Класс для представления сведений об активации направленной защиты
 */

@Getter
@Setter
public class ACD extends ACT {
    private Attribute<Boolean> general = new Attribute<>();
    private Attribute<Direction> dirGeneral = new Attribute<>();
    private Attribute<Boolean> phsA = new Attribute<>();
    private Attribute<Direction> dirPhsA = new Attribute<>();
    private Attribute<Boolean> phsB = new Attribute<>();
    private Attribute<Direction> dirPhsB = new Attribute<>();
    private Attribute<Boolean> phsC = new Attribute<>();
    private Attribute<Direction> dirPhsC = new Attribute<>();
    private Attribute<Boolean> neut = new Attribute<>();
    private Attribute<Direction> dirNeut = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();

    public enum Direction {
        UNKNOWN, FORWARD, BACKWARD, BOTH
    }
}
