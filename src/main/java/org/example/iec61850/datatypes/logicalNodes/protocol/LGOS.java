package org.example.iec61850.datatypes.logicalNodes.protocol;

import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.logicalNodes.common.LN;

public class LGOS extends LN {

	public SPC BlkRec = new SPC(); // Запрет АПВ

	@Override
	public void process() {
		BlkRec.getStVal().setValue(false);
	}
}