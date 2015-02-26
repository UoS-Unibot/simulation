package org.unisim.reality;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

/**
 * Provides a higher level interface to the UnibotComms class, specifying which
 * commands should be sent and listening for data.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimpleUnibotController implements DataReceivedListener {

    public static class DataPacket {

        final PacketType type;
        double[] values;
        boolean isEmptyPacket = false;

        DataPacket(PacketType type, String dataToParse) {
            // TODO: move to UnibotComms, fairly low level so that is a more appropriate place for it
            this.type = type;
            if (dataToParse.isEmpty()) {
                isEmptyPacket = true;
            } else if (!dataToParse.startsWith("#")) {
                throw new IllegalArgumentException("Non standard data packet received; must start with # symbol. Received data: " + dataToParse);
            } else {
                parseDataStr(dataToParse.substring(1));
            }
        }

        private void parseDataStr(String dataStr) {
            if (type == PacketType.ACK) {
                values = new double[]{};
            } else {
                ArrayList<String> dataArr = Lists.newArrayList(Splitter.on(' ').trimResults().omitEmptyStrings().split(dataStr));
                values = new double[dataArr.size()];
                for (int i = 0; i < dataArr.size(); i++) {
                    values[i] = Double.parseDouble(dataArr.get(i));
                }
            }
        }

        public PacketType getType() {
            return type;
        }

        public double[] getValues() {
            return values;
        }

    }

    @Override
    public void dataReceivedEvent(String dataReceived) {
        if (lastDataRequest == null) {
            return;
        }
        lastData.put(PacketType.SONAR, parseDataStr(lastDataRequest, dataReceived));
    }

    private double[] parseDataStr(PacketType type, String dataStr) {
        double[] values;
        if (type == PacketType.ACK) {
            values = new double[]{};
        } else {
            ArrayList<String> dataArr = Lists.newArrayList(Splitter.on(' ').trimResults().omitEmptyStrings().split(dataStr));
            values = new double[dataArr.size()];
            for (int i = 0; i < dataArr.size(); i++) {
                values[i] = Double.parseDouble(dataArr.get(i));
            }
        }
        return values;
    }

    public double[] getLastData(PacketType key) {
        return lastData.get(key);
    }

    public enum PacketType {

        ACK("0"), ENCODER_SONAR("1"), ENCODER("2"), SONAR("3"), RANGE_ALL("4"), IMU("5"), RANGE_SINGLE("");

        private final String cmd;

        public String getCmd() {
            return cmd;
        }

        PacketType(String cmd) {
            this.cmd = cmd;
        }
    }

    public SimpleUnibotController() throws SerialPortException {
        unibot = new UnibotComms();
        unibot.setDataReceivedListener(this);
        unibot.sendCommand(UnibotCommands.SET_PAUSE_STATE, "1");
    }

    private final UnibotComms unibot;
    private final Timer timer = new Timer();
    private final Collection<RobotDataPacketListener> listeners = new LinkedList<>();
    private final HashMap<PacketType, double[]> lastData = new HashMap<>();

    private PacketType lastDataRequest;

    public void addListener(RobotDataPacketListener listener) {
        listeners.add(listener);
    }

    public void driveDiff(int timeMSec, float velocity, float wheelDiff) throws SerialPortException {

        unibot.doDifferentialDrive(velocity, wheelDiff);

        final Date endTime = new Date();
        endTime.setTime(endTime.getTime() + timeMSec);
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            unibot.doDifferentialDrive(0, 0);
                        } catch (SerialPortException ex) {
                            Logger.getLogger(SimpleUnibotController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                endTime
        );
    }

    public void requestData(PacketType type) throws SerialPortException {
        if (type == PacketType.RANGE_SINGLE) {
            throw new IllegalArgumentException("Cannot request single rangefinder data through requestData(PacketType.RANGE_SINGLE); use requestRange() instead.");
        } else {
            unibot.sendCommand(UnibotCommands.DATA_GET_PACKET, type.getCmd());
            lastDataRequest = type;
        }
    }

    public void requestRange() throws SerialPortException {
        unibot.sendCommand(UnibotCommands.RANGEFINDER_GET_RANGE);
        lastDataRequest = PacketType.RANGE_SINGLE;
    }

}
