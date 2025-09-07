package org.example.iec61850.datatypes.logicalNodes.measurements;

import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.DEL;
import org.example.iec61850.datatypes.measurements.SAV;
import org.example.iec61850.datatypes.measurements.WYE;
import org.example.iec61850.datatypes.utils.Filter;
import org.example.iec61850.datatypes.utils.Fourier;

/**
 * Класс для описания узла измерений и передачи данных в фильтр
 */
public class MMXU extends LN {

    public static int busSize = 80;

    private SAV TotW = new SAV();
    private SAV TotVAr = new SAV();
    private SAV TotVA = new SAV();
    private SAV TotPF = new SAV();
    private SAV Hz = new SAV();
    private DEL PPV = new DEL();

    private WYE PhV = new WYE();
    private WYE W = new WYE();
    private WYE VAr = new WYE();
    private WYE VA = new WYE();
    private WYE PF = new WYE();
    private WYE Z = new WYE();


    /* Входы */
    public SAV IaInst = new SAV();
    public SAV IbInst = new SAV();
    public SAV IcInst = new SAV();
    public SAV UaInst = new SAV();
    public SAV UbInst = new SAV();
    public SAV UcInst = new SAV();


    /* Выходы */
    public WYE A = new WYE(); // Фазные токи (IL1, IL2, IL3)
    public WYE PNV = new WYE();


    /* Переменные */
    private final Filter ia = new Fourier(busSize);
    private final Filter ib = new Fourier(busSize);
    private final Filter ic = new Fourier(busSize);

    private final Filter ua = new Fourier(busSize);
    private final Filter ub = new Fourier(busSize);
    private final Filter uc = new Fourier(busSize);


    @Override
    public void process() {
        this.ia.process(this.IaInst, A.getPhsA().getCVal());
        this.ib.process(this.IbInst, A.getPhsB().getCVal());
        this.ic.process(this.IcInst, A.getPhsC().getCVal());

        this.ua.process(this.UaInst, PNV.getPhsA().getCVal());
        this.ub.process(this.UbInst, PNV.getPhsB().getCVal());
        this.uc.process(this.UcInst, PNV.getPhsC().getCVal());
    }
}

