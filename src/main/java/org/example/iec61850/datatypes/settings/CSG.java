package org.example.iec61850.datatypes.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для настройки формы кривой
 */

@Getter @Setter
public class CSG extends Data {
    private Attribute<Float> pointZ = new Attribute<>();
    private Attribute<Integer> numPts = new Attribute<>();
    private List<Double> crvPts = new ArrayList<>();
}
