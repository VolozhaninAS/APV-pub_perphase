package org.example.iec61850.datatypes.measurements;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс для представления набора междуфазных измеренных значений
 */

@Getter @Setter
public class DEL extends Data {
    private CMV phsAB = new CMV();
    private CMV phsBC = new CMV();
    private CMV phsCA = new CMV();
    private Attribute<AngRef> angRef = new Attribute<>();
    private Attribute<String> d = new Attribute<>();
    private Attribute<Character.UnicodeBlock> dU = new Attribute<>();
    private Attribute<String> cdcNs = new Attribute<>();
    private Attribute<String> cdcName = new Attribute<>();
    private Attribute<String> dataNs = new Attribute<>();

    public enum AngRef {
        Va, Vb, Vc, Aa, Ab, Ac, Vab, Vbc,
        Vca, Vother, Aother, Synchrophasor
    }
}
