package org.example.iec61850.datatypes.logicalNodes.breakers;

import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.protection.ACT;
import org.example.iec61850.datatypes.protection.SPS;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для управления всеми состояниями переключений
 */
public class CSWI extends LN {
    private SPS LocKey = new SPS();
    private SPS Loc = new SPS();
    public ACT OpOpn = new ACT();
    public ACT OpCls = new ACT();
    private INC OpCntRs = new INC();
    private SPC LocSta = new SPC();
    public DPC Pos = new DPC();
    public DPC PosA = new DPC();
    public DPC PosB = new DPC();
    public DPC PosC = new DPC();

    /* Входы */
    private SPS SelOpnA = new SPS();
    private SPS SelOpnB = new SPS();
    private SPS SelOpnC = new SPS();

    private SPS SelClsA = new SPS();
    private SPS SelClsB = new SPS();
    private SPS SelClsC = new SPS();

    /* Блокировки (общие для всех фаз) */
    private SPS BlkOpn = new SPS(); // Блокировка отключения
    private SPS BlkCls = new SPS(); // Блокировка включения

    public List<ACT> protSignalsList = new ArrayList<>(); // Сигналы защиты
    public List<ACT> automaticSignalsList = new ArrayList<>(); // Сигналы автоматики

    @Override
    public void process() {

        // Защита от null в начале
        if (SelOpnA.getStVal().getValue() == null) SelOpnA.getStVal().setValue(false);
        if (SelOpnB.getStVal().getValue() == null) SelOpnB.getStVal().setValue(false);
        if (SelOpnC.getStVal().getValue() == null) SelOpnC.getStVal().setValue(false);
        if (SelClsA.getStVal().getValue() == null) SelClsA.getStVal().setValue(false);
        if (SelClsB.getStVal().getValue() == null) SelClsB.getStVal().setValue(false);
        if (SelClsC.getStVal().getValue() == null) SelClsC.getStVal().setValue(false);
        if (BlkOpn.getStVal().getValue() == null) BlkOpn.getStVal().setValue(false);
        if (BlkCls.getStVal().getValue() == null) BlkCls.getStVal().setValue(false);

        // Сбрасываем все селекторы
        SelOpnA.getStVal().setValue(false);
        SelOpnB.getStVal().setValue(false);
        SelOpnC.getStVal().setValue(false);
        SelClsA.getStVal().setValue(false);
        SelClsB.getStVal().setValue(false);
        SelClsC.getStVal().setValue(false);

        // Обрабатываем сигналы защиты (отключение)
        for (ACT act : protSignalsList) {
            if (act.getPhsA().getValue() != null && act.getPhsA().getValue()) {
                SelOpnA.getStVal().setValue(true);
            }
            if (act.getPhsB().getValue() != null && act.getPhsB().getValue()) {
                SelOpnB.getStVal().setValue(true);
            }
            if (act.getPhsC().getValue() != null && act.getPhsC().getValue()) {
                SelOpnC.getStVal().setValue(true);
            }
        }

        // Обрабатываем сигналы автоматики (включение)
        for (ACT act : automaticSignalsList) {
            if (act.getPhsA().getValue() != null && act.getPhsA().getValue()) {
                SelClsA.getStVal().setValue(true);
            }
            if (act.getPhsB().getValue() != null && act.getPhsB().getValue()) {
                SelClsB.getStVal().setValue(true);
            }
            if (act.getPhsC().getValue() != null && act.getPhsC().getValue()) {
                SelClsC.getStVal().setValue(true);
            }
        }

        // Управление положением фаз с учетом блокировок
        // Фаза A
        if (SelOpnA.getStVal().getValue() && !BlkOpn.getStVal().getValue()) {
            PosA.getStVal().setValue(DPC.Values.OFF);
        } else if (SelClsA.getStVal().getValue() && !BlkCls.getStVal().getValue()) {
            PosA.getStVal().setValue(DPC.Values.ON);
        }

        // Фаза B
        if (SelOpnB.getStVal().getValue() && !BlkOpn.getStVal().getValue()) {
            PosB.getStVal().setValue(DPC.Values.OFF);
        } else if (SelClsB.getStVal().getValue() && !BlkCls.getStVal().getValue()) {
            PosB.getStVal().setValue(DPC.Values.ON);
        }

        // Фаза C
        if (SelOpnC.getStVal().getValue() && !BlkOpn.getStVal().getValue()) {
            PosC.getStVal().setValue(DPC.Values.OFF);
        } else if (SelClsC.getStVal().getValue() && !BlkCls.getStVal().getValue()) {
            PosC.getStVal().setValue(DPC.Values.ON);
        }

        // Общее положение выключателя
//        updateGeneralPosition();
    }

    private void updateGeneralPosition() {
        // Защита от null
        if (PosA.getStVal().getValue() == null) PosA.getStVal().setValue(DPC.Values.ON);
        if (PosB.getStVal().getValue() == null) PosB.getStVal().setValue(DPC.Values.ON);
        if (PosC.getStVal().getValue() == null) PosC.getStVal().setValue(DPC.Values.ON);

        // Общее положение = ON только если все фазы включены
        boolean allPhasesOn = PosA.getStVal().getValue().equals(DPC.Values.ON) &&
                PosB.getStVal().getValue().equals(DPC.Values.ON) &&
                PosC.getStVal().getValue().equals(DPC.Values.ON);

        // Общее положение = OFF если хотя бы одна фаза отключена
        boolean anyPhaseOff = PosA.getStVal().getValue().equals(DPC.Values.OFF) ||
                PosB.getStVal().getValue().equals(DPC.Values.OFF) ||
                PosC.getStVal().getValue().equals(DPC.Values.OFF);

        if (anyPhaseOff) {
            Pos.getStVal().setValue(DPC.Values.OFF);
        } else if (allPhasesOn) {
            Pos.getStVal().setValue(DPC.Values.ON);
        }
    }

    // Методы для установки блокировок
    public void setBlkOpn(boolean value) {
        BlkOpn.getStVal().setValue(value);
    }

    public void setBlkCls(boolean value) {
        BlkCls.getStVal().setValue(value);
    }
}