package Device;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import huisCentrale.Application;
import huisCentrale.Controllers.DeviceController;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private String comPort;
    private int baudRate;

    private SerialPort serialPort;

    private String deviceState;
    private String deviceType;
    StringBuilder message;
//    private static List instances = new ArrayList();
    private String deviceName;
    private String deviceMeasure;
//  private int deviceID;
//    private String type;

    public Device(int deviceID, String comPort, int baudRate, String deviceState, String type){
        message = new StringBuilder();
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.serialPort = SerialPort.getCommPort(this.comPort);
    }

    public Device(String comPort, int baudRate){
        message = new StringBuilder();
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.serialPort = SerialPort.getCommPort(this.comPort);
    }



    public String getDeviceType(){
        return deviceType;
    }




//    public Device(int deviceID, String comPort, int baudRate, String deviceState, String type, String analogState){
//
//        message = new StringBuilder();
//        this.comPort = comPort;
//        this.baudRate = baudRate;
//        this.type = type;
//        this.deviceID = deviceID;
//        this.deviceState =deviceState;
//        this.serialPort = SerialPort.getCommPort(this.comPort);
//        this.analogState = analogState;
//
//    }
//
//    public void update(){
//        DBConnection conn = new DBConnection();
//        Connection c = conn.connection();
//        try
//        {
//            String query = "SELECT deviceState,AnalogState FROM device WHERE deviceID = (?)";
//            PreparedStatement pstate = c.prepareStatement(query);
//            pstate.setInt(1, this.deviceID);
//            ResultSet result = pstate.executeQuery();
//            while(result.next())
//            {
//                if (this.type.equalsIgnoreCase("ANALOG")){
//                    this.analogState = result.getString("AnalogState");
//                    this.sendCommand(this.analogState);
//                }
//                else if (this.type.equalsIgnoreCase("DIGITAL")){
//                    this.deviceState = result.getString("deviceState");
//                    this.sendCommand(this.deviceState.toUpperCase());
//                }
//            }
//            c.close();
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        catch (NullPointerException n)
//        {
//            n.printStackTrace();
//        }
//    }

    public String getDeviceName(){
        return deviceName;
    }

//    public String getanalogState(){
//        return this.analogState;
//    }

    public String getdeviceState(){
        return this.deviceState;
    }

    public String getComport(){
        return this.comPort;
    }

//    public int getdeviceId(){
//        return this.deviceID;
//    }

    public int getBaudRate(){
        return this.baudRate;
    }

//    public String getType(){
//        return this.type;
//    }

    public void start(){
        connect();
        System.out.println(serialPort.isOpen());
        addListener();
    }

    public void stop(){
//        if (serialPort.isOpen()){
            serialPort.removeDataListener();
            serialPort.closePort();
            System.out.println("Closed" + serialPort.getDescriptivePortName());
//        }
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
        this.serialPort.setBaudRate(this.baudRate);
        this.serialPort.setNumDataBits(8);
        this.serialPort.openPort();
    }
//
//    public void insertStatement(String value) {
//        DBConnection conn = new DBConnection();
//        Connection c = conn.connection();
//        try {
//            String addQuery = "INSERT INTO deviceLogging (deviceID, measure) VALUES (?, ?)";
//            PreparedStatement pstate = c.prepareStatement(addQuery);
//            pstate.setInt(1, deviceID);
//            pstate.setString(2, value);
//            pstate.executeUpdate();
//            c.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateStatement(String value) {
//        DBConnection conn = new DBConnection();
//        Connection c = conn.connection();
//        if(this.getType().equalsIgnoreCase("ANALOG")){
//        try {
//            String[] parts = value.substring(1).split("/");
//            if (parts.length == 3){
//                if(!parts[1].equalsIgnoreCase("SET") ){
//                    String addQuery = "UPDATE Device SET measuredAnalogState = (?), deviceState = (?) WHERE deviceName = (?)";
//                    PreparedStatement pstate = c.prepareStatement(addQuery);
//                    pstate.setString(1, parts[2]);
//                    pstate.setString(2, parts[1]);
//                    pstate.setString(3, parts[0]);
//                    pstate.executeUpdate();
//                    c.close();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    }




    public void addListener() {
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                try{
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(buffer, serialPort.bytesAvailable());
                    for (byte b: buffer) {
                        message.append((char) b);
                        if ((b == 13 || b == 10)&& message.length() > 1) {
                            String toProcess = message.toString();
                            String[] parts = toProcess.substring(1).split("/");
                            if (parts.length == 4){
                                if (parts[0].length() == 6){
                                    deviceName = parts[0];
                                    deviceType = parts[1];
                                    deviceState = parts[2];
                                    deviceMeasure = parts[3];
                                    System.out.println(toProcess);
                                    try {
                                        String url = "http://localhost:8080";
                                        CloseableHttpClient client = HttpClients.createDefault();
                                        HttpPost httpPost = new HttpPost(url + "/adding");

                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("huisCentrale", Application.id));
                                        params.add(new BasicNameValuePair("deviceName", deviceName));
                                        params.add(new BasicNameValuePair("deviceType", deviceType));
                                        params.add(new BasicNameValuePair("deviceState", deviceState));
                                        params.add(new BasicNameValuePair("deviceMeasure", deviceMeasure));
                                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                                        CloseableHttpResponse response = client.execute(httpPost);
                                        client.close();
                                    }catch (Exception e){

                                    }
                                }
                            }
                            message.setLength(0);
                        }
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    System.out.println("Device Disconnected");
                    stop();
                    DeviceController.removeDevice(comPort);
                }
            }
        });
    }





//    private static void getEmployees()
//    {
//        final String uri = "http://localhost:8080/springrestexample/employees.xml";
//
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
//
//        System.out.println(result);
//    }








}
