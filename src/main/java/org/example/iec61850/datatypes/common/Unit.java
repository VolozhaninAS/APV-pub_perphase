package org.example.iec61850.datatypes.common;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Unit extends Data{
    private Attribute<Units> SIUnit = new Attribute<>(); // Физические величины
    private Attribute<Multiplier> multiplier = new Attribute<>(); // Множители

    public enum Units{
        AMPERE, VOLT, SECOND, DEGREES, RADIAN, OHM, HERTZ,
        VOLT_AMPERE, WATTS, VOLT_AMPERE_REACTIVE
    }

    public enum Multiplier{
        YOCTO, ZEPTO, ATTO, FEMTO, PICO, NANO, MICRO, MILLI, CENTI, DECI,
        DECA, HECTO, KILO, MEGA, GIGA, TERA, PETA, EXA, ZETTA, YOTTA
    }
}
