package org.example.iec61850.datatypes.description;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;
import org.example.iec61850.datatypes.common.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для описания формы кривой
 */

@Getter @Setter
public class CSD extends Data {
    private Unit xUnits = new Unit();
    private Attribute<String> xD = new Attribute<>();
    private Attribute<Character.UnicodeBlock> xDU = new Attribute<>();
    private Unit yUnits = new Unit();
    private Attribute<String> yD = new Attribute<>();
    private Attribute<Character.UnicodeBlock> yDU = new Attribute<>();
    private Unit zUnits = new Unit();
    private Attribute<String> zD = new Attribute<>();
    private Attribute<Character.UnicodeBlock> zDU = new Attribute<>();
    private Attribute<Integer> numPts = new Attribute<>();
    private List<Double> crvPts = new ArrayList<>();
    private Attribute<String> d = new Attribute<>();
    private Attribute<Character.UnicodeBlock> dU = new Attribute<>();
    private Attribute<String> cdcNs = new Attribute<>();
    private Attribute<String> cdcName = new Attribute<>();
    private Attribute<String> dataNs = new Attribute<>();
}
