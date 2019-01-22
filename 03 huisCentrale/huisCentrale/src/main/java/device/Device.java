package device;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Device {
    private String comPort;
    private int baudRate;
    private String type;
    private SerialPort serialPort;
    private int deviceID;
    private String deviceState;
    private String analogState;
    StringBuilder message;
    private static List instances = new ArrayList();


    public Device(int deviceID, String comPort, int baudRate, String deviceState, String type, String analogState){

        message = new StringBuilder();
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.type = type;
        this.deviceID = deviceID;
        this.deviceState =deviceState;
        this.serialPort = SerialPort.getCommPort(this.comPort);
        this.analogState = analogState;

    }

    public void update(){
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();
        try
        {
            String query = "SELECT deviceState,digitalState FROM device WHERE deviceID = (?)";
            PreparedStatement pstate = c.prepareStatement(query);
            pstate.setInt(1, this.deviceID);
            ResultSet result = pstate.executeQuery();
            while(result.next())
            {
                if (this.type.equalsIgnoreCase("ANALOG")){
                    this.analogState = result.getString("digitalState");
                    this.sendCommand(this.analogState);
                }
                else if (this.type.equalsIgnoreCase("DIGITAL")){
                    this.deviceState = result.getString("deviceState");
                    this.sendCommand(this.deviceState.toUpperCase());
                }
            }
            c.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
        }
    }

    public String getanalogState(){
        return this.analogState;
    }

    public String getdeviceState(){
        return this.deviceState;
    }

    public String getComport(){
        return this.comPort;
    }

    public int getdeviceId(){
        return this.deviceID;
    }

    public int getBaudRate(){
        return this.baudRate;
    }

    public String getType(){
        return this.type;
    }

    public void start(){
        connect();
        addListener();
    }

    public void stop(){
        if (serialPort.isOpen()){
            serialPort.removeDataListener();
            serialPort.closePort();
        }
    }

    public void sendCommand(String Command){
        Command = Command + "\n";
        byte[] b = encryt(Command);
        this.serialPort.writeBytes(b,b.length);
    }

    public byte[] encryt(String message){
        byte[] b = message.getBytes();
        return b;
    }

    public String decryt(byte[] newData){
        String s = new String(newData);
        return s;
    }

    public void connect(){
        this.serialPort.setBaudRate(baudRate);
        this.serialPort.setNumDataBits(8);
        serialPort.openPort();
    }

    public void insertStatement(String value) {
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();
        try {
            String addQuery = "INSERT INTO deviceLogging (deviceID, measure) VALUES (?, ?)";
            PreparedStatement pstate = c.prepareStatement(addQuery);
            pstate.setInt(1, deviceID);
            pstate.setString(2, value);
            pstate.executeUpdate();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatement(String value) {
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();
        if(this.getType().equalsIgnoreCase("ANALOG")){
        try {
            String[] parts = value.substring(1).split("/");
            if (parts.length == 3){
                if(!parts[1].equalsIgnoreCase("SET") ){
                    String addQuery = "UPDATE device SET measuredDigitalState = (?), deviceState = (?) WHERE deviceName = (?)";
                    PreparedStatement pstate = c.prepareStatement(addQuery);
                     pstate.setString(1, parts[2]);
                    pstate.setString(2, parts[1]);
                    pstate.setString(3, parts[0]);
                    pstate.executeUpdate();
                    c.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

    public void addListener() {
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] buffer = new byte[serialPort.bytesAvailable()];
                try {
                    serialPort.readBytes(buffer, serialPort.bytesAvailable());
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                for (byte b: buffer) {
                    message.append((char) b);
                    if ((b == 13 || b == 10)&& message.length() > 1) {
                        String toProcess = message.toString();
                        // sent to database
                        insertStatement(toProcess);
                        updateStatement(toProcess);
                        message.setLength(0);
                    }
                }
            }
        });
    }
}
