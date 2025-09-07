package org.example.iec61850.datatypes.logicalNodes.protocol;//package org.example.iec61850.datatypes.logicalNodes.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.logicalNodes.common.LN;
import org.example.iec61850.datatypes.measurements.SAV;
import org.pcap4j.core.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Setter
public class LSVS extends LN {
    static {
        try {for (PcapNetworkInterface nic : Pcaps.findAllDevs()) {log.info("found nic {}", nic);}}
        catch (PcapNativeException e) {throw new RuntimeException(e);}
    }

    public DPC Pos = new DPC();
    public DPC PosA = new DPC();
    public DPC PosB = new DPC();
    public DPC PosC = new DPC();

    public SAV phsAIInst = new SAV();
    public SAV phsBIInst = new SAV();
    public SAV phsCIInst = new SAV();
    public SAV phsAUInst = new SAV();
    public SAV phsBUInst = new SAV();
    public SAV phsCUInst = new SAV();

    @Setter
    private String nicName;
    private PcapHandle handle;

    @Setter
    private int datasetSize;
    private int selector = 0;
    private final List<MyPacketListener> packetListeners = new CopyOnWriteArrayList<>();

    private final PacketListener defaultPacketListener = packet -> {
        if(selector == 0) {
            decode(packet);
            packetListeners.forEach(MyPacketListener::listen);
        }
        selector ++;
        if (selector == 2) selector = 0;
    };


    @Override
    @SneakyThrows
    public void process() {
        if (handle == null){
            initializeNetworkInterface();

            if (handle != null) {
                String filter = "ether proto 0x88ba && ether dst 01:0C:CD:04:00:01";
                handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);

                Thread captureThread = new Thread(() -> {
                    try {
                        log.info("Starting packet capture");
                        handle.loop(0, defaultPacketListener);
                    } catch (PcapNativeException | InterruptedException | NotOpenException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("Packet capture finished");
                });
                captureThread.start();

            }
        }
    }

    @SneakyThrows
    private void initializeNetworkInterface() {
        Optional<PcapNetworkInterface> nic = Pcaps.findAllDevs().stream()
                .filter(i -> nicName.equals(i.getDescription()))
                .findFirst();
        if (nic.isPresent()) {
            handle = nic.get().openLive(1500, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            log.info("Network Handler created: {}", nic);
        } else {
            log.error("Network interface not found");
        }
    }

    public void addListener(MyPacketListener listener) {
        packetListeners.add(listener);
    }

    public void decode(PcapPacket packet){
        try {
            byte[] data = packet.getRawData();
            int strDataByte = data.length - datasetSize;

            phsAIInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte) / 1000));
            phsBIInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte + 8) / 1000));
            phsCIInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte + 16) / 1000));
            phsAUInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte + 32) / 62));
            phsBUInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte + 40) / 62));
            phsCUInst.getInstMag().getF().setValue((double) (byteArrayToInt(data, strDataByte + 48) / 62));

            if (PosA.getStVal().getValue() != null && PosA.getStVal().getValue().equals(DPC.Values.OFF)) {
                phsAIInst.getInstMag().getF().setValue(0.0);
            }
            if (PosB.getStVal().getValue() != null && PosB.getStVal().getValue().equals(DPC.Values.OFF)) {
                phsBIInst.getInstMag().getF().setValue(0.0);
            }
            if (PosC.getStVal().getValue() != null && PosC.getStVal().getValue().equals(DPC.Values.OFF)) {
                phsCIInst.getInstMag().getF().setValue(0.0);
            }

        } catch (Exception e) {log.error("Cannot parse sv packet");}

    }

    public static int byteArrayToInt(byte[] b, int offset){
        return b[offset + 3] & 0xFF | (b[offset + 2] & 0xFF) << 8 | (b[offset + 1] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
    }
}
