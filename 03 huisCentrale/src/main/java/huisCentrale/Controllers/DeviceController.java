package huisCentrale.Controllers;

import Authentication.Authenticate;
import Device.Device;
import Database.DBConnection;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fazecast.jSerialComm.SerialPort;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class DeviceController extends TimerTask {


    public static HashMap<String, Device> devices = new HashMap<String, Device>();

    public static Collection<Device> getAllDevices(){
        return devices.values();
    }

    public static void addDevice(String comport, Device device){
        devices.put(comport,device);
    }
    public static void removeDevice(String comport){
        devices.remove(comport);
    }

    public static void clearDevices(){
        for(Device an: devices.values()){
            an.stop();
        }
        devices.clear();
    }

    public static void start() throws InterruptedException {
        for(Device an: devices.values()){

            System.out.println(an);
            System.out.println(an.getComport());
            an.start();
            TimeUnit.SECONDS.sleep(5);
//            an.update();
        }
    }

    public static void getAllConnectedDevices(){
        for(SerialPort sp: SerialPort.getCommPorts()){
            System.out.println(sp.getCTS());
            System.out.println(sp.getSystemPortName());
        }
    }

    // deze kan nog beter met een update value
    @PostMapping("/setDeviceStatus")
    public ResponseEntity<?> setDeviceStatus(@RequestParam(value = "deviceName") String deviceName, @RequestParam(value = "setValue") String setValue){
        System.out.println("Setting Device: " + deviceName);
        for(Device device: devices.values()){
            if(device.getDeviceName().equalsIgnoreCase(deviceName)){
                    device.sendCommand(setValue);
                }
        }
        return new ResponseEntity<String>("Device set", HttpStatus.OK);
    }

    @RequestMapping("devices/update")
    public static void update(@RequestParam(value="deviceID", defaultValue="0") int deviceId){
//          for(Device an: devices.values()){
//            if (an.getdeviceId() == deviceId) {
////                an.update();
//                break;
//            }
//        }
    }

    @RequestMapping("devices/loadDevices")
    public static void loadDevices(){
       clearDevices();
//       run();
    }

    @RequestMapping("devices/authenticate")
    public static void authenticate(){
        Authenticate authenticate = new Authenticate();
        if(authenticate.verifyCentrale()){
//            DeviceController.run();
        }

    }

//    public static void run(){
////        setAllDevices();
//        try {
//            start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void setAllDevicesAutomatic(){


    }

//    public static void setAllDevices() {
//        DBConnection conn = new DBConnection();
//        Connection c = conn.connection();
//        ArrayList<Device> allDevices = new ArrayList<>();
//        try
//        {
//            String query = "SELECT d.deviceID, d.devicePort,d.deviceState, d.deviceType, d.AnalogState FROM centrale c LEFT JOIN device D on d.centraleID = c.centraleID WHERE c.centraleMac =  (?)";
//            PreparedStatement pstate = c.prepareStatement(query);
//            pstate.setString(1, Authenticate.getId());
//            ResultSet result = pstate.executeQuery();
//            while(result.next())
//            {
//                int deviceID = result.getInt("deviceID");
//                String comPort = result.getString("devicePort");
//                int baudRate = 9600;
//                String deviceState = result.getString("deviceState");
//                String type = result.getString("deviceType");
//                String analogState = result.getString("AnalogState");
//                Device ar = new Device(deviceID, comPort, baudRate, deviceState, type, analogState);
//                allDevices.add(ar);
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
//        devices = allDevices;
//    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        if (!(devices.size() == SerialPort.getCommPorts().length)){
            clearDevices();
        }
        for(SerialPort sp: SerialPort.getCommPorts()){
            String key = sp.getSystemPortName();
            System.out.println(key);
            if(!devices.containsKey(key)){
                Device device = new Device(sp.getSystemPortName(), sp.getBaudRate());
                addDevice(key, device);
                device.start();
            }
            else{
                System.out.println("all devices already added");
            }
        }

    }

}
