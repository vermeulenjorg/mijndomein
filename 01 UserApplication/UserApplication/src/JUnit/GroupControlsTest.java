package JUnit;

import Device.Device;
import Group.Group;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupControlsTest {

    ArrayList<Device> devices = new ArrayList<Device>();
    ArrayList<Group> groups = new ArrayList<Group>();

    @Before
    public void setUp() throws Exception {
        Group group1 = new Group("groep1");
        Group group2 = new Group("groep2");
        Group group3 = new Group("groep3");
        Group group4 = new Group("groep4");
        Group group5 = new Group("groep5");

        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);
        groups.add(group5);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void currentActivationState() {
        groups.get(0).setActivationState("ON");
        assertEquals("ON", groups.get(0).getActivationState());

    }

    @Test
    public void showGroups() {
        assertEquals("size of the grouplist", 5, groups.size());
    }

    @Test
    public void showDevicesInGroup() {
        ArrayList<Device> groep1 = new ArrayList<Device>();

        Device device6 = new Device("device1", 1, "ANALOG", 1);
        Device device7 = new Device("device2", 1, "ANALOG", 1);
        Device device8 = new Device("device3", 1, "DIGITAL", 1);
        Device device9 = new Device("device4", 1, "ANALOG", 1);
        Device device10 = new Device("device5", 1, "DIGITAL", 1);

        groep1.add(device6);
        groep1.add(device7);
        groep1.add(device8);
        groep1.add(device9);
        groep1.add(device10);

        assertEquals("size of devices in group", 5, groep1.size());
    }
}