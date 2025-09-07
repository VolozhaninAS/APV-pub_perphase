package org.example.iec61850.datatypes.logicalNodes.automatic;

import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.protection.ACT;
import org.example.iec61850.datatypes.protection.INS;
import org.example.iec61850.datatypes.protection.SPS;
import org.example.iec61850.datatypes.settings.ING;

/**
 * Класс, описывающий АПВ
 */
public class RREC extends LN {

    public static int dt = 1;

    private INC OpCntRs = new INC();
    private SPC ChkRec = new SPC();
    private SPS Auto = new SPS();
    private INS AutoRecSt = new INS();

    public ACT OpOpn = new ACT();
    public DPC PosA = new DPC();
    public DPC PosB = new DPC();
    public DPC PosC = new DPC();
    public SPS Rel = new SPS();
    public SPS Blk = new SPS();
    public SPC BlkRec = new SPC();

    public ACT Op = new ACT();
    public SPS SynPrg = new SPS();

    public ING Rec1Tmms = new ING();
    public ING Rec2Tmms = new ING();
    public ING Rec3Tmms = new ING();
    public ING PlsTmms = new ING();
    public ING RclTmms = new ING();

    private int recCntTimeA = 0;
    private int currentCycleNumA = 0;
    private boolean prevValueOfOpOpnA = false;
    private int impulseStartTimeA = 0;
    private boolean isImpulseActiveA = false;
    private int readyTimeA = 0;
    private boolean inReadyStateA = false;

    private int recCntTimeB = 0;
    private int currentCycleNumB = 0;
    private boolean prevValueOfOpOpnB = false;
    private int impulseStartTimeB = 0;
    private boolean isImpulseActiveB = false;
    private int readyTimeB = 0;
    private boolean inReadyStateB = false;

    private int recCntTimeC = 0;
    private int currentCycleNumC = 0;
    private boolean prevValueOfOpOpnC = false;
    private int impulseStartTimeC = 0;
    private boolean isImpulseActiveC = false;
    private int readyTimeC = 0;
    private boolean inReadyStateC = false;

