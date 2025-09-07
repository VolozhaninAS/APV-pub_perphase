package org.example.iec61850.datatypes.logicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.CMV;
import org.example.iec61850.datatypes.measurements.DEL;
import org.example.iec61850.datatypes.measurements.WYE;
import org.example.iec61850.datatypes.protection.ACD;
import org.example.iec61850.datatypes.settings.ASG;

/**
 * Класс, описывающий орган направления мощности
 */
@Getter @Setter
public class RDIR extends LN {
    private ACD Dir = new ACD();
    private ASG ChrAng = new ASG();
    private ASG MinFwdAng = new ASG();
    private ASG MinRvAng = new ASG();
    private ASG MaxFwdAng = new ASG();
    private ASG MaxRvAng = new ASG();
    private ASG BlkValA = new ASG();
    private ASG BlkValV = new ASG();
    private ASG MinPPV = new ASG();

    public DEL lineVoltages = new DEL();
    public WYE A = new WYE();
    public WYE PNV = new WYE();
    private DEL ImbPPV = new DEL();

    public RDIR (double ChrAng, double MaxFwdAng, double MinFwdAng, double minI, double MinPPV ){
        this.ChrAng.getSetMag().getF().setValue(ChrAng);
        this.MaxFwdAng.getSetMag().getF().setValue(MaxFwdAng);
        this.MinFwdAng.getSetMag().getF().setValue(MinFwdAng);
        this.BlkValA.getSetMag().getF().setValue(minI);
        this.MinPPV.getSetMag().getF().setValue(MinPPV);
    }

    @Override
    public void process() {
        Dir.getPhsA().setValue(
                checkAngle(lineVoltages.getPhsBC(), A.getPhsA())
        );
        Dir.getPhsB().setValue(
                checkAngle(lineVoltages.getPhsCA(), A.getPhsB())
        );
        Dir.getPhsC().setValue(
                checkAngle(lineVoltages.getPhsAB(), A.getPhsC())
        );
        Dir.getGeneral().setValue(
                Dir.getPhsA().getValue() || Dir.getPhsB().getValue() ||
                        Dir.getPhsC().getValue()
        );
    }
    private boolean checkAngle(CMV v, CMV i){
        if (i.getCVal().getMag().getF().getValue() <
                BlkValA.getSetMag().getF().getValue())
            return false;
        if (v.getCVal().getMag().getF().getValue() <
                MinPPV.getSetMag().getF().getValue())
            return false;
        double angle = v.getCVal().getAng().getF().getValue() -
                i.getCVal().getAng().getF().getValue();
        return angle >= MinFwdAng.getSetMag().getF().getValue() && angle
                <= MaxFwdAng.getSetMag().getF().getValue();
    }
}
