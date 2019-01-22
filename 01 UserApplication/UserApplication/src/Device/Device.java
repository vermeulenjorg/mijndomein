package Device;

import SystemControls.DeviceControls;
import User.UserControls;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Device
{

    public int devicePort;
    public String deviceType;
    public String deviceState;
    public String deviceName;
    public int analogState;
    public int measuredAnalogState;
    public int deviceID;
    public int centraleID;

    DeviceControls controls = new DeviceControls();

    /**
     * Constructor.
     * Sets the name, type and port when device is created.
     * @param deviceName The name for the new device.
     * @param devicePort The port for the new device.
     * @param deviceType The type of the new device ex. ANALOG or DIGITAL.
     */
    public Device(String deviceName, int devicePort, String deviceType, int centraleID)
    {
        this.deviceName = deviceName;
        this.devicePort = devicePort;
        this.deviceType = deviceType;
        this.centraleID = centraleID;
    }

    /**
     * Adds a device to the database.
     * Gets the connection from UserControls.
     * Adds device to database and adds current user as owner.
     */
    public void saveNewDevice() throws SQLException, IOException
    {
        Connection c = UserControls.connection;

        int userID = UserControls.userID;
        int deviceID = 0;
        int centraleID = 0;

        String saveQuery = "INSERT INTO device (deviceName, deviceType, devicePort, deviceState, centraleID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstate = c.prepareStatement(saveQuery, Statement.RETURN_GENERATED_KEYS);
        pstate.setString(1, getDeviceName());
        pstate.setString(2, getDeviceType());
        pstate.setInt(3, getDevicePort());
        pstate.setString(4, getDeviceState());
        pstate.setInt(5, getCentraleID());
        pstate.executeUpdate();
        ResultSet result = pstate.getGeneratedKeys();

        while (result.next())
        {
            deviceID = result.getInt(1);
        }

        String userQuery = "INSERT INTO userdevices (userID, deviceID) VALUES (?, ?)";
        PreparedStatement pst = c.prepareStatement(userQuery);
        pst.setInt(1, userID);
        pst.setInt(2, deviceID);

        pst.executeUpdate();
        controls.updateDevice(deviceID, centraleID);
    }

    /**
     * Deletes a device from the database.
     * @param deviceID The ID of the device that needs to be deleted.
     */
    public void deleteDevice(int deviceID) throws SQLException, IOException
    {
        Connection c = UserControls.connection;
        Device device = controls.deviceObject(deviceID);

        String deleteQuery = "DELETE FROM device WHERE deviceID = (?)";
        PreparedStatement pstate = c.prepareStatement(deleteQuery);
        pstate.setInt(1, deviceID);

        pstate.executeUpdate();
        controls.updateDevice(deviceID, device.getCentraleID());
    }

    /* GETTERS AND SETTERS */

    public int getDeviceID()
    {
        return deviceID;
    }

    public void setDeviceID(int deviceID)
    {
        this.deviceID = deviceID;
    }

    public int getDevicePort()
    {
        return devicePort;
    }

    public void setDevicePort(int devicePort)
    {
        this.devicePort = devicePort;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getDeviceState()
    {
        return deviceState;
    }

    public void setDeviceState(String deviceState)
    {
        this.deviceState = deviceState;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public int getAnalogState()
    {
        return analogState;
    }

    public void setAnalogState(int analogState)
    {
        this.analogState = analogState;
    }

    public int getMeasuredAnalogState()
    {
        return measuredAnalogState;
    }

    public void setMeasuredAnalogState(int measuredAnalogState)
    {
        this.measuredAnalogState = measuredAnalogState;
    }

    public int getCentraleID()
    {
        return centraleID;
    }

    public void setCentraleID(int centraleID)
    {
        this.centraleID = centraleID;
    }
}