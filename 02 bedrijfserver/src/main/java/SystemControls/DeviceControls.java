package SystemControls;

import Database.DBConnection;
import Device.Device;
import SystemControls.CentraleControls;
import hello.Controls.UserControls;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeviceControls
{
    CentraleControls cControls = new CentraleControls();

    /**
     * Constructor DeviceControls.
     */
    public DeviceControls()
    {

    }

    /**
     * Activate or deactivate a device based on the current state.
     * Checks if the the device type is Digital.
     * If digital then user will be prompted to enter desired degrees.
     * @param deviceID The ID of the device that needs to be changed.
     */
    public void setDeviceActivationState(int deviceID, int value) throws SQLException, IOException
    {
        Connection c = UserControls.connection;
        Device device = deviceObject(deviceID);
        String currentState = device.getDeviceState();
        String type = device.getDeviceType();
        String activationState;
        String query = "UPDATE device SET deviceState = (?) WHERE deviceID = (?)";
        PreparedStatement pstate = c.prepareStatement(query);
        pstate.setInt(2, deviceID);

        if(type.equalsIgnoreCase("analog"))
        {
            valueNeeded(deviceID, value);
        }
        else
        {
            if(currentState.equalsIgnoreCase("on"))
            {
                activationState = "OFF";
                pstate.setString(1, activationState);
                pstate.executeUpdate();
                System.out.println(deviceID + " " + value);
                sendCommand(deviceID, value);
            }
            else if(currentState.equalsIgnoreCase("off"))
            {
                activationState = "ON";
                pstate.setString(1, activationState);
                pstate.executeUpdate();
                sendCommand(deviceID,value);
            }
        }
    }

    /**
     * Shows the current device state (ON or OFF).
     * @param deviceID The ID of the device which state needs to be retrieved.
     * @return The current device state.
     */
    public String showState(int deviceID) throws SQLException
    {
        Connection c = UserControls.connection;
        String deviceState;
        Device device = deviceObject(deviceID);
        String deviceType = device.getDeviceType();

        if(deviceType.equalsIgnoreCase("analog"))
        {
            deviceState = Integer.toString(device.getMeasuredAnalogState());
        }
        else
        {
            deviceState = device.getDeviceState();
        }
        return deviceState;
    }

    /**
     * Shows all devices.
     * @return List with devices.
     */
    public List<Device> showAllDevices() throws SQLException, NullPointerException
    {
        ArrayList<Device> allDevices = new ArrayList<Device>();
        Connection c = UserControls.connection;
        int userID = UserControls.userID;

        String query = "SELECT d.deviceID FROM device d LEFT JOIN userdevices ud ON d.deviceID = ud.deviceID WHERE ud.userID = (?)";
        PreparedStatement pstate = c.prepareStatement(query);
        pstate.setInt(1, userID);
        ResultSet result = pstate.executeQuery();

        while(result.next())
        {
           int deviceID = result.getInt("deviceID");
           Device device = deviceObject(deviceID);
           allDevices.add(device);
        }
        return allDevices;
    }

    public Device deviceObject(int deviceID) throws SQLException
    {
        Connection c = UserControls.connection;
        Device device = null ;

        String selectQuery = "SELECT deviceID, deviceName, devicePort, deviceType, deviceState, analogState, measuredAnalogState, centraleID FROM device WHERE deviceID = (?)";
        PreparedStatement p = c.prepareStatement(selectQuery);
        p.setInt(1, deviceID);

        ResultSet result = p.executeQuery();

        while(result.next())
        {
            int devID = result.getInt("deviceID");
            int devicePort = result.getInt("devicePort");
            String cID = result.getString("centraleID");
            String deviceName = result.getString("deviceName");
            String deviceType = result.getString("deviceType");
            String deviceState = result.getString("deviceState");
            int analogState = result.getInt("analogState");
            int measuredAnalogState= result.getInt("measuredAnalogState");

            device = new Device(deviceName, devicePort, deviceType, cID);
            device.setDeviceID(devID);
            device.setDeviceState(deviceState);
            device.setAnalogState(analogState);
            device.setMeasuredAnalogState(measuredAnalogState);
        }
        return device;
    }

    public void valueNeeded(int deviceID, int value) throws IOException, SQLException
    {
        Connection c = UserControls.connection;

        Scanner in = new Scanner(System.in);
        Device device = deviceObject(deviceID);

        int newDigitalState = value;
        String dQuery = "UPDATE device SET analogState = (?) WHERE deviceID = (?)";
        PreparedStatement pt = c.prepareStatement(dQuery);
        pt.setInt(1, newDigitalState);
        pt.setInt(2, deviceID);
        pt.executeUpdate();
        sendCommand(deviceID, value);
    }

    public void updateDevice(int deviceID, String centraleID) throws IOException, SQLException
    {
        Connection c = UserControls.connection;
        String url = cControls.getCentraleUrl(centraleID);

        String dID = Integer.toString(deviceID);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url + "/setDeviceStatus");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("deviceId", dID));
        params.add(new BasicNameValuePair("centraleID", centraleID));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();
    }

    public void sendCommand(int deviceID, int setValue) throws IOException, SQLException
    {

        Device device = deviceObject(deviceID);
        String test = device.getCentraleID();

        String url = cControls.getCentraleUrl(device.getCentraleID());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url + "/setDeviceStatus");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("deviceName", device.getDeviceName()));
        params.add(new BasicNameValuePair("setValue", Integer.toString(setValue)));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();
    }

    public void addDevice(Device device) throws SQLException {
//        System.out.println(dev.getCentraleID());
        DBConnection c = new DBConnection();
        Connection conn = c.connection();

        if (getDevice(device.getDeviceName())) {
            //update
            String insertQuery = "UPDATE device SET measuredAnalogState = (?), deviceState = (?) WHERE deviceName = (?)";
            PreparedStatement p = conn.prepareStatement(insertQuery);
            p.setInt(1, device.getMeasuredAnalogState());
            p.setString(2, device.getDeviceState());
            p.setString(3, device.getDeviceName());
            p.executeUpdate();
        } else {
            //insert
            String insertQuery = "INSERT INTO device (deviceID, deviceName, deviceType, measuredAnalogState,  centraleID) VALUES (NULL, (?),(?),(?),(?))";
            PreparedStatement p = conn.prepareStatement(insertQuery);
            p.setString(1, device.getDeviceName());
            p.setString(2, device.getDeviceType());
            p.setInt(3, device.getMeasuredAnalogState());
            p.setString(4, device.getCentraleID());
            p.executeUpdate();

        }
        conn.close();
    }

    public boolean getDevice(String deviceName) throws SQLException{
        boolean exists = false;
        DBConnection c = new DBConnection();
        Connection conn = c.connection();
        String selectQuery = "SELECT * FROM device WHERE deviceName = (?)";
        PreparedStatement p = conn.prepareStatement(selectQuery);
        p.setString(1, deviceName);
        ResultSet result = p.executeQuery();
        if(result.next())
        {
            exists = true;

        }
        conn.close();
        return exists;

    }

}
