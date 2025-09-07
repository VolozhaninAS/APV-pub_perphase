package org.example.iec61850.datatypes.logicalNodes.measurements;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.DEL;
import org.example.iec61850.datatypes.measurements.SAV;
import org.example.iec61850.datatypes.measurements.SEQ;
import org.example.iec61850.datatypes.measurements.WYE;

import static java.lang.Math.*;

/**
 * Класс для описания узла получения последовательностей
 */
@Getter @Setter
public class MSQI extends LN {
    private SEQ SeqA = new SEQ();
    private SEQ SeqV = new SEQ();
    private SEQ DQ0Seq = new SEQ();

    private WYE ImbA = new WYE();

    private SAV ImbNgA = new SAV();
    private SAV ImbNgV = new SAV();

    private DEL ImbPPV = new DEL();

    private WYE ImbV = new WYE();

    private SAV ImbZroA = new SAV();
    private SAV ImbZroV = new SAV();
    private SAV MaxImbA = new SAV();
    private SAV MaxImbPPV = new SAV();
    private SAV MaxImbV = new SAV();


//    Входные данные: мгновенные значения токов и напряжений

    public WYE A = new WYE();
    public WYE PNV = new WYE();


    @Override
    public void process() {
        double MagIa = A.getPhsA().getCVal().getMag().getF().getValue();
        double MagIb = A.getPhsB().getCVal().getMag().getF().getValue();
        double MagIc = A.getPhsC().getCVal().getMag().getF().getValue();

        double AngIa = toRadians(A.getPhsA().getCVal().getAng().getF().getValue());
        double AngIb = toRadians(A.getPhsB().getCVal().getAng().getF().getValue());
        double AngIc = toRadians(A.getPhsC().getCVal().getAng().getF().getValue());

        double MagUa = PNV.getPhsA().getCVal().getMag().getF().getValue();
        double MagUb = PNV.getPhsB().getCVal().getMag().getF().getValue();
        double MagUc = PNV.getPhsC().getCVal().getMag().getF().getValue();

        double AngUa = toRadians(PNV.getPhsA().getCVal().getAng().getF().getValue());
        double AngUb = toRadians(PNV.getPhsB().getCVal().getAng().getF().getValue());
        double AngUc = toRadians(PNV.getPhsC().getCVal().getAng().getF().getValue());

        ImbPPV.getPhsAB().getCVal().getMag().getF().setValue(
                sqrt( pow(MagUa * cos(AngUa) + MagUb * cos(AngUb), 2) + pow(MagUa * sin(AngUa) + MagUb * sin(AngUb) , 2))
        );
        ImbPPV.getPhsAB().getCVal().getAng().getF().setValue(
                atan((MagUa * sin(AngUa) + MagUb * sin(AngUb)) / (MagUa * cos(AngUa) + MagUb * cos(AngUb))) * 180 / PI
        );
        ImbPPV.getPhsBC().getCVal().getMag().getF().setValue(
                sqrt( pow(MagUb * cos(AngUb) + MagUc * cos(AngUc), 2) + pow(MagUb * sin(AngUb) + MagUc * sin(AngUc) , 2))
        );
        ImbPPV.getPhsBC().getCVal().getAng().getF().setValue(
                atan((MagUb * sin(AngUb) + MagUc * sin(AngUc)) / (MagUb * cos(AngUb) + MagUc * cos(AngUc))) * 180 / PI
        );
        ImbPPV.getPhsCA().getCVal().getMag().getF().setValue(
                sqrt( pow(MagUc * cos(AngUc) + MagUa * cos(AngUa), 2) + pow(MagUc * sin(AngUc) + MagUa * sin(AngUa) , 2))
        );
        ImbPPV.getPhsCA().getCVal().getAng().getF().setValue(
                atan((MagUc * sin(AngUc) + MagUa * sin(AngUa)) / (MagUc * cos(AngUc) + MagUa * cos(AngUa))) * 180 / PI
        );


        SeqA.getC3().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagIa * cos(AngIa) + MagIb * cos(AngIb) + MagIc * cos(AngIc), 2)) + pow(MagIa * sin(AngIa) + MagIb * sin(AngIb) + MagIc * sin(AngIc), 2))));
        SeqA.getC3().getCVal().getAng().getF().setValue(atan(toRadians(MagIa * sin(AngIa) + MagIb * sin(AngIb) + MagIc * sin(AngIc) / (MagIa * cos(AngIa) + MagIb * cos(AngIb) + MagIc * cos(AngIc)))));

        SeqA.getC1().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagIa * cos(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 120)[0] +
                rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 240)[0], 2)) +
                pow(MagIa * sin(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 120)[1] +
                        rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 240)[1], 2))));

        SeqA.getC1().getCVal().getAng().getF().setValue(atan(toRadians(
                (MagIa * sin(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 120)[1] +
                        rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 240)[1]) /
                        (MagIa * cos(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 120)[0] +
                                rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 240)[0])
        )));

        SeqA.getC2().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagIa * cos(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 240)[0] +
                rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 120)[0], 2)) +
                pow(MagIa * sin(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 240)[1] +
                        rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 120)[1], 2))));

        SeqA.getC2().getCVal().getAng().getF().setValue(atan(toRadians(
                (MagIa * sin(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 240)[1] +
                        rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 120)[1]) /
                        (MagIa * cos(AngIa) + rotateVector(MagIb * cos(AngIb), MagIb * sin(AngIb), 240)[0] +
                                rotateVector(MagIc * cos(AngIc), MagIc * sin(AngIc), 120)[0])
        )));



        SeqV.getC3().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagUa * cos(AngUa) + MagUb * cos(AngUb) + MagUc * cos(AngUc), 2)) + pow(MagUa * sin(AngUa) + MagUb * sin(AngUb) + MagUc * sin(AngUc), 2))));
        SeqV.getC3().getCVal().getAng().getF().setValue(atan(toRadians(MagUa * sin(AngUa) + MagUb * sin(AngUb) + MagUc * sin(AngUc) / (MagUa * cos(AngUa) + MagUb * cos(AngUb) + MagUc * cos(AngUc)))));

        SeqV.getC1().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagUa * cos(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 120)[0] +
                rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 240)[0], 2)) +
                pow(MagUa * sin(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 120)[1] +
                        rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 240)[1], 2))));

        SeqV.getC1().getCVal().getAng().getF().setValue(atan(toRadians(
                (MagUa * sin(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 120)[1] +
                        rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 240)[1]) /
                        (MagUa * cos(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 120)[0] +
                                rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 240)[0])
        )));

        SeqV.getC2().getCVal().getMag().getF().setValue(1.0 / 3.0 * (sqrt((pow(MagUa * cos(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 240)[0] +
                rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 120)[0], 2)) +
                pow(MagUa * sin(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 240)[1] +
                        rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 120)[1], 2))));

        SeqV.getC2().getCVal().getAng().getF().setValue(atan(toRadians(
                (MagUa * sin(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 240)[1] +
                        rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 120)[1]) /
                        (MagUa * cos(AngUa) + rotateVector(MagUb * cos(AngUb), MagUb * sin(AngUb), 240)[0] +
                                rotateVector(MagUc * cos(AngUc), MagUc * sin(AngUc), 120)[0])
        )));

    }


    public static double[] rotateVector(double x, double y, double angle) {
        double[] rotatedVector = new double[3];

        double sin = Math.sin(toRadians(angle));
        double cos = Math.cos(toRadians(angle));

        double oldx = x;
        double oldy = y;

        x = oldx * cos - oldy * sin;
        y = oldx * sin + oldy * cos;

        double angleAfterRotation = Math.atan2(y, x) * 180 / Math.PI;

        rotatedVector[0] = x;
        rotatedVector[1] = y;
        rotatedVector[2] = angleAfterRotation;

        return rotatedVector;
    }
}
