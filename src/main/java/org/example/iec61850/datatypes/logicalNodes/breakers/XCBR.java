package org.example.iec61850.datatypes.logicalNodes.breakers;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.description.DPL;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.protection.BCR;
import org.example.iec61850.datatypes.protection.ENS;
import org.example.iec61850.datatypes.protection.INS;
import org.example.iec61850.datatypes.protection.SPS;
import org.example.iec61850.datatypes.settings.ING;

/**
 * Класс для реализации состояний силового выключателя
 */
public class XCBR extends LN {
    private DPL EEName = new DPL();
    private ENS EEHealth = new ENS();
    private SPS LocKey = new SPS();
    private SPS Loc = new SPS();
    private INS OpCnt = new INS();
    private ENS CBOpCap = new ENS();
    private ENS POWCap = new ENS();
    private INS MaxOpCap = new INS();
    private SPS Dsc = new SPS();
    private BCR SumSwARs = new BCR();
    private SPC LocSta = new SPC();

    // Пофазные положения
    public DPC Pos = new DPC();  // Общее положение
    public DPC PosA = new DPC(); // Положение фазы A
    public DPC PosB = new DPC(); // Положение фазы B
    public DPC PosC = new DPC(); // Положение фазы C

    // Блокировки (общие для всех фаз)
    public SPC BlkOpn = new SPC(); // Блокировка отключения
    public SPS BlkCls = new SPS(); // Блокировка включения

    private SPC ChaMotEna = new SPC();
    private ING CBTmms = new ING();

    // Пофазные статусы
    public Attribute<Boolean> isOpenA = new Attribute<>();
    public Attribute<Boolean> isOpenB = new Attribute<>();
    public Attribute<Boolean> isOpenC = new Attribute<>();
    public Attribute<Boolean> isOpen = new Attribute<>(); // Общий статус

    @Override
    public void process() {
        BlkCls.getStVal().setValue(false);

        // Обновляем статусы фаз
        updatePhaseStatus('A', PosA, isOpenA);
        updatePhaseStatus('B', PosB, isOpenB);
        updatePhaseStatus('C', PosC, isOpenC);

//        System.out.println(PosA.getStVal().getValue());

        // Обновляем общий статус
//        updateGeneralStatus();
    }

    private void updatePhaseStatus(char phase, DPC pos, Attribute<Boolean> isOpenAttr) {
        if (pos.getStVal().getValue() != null && pos.getStVal().getValue().equals(DPC.Values.ON)) {
            isOpenAttr.setValue(false); // Выключатель включен
        } else {
            isOpenAttr.setValue(true);  // Выключатель отключен
        }
    }

    private void updateGeneralStatus() {
        // Общий статус = отключен, если хотя бы одна фаза отключена
        boolean anyPhaseOpen = isOpenA.getValue() || isOpenB.getValue() || isOpenC.getValue();

        // Общий статус = включен, если все фазы включены
        boolean allPhasesClosed = !isOpenA.getValue() && !isOpenB.getValue() && !isOpenC.getValue();

        if (anyPhaseOpen) {
            isOpen.setValue(true);
            Pos.getStVal().setValue(DPC.Values.OFF);
        } else if (allPhasesClosed) {
            isOpen.setValue(false);
            Pos.getStVal().setValue(DPC.Values.ON);
        }
    }

    // Методы для управления положением фаз
    public void setPhaseA(boolean closed) {
        PosA.getStVal().setValue(closed ? DPC.Values.ON : DPC.Values.OFF);
        isOpenA.setValue(!closed);
    }

    public void setPhaseB(boolean closed) {
        PosB.getStVal().setValue(closed ? DPC.Values.ON : DPC.Values.OFF);
        isOpenB.setValue(!closed);
    }

    public void setPhaseC(boolean closed) {
        PosC.getStVal().setValue(closed ? DPC.Values.ON : DPC.Values.OFF);
        isOpenC.setValue(!closed);
    }

    // Методы для проверки состояний
    public boolean isPhaseAClosed() {
        return !isOpenA.getValue();
    }

    public boolean isPhaseBClosed() {
        return !isOpenB.getValue();
    }

    public boolean isPhaseCClosed() {
        return !isOpenC.getValue();
    }

    public boolean isAllPhasesClosed() {
        return isPhaseAClosed() && isPhaseBClosed() && isPhaseCClosed();
    }

    public boolean isAnyPhaseOpen() {
        return isOpenA.getValue() || isOpenB.getValue() || isOpenC.getValue();
    }
}