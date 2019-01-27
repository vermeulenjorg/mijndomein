package SystemControls;


import Device.Device;
import Group.Group;
import com.mysql.cj.protocol.Resultset;
import hello.Controls.UserControls;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupControls
{
    /**
     * Constructor GroupControls.
     */
    public GroupControls()
    {

    }

    /**
     * Deletes a group, without deleting devices.
     * Gets the connection from UserControls ans deletes the group.
     * @param groupID The ID of the group that needs to be deleted.
     */
    public void deleteGroup(int groupID) throws SQLException
    {
        Connection c = UserControls.connection;

        String deleteQuery = "DELETE FROM groep WHERE ID = (?)";
        PreparedStatement pstate = c.prepareStatement(deleteQuery);
        pstate.setInt(1, groupID);
        pstate.executeUpdate();
    }

    /**
     * Adds a device to a group.
     * Gets the connection from UserControls and adds the device to a group.
     * @param deviceID The ID of the device that needs to be added.
     * @param groupID The ID of the group where the device needs to be added to.
     */
    public void addDevice(int deviceID, int groupID) throws SQLException
    {
        if(!checkDeviceInGroup(deviceID, groupID))
        {
            Connection c = UserControls.connection;

            String addQuery = "INSERT INTO devicegroup VALUES (?, ?)";
            PreparedStatement pstate = c.prepareStatement(addQuery);
            pstate.setInt(1, deviceID);
            pstate.setInt(2, groupID);
            pstate.executeUpdate();
        }
    }

    /**
     * Removes the devices from a group.
     * @param deviceID The ID of the device that needs to be removed.
     * @param groupID The ID of the group where the device needs to be removed from.
     */
    public void removeDevice(int deviceID, int groupID) throws SQLException
    {
        Connection c = UserControls.connection;

        String deleteQuery = "DELETE FROM devicegroup WHERE deviceID = (?) AND groupID = (?)";
        PreparedStatement pstate = c.prepareStatement(deleteQuery);
        pstate.setInt(1, deviceID);
        pstate.setInt(2, groupID);
        pstate.executeUpdate();
    }

    /**
     * Sets the ActivationState of all devices in a group to ON or OFF based on the currentState.
     * The currentState is retrieved with helper method currentActivationState.
     * @param groupID The ID of the group which state needs to be changed to ON or OFF.
     */
    public void setGroupActivationState(int groupID, int value) throws IOException, SQLException
    {
        Connection c = UserControls.connection;

        String activationState;
        String currentState = currentActivationState(groupID);
        String selectQuery = "SELECT d.deviceID FROM device d LEFT JOIN devicegroup dg ON d.deviceID = dg.deviceID WHERE dg.groupID = (?)";
        String groupQuery = "UPDATE groep SET activationState = (?) WHERE ID = (?)";
        PreparedStatement prep = c.prepareStatement(groupQuery);
        PreparedStatement pst = c.prepareStatement(selectQuery);
        pst.setInt(1, groupID);
        ResultSet result = pst.executeQuery();

        if(currentState.equalsIgnoreCase("on"))
        {
            activationState = "OFF";
            prep.setString(1, activationState);
            prep.setInt(2, groupID);
            prep.executeUpdate();
        }
        else if(currentState.equalsIgnoreCase("off"))
        {
            activationState = "ON";
            prep.setString(1, activationState);
            prep.setInt(2, groupID);
            prep.executeUpdate();
        }

        int tempID = 0;
        while (result.next())
        {
            tempID = result.getInt("deviceID");
            DeviceControls controls = new DeviceControls();
            controls.setDeviceActivationState(tempID, value);
        }
    }

    /**
     * Helper method to check the current ActivationState of a group.
     * @param groupID The ID of the group which state needs to be returned.
     * @return The current ActivationState of the requested group
     */
    public String currentActivationState(int groupID) throws SQLException
    {
        Connection c = UserControls.connection;
        String currentState = "";

        String query = "SELECT activationState FROM groep WHERE ID = (?)";
        PreparedStatement pst = c.prepareStatement(query);
        pst.setInt(1, groupID);
        ResultSet result = pst.executeQuery();

        while (result.next())
        {
            currentState = result.getString("activationState");
        }
        return currentState;
    }

    /**
     * Shows all groups.
     * @return List with all group ID's.
     */
    public List<Group> showGroups() throws SQLException
    {
        ArrayList<Group> allGroups = new ArrayList<Group>();
        Connection c = UserControls.connection;
        int userID = UserControls.userID;

        String query = "SELECT g.ID, g.groupName, g.activationState FROM groep g LEFT JOIN usergroups ug ON g.ID = ug.groupID WHERE userID = (?)";
        PreparedStatement pstate = c.prepareStatement(query);
        pstate.setInt(1, userID);
        ResultSet result = pstate.executeQuery();

        while(result.next())
        {
            int groupID = result.getInt("ID");
            String groupName = result.getString("groupName");
            String state = result.getString("activationState");

            Group group = new Group(groupName);
            group.setGroupID(groupID);
            group.setActivationState(state);
            allGroups.add(group);
        }
        System.out.println(pstate);
        return allGroups;
    }

    /**
     * Shows all devices in a specific group.
     * @param groupID The groupID which devices need to be shown.
     * @return List with all device ID's.
     */
    public List<Device> showDevicesInGroup(int groupID) throws SQLException
    {
        Connection c = UserControls.connection;
        DeviceControls dControls = new DeviceControls();
        ArrayList<Device> devicesInGroup = new ArrayList<Device>();

        String query = "SELECT d.deviceID FROM devicegroup dg LEFT JOIN device d ON d.deviceID = dg.deviceID WHERE dg.groupID = (?)";
        PreparedStatement pstate = c.prepareStatement(query);
        pstate.setInt(1, groupID);
        ResultSet result = pstate.executeQuery();

        while(result.next())
        {
            int deviceID = result.getInt("deviceID");
            Device device = dControls.deviceObject(deviceID);
            devicesInGroup.add(device);
        }
        System.out.println(pstate);
        return devicesInGroup;
    }

    public boolean checkDeviceInGroup(int deviceID, int groupID) throws SQLException
    {
        boolean exists = false;
        Connection c = UserControls.connection;

        String selectQuery = "SELECT deviceID, groupID FROM devicegroup WHERE deviceID = (?) AND groupID = (?)";
        PreparedStatement p = c.prepareStatement(selectQuery);
        p.setInt(1, deviceID);
        p.setInt(2, groupID);

        ResultSet r = p.executeQuery();

        if(r.next())
        {
            exists = true;
        }
        return exists;
    }


}
