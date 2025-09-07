package org.example.iec61850.datatypes.logicalNodes.automatic;

import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.CMV;
import org.example.iec61850.datatypes.measurements.WYE;
import org.example.iec61850.datatypes.protection.SPS;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ING;

/**
 * Класс, описывающий контроль синхронизма
 */
public class RSYN extends LN {

    private SPC RHz = new SPC();
    private SPC LHz = new SPC();
    private SPC RV = new SPC();
    private SPC LV = new SPC();
    private SPS VInd = new SPS();
    private SPS AngInd = new SPS();
    private SPS HzInd = new SPS();
    private CMV DifVClc = new CMV();
    private CMV DifHzClc = new CMV();
    private CMV DifAngClc = new CMV();

    public WYE LinePhV = new WYE();
    public WYE BusPhV = new WYE();
    public CMV LineHz = new CMV();
    public CMV BusHz = new CMV();
    public SPS SynPrg = new SPS();
    public SPS Rel = new SPS();
    public ASG DifV = new ASG();
    public ASG DifHz = new ASG();
    public ASG DifAng = new ASG();
    public ING LivDeaMod = new ING();
    public ASG DeaLinVal = new ASG();
    public ASG LivLinVal = new ASG();
    public ASG DeaBusVal = new ASG();
    public ASG LivBusVal = new ASG();
    public ING PlsTmms = new ING();
    public ING BkrTmms = new ING();

    @Override
    public void process() {
        Rel.getStVal().setValue(false);

        if (SynPrg.getStVal().getValue() != null && SynPrg.getStVal().getValue()) {
            if (LivDeaMod.getSetVal().getValue() != null && LivDeaMod.getSetVal().getValue() == 0) {
                Rel.getStVal().setValue(true);
                return;
            }
            if (isValidData()) {
                boolean ONL = LinePhV.getPhsA().getCVal().getMag().getF().getValue() < DeaLinVal.getSetMag().getF().getValue();
                boolean NNL = LinePhV.getPhsA().getCVal().getMag().getF().getValue() > LivLinVal.getSetMag().getF().getValue();
                boolean ONSH = BusPhV.getPhsA().getCVal().getMag().getF().getValue() < DeaBusVal.getSetMag().getF().getValue();
                boolean NNSH = BusPhV.getPhsA().getCVal().getMag().getF().getValue() > LivBusVal.getSetMag().getF().getValue();
                boolean voltageConditionsMet = false;
                switch (LivDeaMod.getSetVal().getValue()) {
                    case 1:
                        voltageConditionsMet = ONL && NNSH;
                        break;
                    case 2:
                        voltageConditionsMet = NNL && ONSH;
                        break;
                    case 3:
                        voltageConditionsMet = (ONL && NNSH) || (NNL && ONSH);
                        break;
                    default:
                        voltageConditionsMet = false;
                        break;
                }

                if (voltageConditionsMet) {
                    DifVClc = calcDifMag(LinePhV, BusPhV);
                    DifAngClc = calcDifAng(LinePhV, BusPhV);
                    DifHzClc = calcDifHz(LineHz, BusHz);

                    VInd.getStVal().setValue(isLargerThenSetting(DifVClc, DifV));
                    AngInd.getStVal().setValue(isLargerThenSetting(DifAngClc, DifAng));
                    HzInd.getStVal().setValue(isLargerThenSetting(DifHzClc, DifHz));

                    if (!VInd.getStVal().getValue() && !AngInd.getStVal().getValue() && !HzInd.getStVal().getValue()) {
                        Rel.getStVal().setValue(true);
                    }
                }
            }
        }
    }

    private boolean isValidData() {
        return LinePhV.getPhsA().getCVal().getMag().getF().getValue() != null &&
                BusPhV.getPhsA().getCVal().getMag().getF().getValue() != null &&
                LineHz.getCVal().getMag().getF().getValue() != null &&
                BusHz.getCVal().getMag().getF().getValue() != null &&
                DeaLinVal.getSetMag().getF().getValue() != null &&
                LivLinVal.getSetMag().getF().getValue() != null &&
                DeaBusVal.getSetMag().getF().getValue() != null &&
                LivBusVal.getSetMag().getF().getValue() != null &&
                LivDeaMod.getSetVal().getValue() != null;
    }

    private CMV calcDifMag(WYE lineV, WYE busV) {
        CMV difMag = new CMV();
        double dif = Math.abs(lineV.getPhsA().getCVal().getMag().getF().getValue() - busV.getPhsA().getCVal().getMag().getF().getValue());
        difMag.getCVal().getMag().getF().setValue(dif);
        return difMag;
    }

    private CMV calcDifAng(WYE lineV, WYE busV) {
        CMV difAng = new CMV();
        double dif = Math.abs(lineV.getPhsA().getCVal().getAng().getF().getValue() - busV.getPhsA().getCVal().getAng().getF().getValue());
        difAng.getCVal().getMag().getF().setValue(dif);
        return difAng;
    }

    private CMV calcDifHz(CMV lineF, CMV busF) {
        CMV difHz = new CMV();
        double dif = Math.abs(lineF.getCVal().getMag().getF().getValue() - busF.getCVal().getMag().getF().getValue());
        difHz.getCVal().getMag().getF().setValue(dif);
        return difHz;
    }

    private boolean isLargerThenSetting(CMV value, ASG settingValue) {
        return value.getCVal().getMag().getF().getValue() > settingValue.getSetMag().getF().getValue();
    }
}