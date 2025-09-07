package org.example.iec61850.datatypes.controls;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.*;

/**
 * Класс - Controllable integer status - целочисленное управление и состояние
 */

@Getter @Setter
public class INC extends Data {
    private Originator origin = new Originator();
    private Attribute<Integer> ctlNum = new Attribute<>();
    private Attribute<Integer> stVal = new Attribute<>();
    private Quality q = new Quality();
    private Timestamp t = new Timestamp();
    private Attribute<Boolean> stSeld = new Attribute<>();
    private Attribute<Boolean> opRcvd = new Attribute<>();
    private Attribute<Boolean> opOk = new Attribute<>();
    private Timestamp tOpOk = new Timestamp();
}
