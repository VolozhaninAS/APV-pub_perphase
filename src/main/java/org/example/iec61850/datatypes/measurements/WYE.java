package org.example.iec61850.datatypes.measurements;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Data;

/**
 * Класс для описания трехфазного сигнала в комплексном виде
 */

@Getter @Setter
public class WYE extends Data {
    private CMV phsA = new CMV();
    private CMV phsB = new CMV();
    private CMV phsC = new CMV();
    private CMV neut = new CMV();
    private CMV net = new CMV();
    private CMV res = new CMV();
    private Attribute<DEL.AngRef> angRef = new Attribute<>();
    private Attribute<Boolean> phsToNeut = new Attribute<>(false);
    private Attribute<String> d = new Attribute<>();
    private Attribute<Character.UnicodeBlock> dU = new Attribute<>();
    private Attribute<String> cdcNs = new Attribute<>();
    private Attribute<String> cdcName = new Attribute<>();
    private Attribute<String> dataNs = new Attribute<>();
}