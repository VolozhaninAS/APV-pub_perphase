package org.example;

import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.logicalNodes.automatic.RREC;
import org.example.iec61850.datatypes.logicalNodes.automatic.RSYN;
import org.example.iec61850.datatypes.logicalNodes.breakers.CSWI;
import org.example.iec61850.datatypes.logicalNodes.breakers.XCBR;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.logicalNodes.hmi.NHMI;
import org.example.iec61850.datatypes.logicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.datatypes.logicalNodes.measurements.MMXU;
import org.example.iec61850.datatypes.logicalNodes.measurements.MSQI;
import org.example.iec61850.datatypes.logicalNodes.protection.PTOC;
import org.example.iec61850.datatypes.logicalNodes.protection.RDIR;
import org.example.iec61850.datatypes.logicalNodes.protocol.LGOS;
import org.example.iec61850.datatypes.logicalNodes.protocol.LSVS;
import org.example.iec61850.datatypes.logicalNodes.protocol.MyPacketListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainX {
    private static final List<LN> logicalNodes = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        LSVS lsvs = new LSVS();
        lsvs.setDatasetSize(64);
        lsvs.setNicName("Qualcomm Atheros QCA9377 Wireless Network Adapter");
		MMXU mmxu = new MMXU();
		logicalNodes.add(mmxu);
		mmxu.IaInst = lsvs.phsAIInst;
		mmxu.IbInst = lsvs.phsBIInst;
		mmxu.IcInst = lsvs.phsCIInst;
		mmxu.UaInst = lsvs.phsAUInst;
		mmxu.UbInst = lsvs.phsBUInst;
		mmxu.UcInst = lsvs.phsCUInst;

		MSQI msqi = new MSQI();
		msqi.A = mmxu.A;
		msqi.PNV = mmxu.PNV;
		logicalNodes.add(msqi);

		RDIR rdir = new RDIR(0, 80, -80, 35, 5000);
		logicalNodes.add(rdir);
		rdir.A = mmxu.A;
		rdir.PNV = mmxu.PNV;
		rdir.lineVoltages = msqi.getImbPPV();

		PTOC ptoc1 = new PTOC();
		logicalNodes.add(ptoc1);
		ptoc1.A = mmxu.A;
		ptoc1.currentSeq = msqi.getSeqA();
		ptoc1.direction = rdir.getDir();
		ptoc1.StrVal.getSetMag().getF().setValue(600.0);
		ptoc1.OpDITmms.getSetVal().setValue(10);
		ptoc1.isDir = false;

		RREC rrec1 = new RREC();
		rrec1.Rec1Tmms.getSetVal().setValue(200);
		rrec1.Rec2Tmms.getSetVal().setValue(250);
		rrec1.Rec3Tmms.getSetVal().setValue(300);
		rrec1.PlsTmms.getSetVal().setValue(10);
		rrec1.RclTmms.getSetVal().setValue(10_000);

		rrec1.OpOpn.setPhsA(ptoc1.Op.getPhsA());
		rrec1.OpOpn.setPhsB(ptoc1.Op.getPhsB());
		rrec1.OpOpn.setPhsC(ptoc1.Op.getPhsC());

		RSYN rsyn1 = new RSYN();
		logicalNodes.add(rsyn1);
		rsyn1.DifV.getSetMag().getF().setValue(1000.0);
		rsyn1.DifHz.getSetMag().getF().setValue(0.5);
		rsyn1.DifAng.getSetMag().getF().setValue(10.0);
		rsyn1.LivDeaMod.getSetVal().setValue(0);
		rsyn1.DeaLinVal.getSetMag().getF().setValue(1000.0);
		rsyn1.LivLinVal.getSetMag().getF().setValue(10_000.0);
		rsyn1.DeaBusVal.getSetMag().getF().setValue(1000.0);
		rsyn1.LivBusVal.getSetMag().getF().setValue(10_000.0);
		rsyn1.PlsTmms.getSetVal().setValue(10);
		rsyn1.BkrTmms.getSetVal().setValue(50);
		rsyn1.LinePhV = mmxu.PNV;
		rsyn1.BusPhV = mmxu.PNV;
		rsyn1.LineHz.getCVal().getMag().getF().setValue(50.0);
		rsyn1.BusHz.getCVal().getMag().getF().setValue(50.0);
		rsyn1.SynPrg = rrec1.SynPrg;
		rrec1.Rel = rsyn1.Rel;

		CSWI cswi1 = new CSWI();
		logicalNodes.add(cswi1);
		cswi1.protSignalsList.add(ptoc1.Op);
		cswi1.automaticSignalsList.add(rrec1.Op);
		cswi1.Pos.getStVal().setValue(DPC.Values.ON);

		XCBR xcbr1 = new XCBR();
		logicalNodes.add(xcbr1);

		xcbr1.PosA = cswi1.PosA;
		xcbr1.PosB = cswi1.PosB;
		xcbr1.PosC = cswi1.PosC;

		lsvs.PosA = xcbr1.PosA;
		lsvs.PosB = xcbr1.PosB;
		lsvs.PosC = xcbr1.PosC;

		rrec1.Blk = xcbr1.BlkCls;

		rrec1.PosA = xcbr1.PosA;
		rrec1.PosB = xcbr1.PosB;
		rrec1.PosC = xcbr1.PosC;

		LGOS lgos1 = new LGOS();
		rrec1.BlkRec = lgos1.BlkRec;
		logicalNodes.add(lgos1);
		logicalNodes.add(rrec1);

		NHMI nhmiCurrents = new NHMI();
		logicalNodes.add(nhmiCurrents);
		nhmiCurrents.addSignals(new NHMISignal("ia", lsvs.phsAIInst.getInstMag().getF()));
		nhmiCurrents.addSignals(new NHMISignal("ib", lsvs.phsBIInst.getInstMag().getF()));
		nhmiCurrents.addSignals(new NHMISignal("ic", lsvs.phsCIInst.getInstMag().getF()));

		NHMI nhmiAnalogCurrent = new NHMI();
		logicalNodes.add(nhmiAnalogCurrent);
		nhmiAnalogCurrent.addSignals(new NHMISignal("rmsA", mmxu.A.getPhsA().getCVal().getMag().getF()));
		nhmiAnalogCurrent.addSignals(new NHMISignal("rmsB", mmxu.A.getPhsB().getCVal().getMag().getF()));
		nhmiAnalogCurrent.addSignals(new NHMISignal("rmsC", mmxu.A.getPhsC().getCVal().getMag().getF()));

		NHMI nhmiProtectionWorkingA = new NHMI();
		logicalNodes.add(nhmiProtectionWorkingA);
		nhmiProtectionWorkingA.addSignals(new NHMISignal("StrPtoc1_A", ptoc1.Str.getPhsA()));
		nhmiProtectionWorkingA.addSignals(new NHMISignal("OpPtoc1_A", ptoc1.Op.getPhsA()));
		nhmiProtectionWorkingA.addSignals(new NHMISignal("OpRrec1_A", rrec1.Op.getPhsA()));
		nhmiProtectionWorkingA.addSignals(new NHMISignal("PosXcbr_A", xcbr1.isOpenA));

		NHMI nhmiProtectionWorkingB = new NHMI();
		logicalNodes.add(nhmiProtectionWorkingB);
		nhmiProtectionWorkingB.addSignals(new NHMISignal("StrPtoc1_B", ptoc1.Str.getPhsB()));
		nhmiProtectionWorkingB.addSignals(new NHMISignal("OpPtoc1_B", ptoc1.Op.getPhsB()));
		nhmiProtectionWorkingB.addSignals(new NHMISignal("OpRrec1_B", rrec1.Op.getPhsB()));
		nhmiProtectionWorkingB.addSignals(new NHMISignal("PosXcbr_B", xcbr1.isOpenB));

		NHMI nhmiProtectionWorkingC = new NHMI();
		logicalNodes.add(nhmiProtectionWorkingC);
		nhmiProtectionWorkingC.addSignals(new NHMISignal("StrPtoc1_C", ptoc1.Str.getPhsC()));
		nhmiProtectionWorkingC.addSignals(new NHMISignal("OpPtoc1_C", ptoc1.Op.getPhsC()));
		nhmiProtectionWorkingC.addSignals(new NHMISignal("OpRrec1_C", rrec1.Op.getPhsC()));
		nhmiProtectionWorkingC.addSignals(new NHMISignal("PosXcbr_C", xcbr1.isOpenC));

        lsvs.addListener(new MyPacketListener() {
            @Override
            public void listen() {
                logicalNodes.forEach(LN::process);
            }
        });
        lsvs.process();
    }
}
