package org.example.iec61850.datatypes.utils;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measurements.SAV;
import org.example.iec61850.datatypes.measurements.Vector;
import org.example.iec61850.datatypes.settings.ING;

/**
 * Класс для реализации фильтра Фурье
 */

public class Fourier extends Filter{
    private ING bSize = new ING();
    private final SAV[] buffer;
    public Attribute<Integer> bCount = new Attribute<>();
    public Attribute<Double> rVal = new Attribute<>();
    public Attribute<Double> imVal = new Attribute<>();
    public Attribute<Double> freq = new Attribute<>();
    public Attribute<Double> dT = new Attribute<>();

    public Fourier(int bufferSize){
        bSize.getSetVal().setValue(bufferSize);
        bCount.setValue(0);
        rVal.setValue(0D);
        imVal.setValue(0D);
        freq.setValue(50D);
        dT.setValue(0.02 / bSize.getSetVal().getValue());
        buffer = new SAV[bSize.getSetVal().getValue()];

        for (int i = 0; i < bSize.getSetVal().getValue(); i++) {
            SAV tempVal = new SAV();
            tempVal.getInstMag().getF().setValue(0D);
            buffer[i] = tempVal;
        }
    }

    @Override
    public void process(SAV measuredValue, Vector result) {

        rVal.setValue(rVal.getValue()
                + (measuredValue.getInstMag().getF().getValue()
                - buffer[bCount.getValue()].getInstMag().getF().getValue())
                * Math.sin(2 * Math.PI * freq.getValue() * bCount.getValue() * dT.getValue())
                * 2 / bSize.getSetVal().getValue()
        );
        imVal.setValue(imVal.getValue()
                + (measuredValue.getInstMag().getF().getValue()
                - buffer[bCount.getValue()].getInstMag().getF().getValue())
                * Math.cos(2 * Math.PI * freq.getValue() * bCount.getValue() * dT.getValue())
                * 2 / bSize.getSetVal().getValue()
        );

        result.getMag().getF().setValue(
                0.7071068 * Math.sqrt(Math.pow(rVal.getValue(), 2) + Math.pow(imVal.getValue(), 2))
        );

        if ( rVal.getValue() <0){
            result.getAng().getF().setValue(
                    Math.atan(imVal.getValue() / rVal.getValue()) * 180 / Math.PI -180 );
        }
        else {
            result.getAng().getF().setValue(
                    Math.atan(imVal.getValue() / rVal.getValue()) * 180 / Math.PI
            );
        }

        buffer[bCount.getValue()].getInstMag().getF().setValue(measuredValue.getInstMag().getF().getValue());
        bCount.setValue(bCount.getValue() + 1);
        if (bCount.getValue() >= bSize.getSetVal().getValue()){
            bCount.setValue(0);
        }
    }
}
