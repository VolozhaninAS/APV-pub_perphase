package org.example.iec61850.datatypes.utils;

import org.example.iec61850.datatypes.measurements.SAV;
import org.example.iec61850.datatypes.measurements.Vector;

/**
 * Базовый класс для всех видов фильтров
 */

public abstract class Filter {
    public abstract void process(SAV measuredValue, Vector result);
}
