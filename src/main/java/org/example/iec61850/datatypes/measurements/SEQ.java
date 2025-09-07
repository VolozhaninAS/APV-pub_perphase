package org.example.iec61850.datatypes.measurements;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс для описания последовательностей
 */
@Getter
@Setter
public class SEQ {
    private CMV c1 = new CMV();
    private CMV c2 = new CMV();
    private CMV c3 = new CMV();

    public enum seqT {
        POS, NEG, ZERO
    }
}