    @Override
    public void process() {
        if (SynPrg.getStVal().getValue() == null) SynPrg.getStVal().setValue(false);
        if (Rel.getStVal().getValue() == null) Rel.getStVal().setValue(false);
        if (Blk.getStVal().getValue() == null) Blk.getStVal().setValue(false);
        if (BlkRec.getStVal().getValue() == null) BlkRec.getStVal().setValue(false);
        if (OpOpn.getPhsA().getValue() == null) OpOpn.getPhsA().setValue(false);
        if (OpOpn.getPhsB().getValue() == null) OpOpn.getPhsB().setValue(false);
        if (OpOpn.getPhsC().getValue() == null) OpOpn.getPhsC().setValue(false);
        if (PosA.getStVal().getValue() == null) PosA.getStVal().setValue(DPC.Values.ON);
        if (PosB.getStVal().getValue() == null) PosB.getStVal().setValue(DPC.Values.ON);
        if (PosC.getStVal().getValue() == null) PosC.getStVal().setValue(DPC.Values.ON);

        boolean relAllowed = Rel.getStVal().getValue();
        boolean blk = Blk.getStVal().getValue() || BlkRec.getStVal().getValue();

        if (inReadyStateA) {
            readyTimeA += dt;
            if (readyTimeA >= RclTmms.getSetVal().getValue()) {
                inReadyStateA = false;
                readyTimeA = 0;
                currentCycleNumA = 0;
                isImpulseActiveA = false;
                recCntTimeA = 0;
            }
        } else if (currentCycleNumA > 3) {
            inReadyStateA = true;
            SynPrg.getStVal().setValue(false);
        }

        if (inReadyStateB) {
            readyTimeB += dt;
            if (readyTimeB >= RclTmms.getSetVal().getValue()) {
                inReadyStateB = false;
                readyTimeB = 0;
                currentCycleNumB = 0;
                isImpulseActiveB = false;
                recCntTimeB = 0;
            }
        } else if (currentCycleNumB > 3) {
            inReadyStateB = true;
            SynPrg.getStVal().setValue(false);
        }

        if (inReadyStateC) {
            readyTimeC += dt;
            if (readyTimeC >= RclTmms.getSetVal().getValue()) {
                inReadyStateC = false;
                readyTimeC = 0;
                currentCycleNumC = 0;
                isImpulseActiveC = false;
                recCntTimeC = 0;
            }
        } else if (currentCycleNumC > 3) {
            inReadyStateC = true;
            SynPrg.getStVal().setValue(false);
        }

        if (!inReadyStateA && currentCycleNumA <= 3) {
            boolean opOpnA = OpOpn.getPhsA().getValue() && !prevValueOfOpOpnA;
            boolean breakerOffA = PosA.getStVal().getValue().equals(DPC.Values.OFF);
            prevValueOfOpOpnA = OpOpn.getPhsA().getValue();
            if (opOpnA && breakerOffA && !blk) {
                currentCycleNumA++;
                SynPrg.getStVal().setValue(true);
                isImpulseActiveA = false;
                recCntTimeA = 0;
            }
            boolean strA = false;
            if (currentCycleNumA >= 1 && currentCycleNumA <= 3) {
                recCntTimeA += dt;
                int requiredTime = switch (currentCycleNumA) {
                    case 1 -> Rec1Tmms.getSetVal().getValue();
                    case 2 -> Rec2Tmms.getSetVal().getValue();
                    case 3 -> Rec3Tmms.getSetVal().getValue();
                    default -> 0;
                };

                if (breakerOffA && relAllowed && recCntTimeA >= requiredTime && !isImpulseActiveA) {
                    isImpulseActiveA = true;
                    impulseStartTimeA = recCntTimeA;
                }

                if (isImpulseActiveA && (recCntTimeA - impulseStartTimeA) <= PlsTmms.getSetVal().getValue()) {
                    strA = true;
                } else if (isImpulseActiveA && (recCntTimeA - impulseStartTimeA) > PlsTmms.getSetVal().getValue()) {
                    isImpulseActiveA = false;
                    SynPrg.getStVal().setValue(false);
                    recCntTimeA = 0;
                }
            }
            Op.getPhsA().setValue(strA);
        } else {
            Op.getPhsA().setValue(false);
        }

        if (!inReadyStateB && currentCycleNumB <= 3) {
            boolean opOpnB = OpOpn.getPhsB().getValue() && !prevValueOfOpOpnB;
            boolean breakerOffB = PosB.getStVal().getValue().equals(DPC.Values.OFF);
            prevValueOfOpOpnB = OpOpn.getPhsB().getValue();
            if (opOpnB && breakerOffB && !blk) {
                currentCycleNumB++;
                SynPrg.getStVal().setValue(true);
                isImpulseActiveB = false;
                recCntTimeB = 0;
            }

            boolean strB = false;
            if (currentCycleNumB >= 1 && currentCycleNumB <= 3) {
                recCntTimeB += dt;
                int requiredTime = switch (currentCycleNumB) {
                    case 1 -> Rec1Tmms.getSetVal().getValue();
                    case 2 -> Rec2Tmms.getSetVal().getValue();
                    case 3 -> Rec3Tmms.getSetVal().getValue();
                    default -> 0;
                };
                if (breakerOffB && relAllowed && recCntTimeB >= requiredTime && !isImpulseActiveB) {
                    isImpulseActiveB = true;
                    impulseStartTimeB = recCntTimeB;
                }
                if (isImpulseActiveB && (recCntTimeB - impulseStartTimeB) <= PlsTmms.getSetVal().getValue()) {
                    strB = true;
                } else if (isImpulseActiveB && (recCntTimeB - impulseStartTimeB) > PlsTmms.getSetVal().getValue()) {
                    isImpulseActiveB = false;
                    SynPrg.getStVal().setValue(false);
                    recCntTimeB = 0;
                }
            }
            Op.getPhsB().setValue(strB);
        } else {
            Op.getPhsB().setValue(false);
        }

        if (!inReadyStateC && currentCycleNumC <= 3) {
            boolean opOpnC = OpOpn.getPhsC().getValue() && !prevValueOfOpOpnC;
            boolean breakerOffC = PosC.getStVal().getValue().equals(DPC.Values.OFF);
            prevValueOfOpOpnC = OpOpn.getPhsC().getValue();
            if (opOpnC && breakerOffC && !blk) {
                currentCycleNumC++;
                SynPrg.getStVal().setValue(true);
                isImpulseActiveC = false;
                recCntTimeC = 0;
            }
            boolean strC = false;
            if (currentCycleNumC >= 1 && currentCycleNumC <= 3) {
                recCntTimeC += dt;
                int requiredTime = switch (currentCycleNumC) {
                    case 1 -> Rec1Tmms.getSetVal().getValue();
                    case 2 -> Rec2Tmms.getSetVal().getValue();
                    case 3 -> Rec3Tmms.getSetVal().getValue();
                    default -> 0;
                };
                if (breakerOffC && relAllowed && recCntTimeC >= requiredTime && !isImpulseActiveC) {
                    isImpulseActiveC = true;
                    impulseStartTimeC = recCntTimeC;
                }
                if (isImpulseActiveC && (recCntTimeC - impulseStartTimeC) <= PlsTmms.getSetVal().getValue()) {
                    strC = true;
                } else if (isImpulseActiveC && (recCntTimeC - impulseStartTimeC) > PlsTmms.getSetVal().getValue()) {
                    isImpulseActiveC = false;
                    SynPrg.getStVal().setValue(false);
                    recCntTimeC = 0;
                }
            }
            Op.getPhsC().setValue(strC);
        } else {
            Op.getPhsC().setValue(false);
        }

        Op.getGeneral().setValue(Op.getPhsA().getValue() || Op.getPhsB().getValue() || Op.getPhsC().getValue());

        if (inReadyStateA || inReadyStateB || inReadyStateC) {
            AutoRecSt.getStVal().setValue(2);
        } else if (currentCycleNumA > 0 || currentCycleNumB > 0 || currentCycleNumC > 0) {
            AutoRecSt.getStVal().setValue(1);
        } else {
            AutoRecSt.getStVal().setValue(0);
        }
    }
}