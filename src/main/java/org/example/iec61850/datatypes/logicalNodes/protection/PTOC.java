package org.example.iec61850.datatypes.logicalNodes.protection;

import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.description.CSD;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.SEQ;
import org.example.iec61850.datatypes.measurements.WYE;
import org.example.iec61850.datatypes.protection.ACD;
import org.example.iec61850.datatypes.protection.ACT;
import org.example.iec61850.datatypes.settings.*;

import java.time.Instant;

/**
 * Класс, описывающий токовую защиту
 */
public class PTOC extends LN {

    public static double dt = 1; // миллисек

    private INC OpCntRs = new INC();
    private CURVE TmACrv = new CURVE();
    private CSG TmAChr33 = new CSG();
    private CSD TmASt = new CSD();

    public ASG TmMult = new ASG();
    private ING MinOpTmms = new ING();
    private ING MaxOpTmms = new ING();
    private ENG TypRsCrv = new ENG();
    private ING RsDiTmms = new ING();
    private ENG DirMod = new ENG();

    /* Входы  */
    public ACD direction = new ACD();
    public SEQ currentSeq = new SEQ();
    public WYE A = new WYE();

    /* Выходы  */
    public ACD Str = new ACD();
    public ACT Op = new ACT();

    /* Уставки  */
    public ASG StrVal = new ASG();
    public ING OpDITmms = new ING();
    public boolean isDir = false;

    /* Переменные  */
    private int cntTimeA = 0;
    private int cntTimeB = 0;
    private int cntTimeC = 0;

    boolean prevOp = false;
    boolean currentOp = false;
    boolean prevIsKZ = false;

    @Override
    public void process() {
        boolean strA = A.getPhsA().getCVal().getMag().getF().getValue() > StrVal.getSetMag().getF().getValue();
        boolean strB = A.getPhsB().getCVal().getMag().getF().getValue() > StrVal.getSetMag().getF().getValue();
        boolean strC = A.getPhsC().getCVal().getMag().getF().getValue() > StrVal.getSetMag().getF().getValue();

        boolean str = strA || strB || strC;

        boolean dir = direction.getGeneral().getValue();

        if (isDir) {
            Str.getPhsA().setValue(strA && dir);
            Str.getPhsB().setValue(strB && dir);
            Str.getPhsC().setValue(strC && dir);
            Str.getGeneral().setValue(str && dir);

            cntTimeA = strA && dir ? cntTimeA + 1 : 0;
            cntTimeB = strB && dir ? cntTimeB + 1 : 0;
            cntTimeC = strC && dir ? cntTimeC + 1 : 0;
        } else {
            Str.getPhsA().setValue(strA);
            Str.getPhsB().setValue(strB);
            Str.getPhsC().setValue(strC);
            Str.getGeneral().setValue(str);

            cntTimeA = strA ? cntTimeA + 1 : 0;
            cntTimeB = strB ? cntTimeB + 1 : 0;
            cntTimeC = strC ? cntTimeC + 1 : 0;
        }

        Op.getPhsA().setValue(cntTimeA * dt > OpDITmms.getSetVal().getValue());
        Op.getPhsB().setValue(cntTimeB * dt > OpDITmms.getSetVal().getValue());
        Op.getPhsC().setValue(cntTimeC * dt > OpDITmms.getSetVal().getValue());

        Op.getGeneral().setValue(Op.getPhsA().getValue() || Op.getPhsB().getValue() || Op.getPhsC().getValue());

        // Метка времени начало КЗ (ф.A)
        if (strA && !prevIsKZ) {
            Instant now = Instant.now();
            System.out.println("КЗ в ф.A возникло в: " + now);
        }
        prevIsKZ = strA;

        // Метка времени срабатывания токовой защиты (ф.A)
        currentOp = Op.getPhsA().getValue();
        if (currentOp && !prevOp) {
            Instant now = Instant.now();
            System.out.println("Защита ф.A сработала в: " + now);
        }
        prevOp = currentOp;
    }
}