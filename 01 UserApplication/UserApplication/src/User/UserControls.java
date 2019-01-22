package User;

import Authentication.Authenticate;
import Centrale.Centrale;
import Database.DBConnection;
import Device.Device;
import Group.Group;
import PwdHash.PwdHash;
import SystemControls.CentraleControls;
import SystemControls.DeviceControls;
import SystemControls.GroupControls;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserControls
{
    public static Connection connection;
    public static int userID;

    DeviceControls dControls = new DeviceControls();
    GroupControls gControls = new GroupControls();
    CentraleControls cControls = new CentraleControls();

    /**
     * Constructor UserControls.
     */
    public UserControls()
    {

    }

    public void addUser(String userName, String password) throws SQLException
    {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.saveUser();
    }

    /**
     * Checks if userName are password are correct.
     * If Authentication is true a connection is made else connection is null.
     * @param userName The provided username.
     * @param password The provided password.
     */
    public void login(String userName, String password)
    {
        try
        {
            Authenticate aUser = new Authenticate();
            DBConnection conn = new DBConnection();
            Connection connection = conn.connection();

            if (aUser.verifyUser(userName, password) == false)
            {
                connection.close();
                this.connection = connection;
            }
            else if (aUser.verifyUser(userName, password) == true)
            {
                User user = new User();
                int userID = user.getUserID(userName, password);

                this.userID = userID;
                this.connection = connection;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Adds a device that will be stored in in the database.
     * @param deviceName The name of the new device.
     * @param deviceType The type of the new device ex. ANALOG or DIGITAL.
     * @param devicePort The port on which the device is attached.
     */
    public void addDevice(String deviceName, String deviceType, int devicePort, int centraleID) throws IOException, SQLException
    {
        Device device = new Device(deviceName, devicePort, deviceType, centraleID);
        device.saveNewDevice();
    }

    /**
     * Deletes the selected device from the database.
     * @param deviceID The ID of the device that need to be deleted.
     */
    public void deleteDevice(int deviceID) throws SQLException, IOException
    {
        Device device = dControls.deviceObject(deviceID);
        device.deleteDevice(deviceID);
    }

    /**
     * Activates or deactivates a device based on its current state.
     * If current state is ON device will be turned OFF and vice versa.
     * @param deviceID The ID of the device that needs to be changed.
     */
    public void deviceActivation(int deviceID) throws IOException, SQLException
    {
        dControls.setDeviceActivationState(deviceID);
    }

    /**
     * Shows if the device is turned ON or OFF.
     * @param deviceID The ID of the device which state need to be retrieved.
     * @return Returns the current state of the device.
     */
    public String showDeviceState(int deviceID) throws SQLException
    {
        return dControls.showState(deviceID);
    }

    /**
     * Shows all devices of the current user.
     * @return List with all device ID's.
     */
    public List<Device> showAllDevices() throws SQLException
    {
        return dControls.showAllDevices();
    }

    /**
     * Creates a device group in the database.
     * @param groupName The name of the new created group.
     */
    public void createGroup(String groupName) throws SQLException
    {
        Group group = new Group(groupName);
        group.saveGroup();
    }

    /**
     * Deletes a group from the database.
     * @param groupID The ID of the group that need to be deleted.
     */
    public void deleteGroup(int groupID) throws SQLException
    {
        gControls.deleteGroup(groupID);
    }

    /**
     * Shows all groups of the current user.
     * @return List with all group ID's.
     */
    public List<Group> showGroups() throws SQLException
    {
        return gControls.showGroups();
    }

    /**
     * Shows all devices in a specific group.
     * @param groupID The ID of the group which devices need to be shown.
     * @return List with all device ID's.
     */
    public List<Device> showDevicesInGroup(int groupID) throws SQLException
    {
        return gControls.showDevicesInGroup(groupID);
    }

    /**
     * Add a device to a group.
     * @param deviceID The ID of the device that need to be added to a group.
     * @param groupID The ID of the group where the device need to be added to.
     */
    public void addDeviceToGroup(int deviceID, int groupID) throws SQLException
    {
        gControls.addDevice(deviceID, groupID);
    }

    /**
     * Removes a device from its current group.
     * @param deviceID The ID of the device that need to be removed from a group.
     */
    public void removeDeviceFromGroup(int deviceID, int groupID) throws SQLException
    {
        gControls.removeDevice(deviceID, groupID);
    }

    /**
     * Activates or deactivates all devices in a group by turning it ON or OFF based on the current state.
     * @param groupID The ID of the group which devices need to be turned ON or OFF.
     */
    public void setGroupActivationState(int groupID) throws IOException, SQLException
    {
        gControls.setGroupActivationState(groupID);
    }

    public List<Integer> getAllCentraleID(int userID) throws SQLException
    {
        Connection c = UserControls.connection;
        ArrayList<Integer> allCentrale = new ArrayList<>();

        String query = "SELECT userID, centraleID FROM usercentrales WHERE userID = (?)";
        PreparedStatement pt = c.prepareStatement(query);
        pt.setInt(1, userID);
        ResultSet result = pt.executeQuery();

        while(result.next())
        {
            int centraleID = result.getInt("centraleID");
            allCentrale.add(centraleID);
        }
        return allCentrale;
    }

    public void addCentrale(String centraleNaam, String centraleUrl, String centraleMac) throws SQLException, IOException
    {
        Centrale centrale = new Centrale(centraleNaam, centraleUrl, centraleMac);
        centrale.saveCentrale();
    }

    public void deleteCentrale(int centraleID) throws SQLException
    {
        cControls.deleteCentrale(centraleID);
    }

    public List showAllCentrale() throws SQLException
    {
       List<Centrale> centrales = cControls.showAllCentrale();
       return centrales;
    }

    public List showDevicesInCentrale(int centraleID) throws SQLException
    {
        List<Device> devices = cControls.showDevicesInCentrale(centraleID);
        return devices;
    }
}
