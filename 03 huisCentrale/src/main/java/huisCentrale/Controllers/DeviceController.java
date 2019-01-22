package huisCentrale.Controllers;

import device.Device;
import database.DBConnection;
import Identification.centraleId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class DeviceController {
    static List<Device> devices = new ArrayList();

    public static List<Device> getAllDevices(){
        return devices;
    }

    public static void clearDevices(){
        for(Device an: devices){
            an.stop();
        }
        devices.clear();
    }

    public static void start() throws InterruptedException {
        for(Device an: devices){
            an.start();
            TimeUnit.SECONDS.sleep(5);
            an.update();
        }
    }

    @RequestMapping("devices/update")
    public static void update(@RequestParam(value="deviceID", defaultValue="0") int deviceId){
          for(Device an: devices){
            if (an.getdeviceId() == deviceId) {
                an.update();
                break;
            }
        }
    }

    @RequestMapping("devices/loadDevices")
    public static void loadDevices(){
       clearDevices();
       run();
    }

    public static void run(){
        setAllDevices();
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setAllDevices() {
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();
        ArrayList<Device> allDevices = new ArrayList<>();
        try
        {
            String query = "SELECT d.deviceID, d.devicePort,d.deviceState, d.deviceType, d.digitalstate FROM centraledevice cd LEFT JOIN centrale c ON c.centraleId = cd.centraleId LEFT JOIN device d ON d.deviceID = cd.deviceID WHERE c.centraleMac = (?)";
            PreparedStatement pstate = c.prepareStatement(query);
            pstate.setString(1, centraleId.getId());
            ResultSet result = pstate.executeQuery();
            while(result.next())
            {
                int deviceID = result.getInt("deviceID");
                String comPort = result.getString("devicePort");
                int baudRate = 9600;
                String deviceState = result.getString("deviceState");
                String type = result.getString("deviceType");
                String analogState = result.getString("digitalstate");
                Device ar = new Device(deviceID, comPort, baudRate, deviceState, type, analogState);
                allDevices.add(ar);
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
        devices = allDevices;
    }
}
