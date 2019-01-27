package hello.Controls;

import Authentication.Authenticate;
import Centrale.Centrale;
import Database.DBConnection;
import Device.Device;
import Group.Group;
import SystemControls.CentraleControls;
import SystemControls.DeviceControls;
import SystemControls.GroupControls;
import hello.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Log.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserControls {
    public static Connection connection;
    public static int userID;

    DeviceControls dControls = new DeviceControls();
    GroupControls gControls = new GroupControls();
    CentraleControls cControls = new CentraleControls();

    /**
     * Constructor UserControls.
     */
    public UserControls() {

    }

    /**
     * Checks if userName are password are correct.
     * If Authentication is true a connection is made else connection is null.
     * //     * @param userName The provided username.
     * //     * @param password The provided password.
     */

    @GetMapping("/login")
    public String showLoginForm() {
        {
//            model.addAttribute("greeting", new Greeting());
            return "login";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Greeting greeting) {

        String userName = greeting.getId();
        String password = greeting.getContent();
        Authenticate aUser = new Authenticate();

        try {
            DBConnection conn = new DBConnection();
            Connection connection = conn.connection();

            if (aUser.verifyUser(userName, password) == false) {
                connection.close();
                this.connection = connection;
            } else if (aUser.verifyUser(userName, password) == true) {
                this.connection = connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (aUser.loginState == true) {
            return "redirect:dashboard";
        } else {
            System.out.println("cant connect");
            return "greeting";
        }

    }

    /* CODE SHY */
    @GetMapping("/dashboard")
    public String dashboard()
    {
        return "dashboard";
    }



    /* END CODE SHY*/


    /**
     * Adds a device that will be stored in in the database.
     *
//     * @param deviceName The name of the new device.
//     * @param deviceType The type of the new device ex. ANALOG or DIGITAL.
//     * @param devicePort The port on which the device is attached.
     */

    @GetMapping("/createdevice")
    public String createDevice()  {
        return "createdevice";
    }

    @PostMapping("/createdevice")
    public String createDeviceSubmit(@ModelAttribute Device device) throws SQLException, IOException {
        device.saveNewDevice();
        return "redirect:groups";
    }


    /**
     * Deletes the selected device from the database.
     *
     * @param deviceID The ID of the device that need to be deleted.
     */
    @PostMapping("/deletedevices")
    public String deleteDevice(@RequestParam (value = "deviceid") int deviceID) throws SQLException, IOException {
        Device device = dControls.deviceObject(deviceID);
        device.deleteDevice(deviceID);
        return "redirect:alldevices";
    }

    @GetMapping("/deletedevices")
    public String deleteDev()
    {
        return "redirect:alldevices";
    }

//    /**
//     * Activates or deactivates a device based on its current state.
//     * If current state is ON device will be turned OFF and vice versa.
//     *
//     * @param deviceID The ID of the device that needs to be changed.
//     */

//    @GetMapping("/activatedevice")
//    public String deviceAct()
//    {
//        return "activatedevice";
//    }

    @PostMapping("/showdevicesinstation")
    public String deviceActivation(@RequestParam(value = "deviceid") int deviceID, @RequestParam(value = "centraleid") String centraleID,  @RequestParam(value = "Value", defaultValue = "0") int value) throws IOException, SQLException {
        dControls.setDeviceActivationState(deviceID, value);
//        System.out.println(deviceID + " " +value);
//        String referrer = request.getHeader("Referrer");
//        System.out.println(referrer);
        Centrale centrale = cControls.centraleObject(centraleID);
        return "redirect:showdevicesinstation?centraleNaam="+centraleID;
//        http://localhost:8080/showdevicesinstation?centraleID=1
    }

    @PostMapping("/alldevices")
    public String deviceActivation1(@RequestParam(value = "deviceid") int deviceID,  @RequestParam(value = "Value", defaultValue = "0") int value) throws IOException, SQLException {
        dControls.setDeviceActivationState(deviceID, value);
//        System.out.println(deviceID + " " +value);
//        String referrer = request.getHeader("Referrer");
//        System.out.println(referrer);
        return "redirect:alldevices";
//        http://localhost:8080/showdevicesinstation?centraleID=1
    }

    @PostMapping("/showdevicesingroup")
    public String deviceActivation2(@RequestParam(value = "deviceid") int deviceID,  @RequestParam(value = "Value", defaultValue = "0") int value, @RequestParam(value = "groupid") int groupID) throws IOException, SQLException {
        dControls.setDeviceActivationState(deviceID, value);
//        System.out.println(deviceID + " " +value);
//        String referrer = request.getHeader("Referrer");
//        System.out.println(referrer);
        return "redirect:showdevicesingroup?groupid=" + groupID;
//        http://localhost:8080/showdevicesinstation?centraleID=1
    }

    /**
     * Shows if the device is turned ON or OFF.
     *
     * @param deviceID The ID of the device which state need to be retrieved.
     * @return Returns the current state of the device.
     */
    public String showDeviceState(int deviceID) throws SQLException {
        return dControls.showState(deviceID);
    }

    /* CODE SHY */
    /**
     * Shows all devices of the current user.
     *
     * @return List with all device ID's.
     */
    @GetMapping("/alldevices")
    public String showAllDevices(Model model) throws SQLException {
        List<Device> devices =  dControls.showAllDevices();

        ArrayList<Device> aDevices = new ArrayList<Device>();
        ArrayList<Device> dDevices = new ArrayList<Device>();

        for(int i = 0; i < devices.size(); i++)
        {
            if(devices.get(i).getDeviceType().equalsIgnoreCase("ANALOG"))
            {
                aDevices.add(devices.get(i));
            }
            else
            {
                dDevices.add(devices.get(i));
            }
        }

        model.addAttribute("aDevices", aDevices);
        model.addAttribute("dDevices", dDevices);
        model.addAttribute("devices", devices);
        return "alldevices";
    }

    /* END CODE SHY */

    /**
     * Creates a device group in the database.
     *
     * @param groupName The name of the new created group.
     */

    @GetMapping("/creategroup")
    public String createGroup(@RequestParam(value = "groupName", defaultValue = "0") String groupName)  {
        return "createGroup";
    }

    @PostMapping("creategroup")
    public String groupSubmit(@ModelAttribute Group group)throws SQLException {
        group.saveGroup();
        return "redirect:allgroups";
}

    /**
     * Deletes a group from the database.
     * @param groupID The ID of the group that need to be deleted.
     */

    @PostMapping("/deletegroup")
    public String deleteGroup(@RequestParam(value = "groupid") int groupID) throws SQLException {
        gControls.deleteGroup(groupID);
        return "redirect:allgroups";
    }


    /**
     * Shows all groups of the current user.
     * @return List with all group ID's.
     */
    @GetMapping("/allgroups")
    public String showGroups(Model model) throws SQLException
    {

        List<Group>  groups = gControls.showGroups();
//        ArrayList<Device> analogDevices = new ArrayList<Device>();
//        ArrayList<Device> digitalDevices =  new ArrayList<Device>();
//        for (Device device: dControls.showAllDevices()){
//            if (device.getDeviceType().equalsIgnoreCase("ANALOG")){
//                analogDevices.add(device);
//            }
//            else if (device.getDeviceType().equalsIgnoreCase("DIGITAL")){
//                digitalDevices.add(device);
//            }
//        }
//        model.addAttribute("analogDevices", analogDevices);
//        model.addAttribute("digitalDevices", digitalDevices);

        model.addAttribute("groups", groups);


        return "allgroups";
    }

    /**
     * Shows all devices in a specific group.
     * @param groupID The ID of the group which devices need to be shown.
     * @return List with all device ID's.
     */

    @GetMapping("/showdevicesingroup")
    public String showDevicesInGroup (@RequestParam(value = "groupid") int groupID, Model model) throws SQLException{

        List<Device> devices = gControls.showDevicesInGroup(groupID);
        List<Device> allUserDevices = dControls.showAllDevices();

        ArrayList<Device> analogDevices = new ArrayList<Device>();
        ArrayList<Device> digitalDevices =  new ArrayList<Device>();
        for (Device device: devices){
            if (device.getDeviceType().equalsIgnoreCase("ANALOG")){
                analogDevices.add(device);
            }
            else if (device.getDeviceType().equalsIgnoreCase("DIGITAL")){
                digitalDevices.add(device);
            }
        }

        model.addAttribute("allUserDevices", allUserDevices);
        model.addAttribute("groupid", groupID);
        model.addAttribute("devices", devices);
        model.addAttribute("aDevices", analogDevices);
        model.addAttribute("dDevices", digitalDevices);
        return "showdevicesingroup";

    }




    /**
     * Add a device to a group.
//     * @param deviceID The ID of the device that need to be added to a group.
//     * @param groupID The ID of the group where the device need to be added to.
     */

//    public String adddevicetoGroup(@RequestParam(value = "groupName", defaultValue = "0") String groupName)  {
//        return "adddevicetogroup";
//    }
//
//
//    public String adddevicetogroupSubmit(@ModelAttribute int groupID, int deviceID)throws SQLException {
//        gControls.addDevice(deviceID, groupID);
//        return "redirect:groups";
//    }

    @PostMapping("/addtogroup")
    public String addDeviceToGroup(@RequestParam (value = "deviceid") int deviceID, @RequestParam (value = "groupid") int groupID) throws SQLException
    {
        gControls.addDevice(deviceID, groupID);
        return "redirect:showdevicesingroup?groupid="+groupID;
    }

    /**
     * Removes a device from its current group.
     * @param deviceID The ID of the device that need to be removed from a group.
     */
    @PostMapping("/removefromgroup")
    public String removeDeviceFromGroup(@RequestParam (value = "deviceid") int deviceID, @RequestParam (value = "groupid") int groupID) throws SQLException
    {
        gControls.removeDevice(deviceID, groupID);
        return "redirect:showdevicesingroup?groupid="+groupID;
    }

    /**
     * Activates or deactivates all devices in a group by turning it ON or OFF based on the current state.
     * @param groupID The ID of the group which devices need to be turned ON or OFF.
     */
    @PostMapping("/groupactivation")
    public String setGroupActivationState(@RequestParam(value = "groupid") int groupID, @RequestParam(value = "Value", defaultValue = "0") int value) throws IOException, SQLException
    {
        gControls.setGroupActivationState(groupID, value);
        return "redirect:allgroups";
    }

//    public List<Integer> getAllCentraleID(int userID) throws SQLException
//    {
//        Connection c = UserControls.connection;
//        ArrayList<Integer> allCentrale = new ArrayList<>();
//
//        String query = "SELECT userID, centraleID FROM usercentrales WHERE userID = (?)";
//        PreparedStatement pt = c.prepareStatement(query);
//        pt.setInt(1, userID);
//        ResultSet result = pt.executeQuery();
//
//        while(result.next())
//        {
//            int centraleID = result.getInt("centraleID");
//            allCentrale.add(centraleID);
//        }
//        return allCentrale;
//    }

//    public void addCentrale(String centraleNaam, String centraleUrl, String centraleMac) throws SQLException, IOException
//    {
//        Centrale centrale = new Centrale(centraleNaam, centraleUrl, centraleMac);
//        centrale.saveCentrale();
//    }

    @PostMapping("/deletecentrale")
    public String deleteCentrale(@RequestParam(value = "centraleid") int centraleID) throws SQLException
    {
        cControls.deleteCentrale(centraleID);
        return "redirect:stations";
    }

    @GetMapping("/stations")
    public String showAllCentrale(Model model) throws SQLException
    {
        List<Centrale> centrales = cControls.showAllCentrale();
        model.addAttribute("centrales", centrales);
        return "stations";
    }

    @GetMapping("/showdevicesinstation")
    public String showDevicesInCentrale(@RequestParam(value = "centraleNaam") String centraleNaam, Model model) throws SQLException
    {
        List<Device> devices = cControls.showDevicesInCentrale(centraleNaam);
        ArrayList<Device> aDevices = new ArrayList<Device>();
        ArrayList<Device> dDevices = new ArrayList<Device>();

        for(int i = 0; i < devices.size(); i++)
        {
            if(devices.get(i).getDeviceType().equalsIgnoreCase("ANALOG"))
            {
                aDevices.add(devices.get(i));
            }
            else
            {
                dDevices.add(devices.get(i));
            }
        }
        model.addAttribute("centraleid", centraleNaam);
        model.addAttribute("devices", devices);
        model.addAttribute("aDevices", aDevices);
        model.addAttribute("dDevices", dDevices);
        return "showdevicesinstation";
    }

    @PostMapping("/adding")
    public ResponseEntity<?> newBazz(@RequestParam(value = "huisCentrale", defaultValue = "0") String huisCentrale, @RequestParam(value = "deviceName", defaultValue = "0") String deviceName, @RequestParam(value = "deviceType", defaultValue = "0") String deviceType, @RequestParam(value = "deviceState", defaultValue = "0") String deviceState, @RequestParam(value = "deviceMeasure", defaultValue = "0") String deviceMeasure){
        String logString = (String.valueOf(System.currentTimeMillis())+ " " +huisCentrale + " " +deviceName + " " + deviceType + " " +deviceState + " " +deviceMeasure);
        System.out.println(logString);
        Device device = new Device(deviceName,5,deviceType,huisCentrale);
        device.setMeasuredAnalogState((int)Math.round(Double.valueOf(deviceMeasure)));
        device.setDeviceState(deviceState);
        try {
            dControls.addDevice(device);
            cControls.addCentrale(huisCentrale);
            Log log = new Log(logString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Device(deviceName, huisCentrale), HttpStatus.OK);
    }
}
