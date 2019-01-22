package Main;

import Device.Device;
import Group.Group;
import User.UserControls;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main
{
    public static void main(String[] args) throws IOException, SQLException
    {
        UserControls uControl = new UserControls();
        /* TEST CODE - ADD DEVICES */

//        uControl.addDevice("Device1", "Lamp", 1, "ANALOG");
//        uControl.addDevice("Device2", "Lamp", 1, "ANALOG");
//        uControl.addDevice("Device3", "Lamp", 1, "DIGITAL");
//        uControl.addDevice("Device4", "Lamp", 1, "ANALOG");
//        uControl.addDevice("Device5", "Lamp", 1, "DIGITAL");
//        System.out.println("Devioes added");
//        System.out.println(uControl.showDevices());
//
//        System.out.println();
//
//        /* TEST CODE - CREATE GROUPS */
//        uControl.createGroup("Groep1");
//        uControl.createGroup("Groep2");
//
//        /* TEST CODE - SHOW GROUPS */
//        System.out.println("GROUPS: " + uControl.showGroups());
//        System.out.println();
//
//        /* TEST CODE - ADD DEVICES TO GROUPS */
//        uControl.addDeviceToGroup(1, 1);
//        uControl.addDeviceToGroup(1, 2);
//        uControl.addDeviceToGroup(3, 1);
//        uControl.addDeviceToGroup(5, 1);
//        uControl.addDeviceToGroup(2, 2);
//
//        /* TEST CODE - SHOW DEVICES IN GROUPS */
//        Group group = UserControls.groupList.get(0);
//        Group group1 = UserControls.groupList.get(1);
//        int ID = group.getID();
//        System.out.println(ID + " " + group.getName());
//        System.out.println("GROUP1 DEVICES: " + group.showDevices());
//
//        /* REMOVE GROUP AND SHOW GROUPS AND GROUP-DEVICES */
//        System.out.println("GROUP REMOVE NOW");
//        uControl.removeGroup(2);
//        System.out.println("GROUPS: " + uControl.showGroups());
//        System.out.println();
//        System.out.println("ALL DEVICES: " + uControl.showDevices());
//        System.out.println(ID + " " + group.getName());
//        System.out.println("GROUP2 DEVICES" + group1.showDevices());
//
//        /* REMOVE A DEVICE */
//        uControl.removeDevice(1);
//        System.out.println("GROUP2 DEVICES" + group1.showDevices());
//        System.out.println(UserControls.devList.get(0).getDeviceName());

        /* TEST CODE - CHANGE DEVICE STATE ON OR OFF*/
//        uControl.changeGroupStatus(2, 1);
//        for(Device device : group1.deviceList)
//        {
//            System.out.println(device.getDeviceState());
//        }
//        uControl.changeGroupStatus(2, 0);
//        for(Device device : group1.deviceList)
//        {
//            System.out.println(device.getDeviceState());
//        }


            uControl.login("user", "user");
//        uControl.addDevice("Device15", "DIGITAL", 15);
//        System.out.println(uControl.login(2,1345));
//        uControl.createGroup("Groep7");
//            uControl.deleteDevice(1);
//        uControl.deleteGroup(1);
//        uControl.addDeviceToGroup(2, 4);
//          uControl.removeDeviceFromGroup(2);
//        uControl.showDeviceState(4);
//        uControl.showGroups();
//        System.out.println(uControl.showGroups());
//        System.out.println(uControl.showDevicesInGroup(8));
//        uControl.showDevicesInGroup(8);
//        uControl.setGroupActivationState(8);
//        uControl.deviceActivation(13);
//        uControl.showAllDevices();
//        uControl.deviceActivation(13);
//        System.out.println(uControl.showAllDevices());
//        System.out.println(uControl.checkLastDigitalState(13));
//        System.out.println(uControl.showAllDevices());
//        System.out.println(uControl.showDeviceState(3));
//        System.out.println(uControl.getAllCentraleID(4));
//        uControl.addUser("user", "user");


    }

}
