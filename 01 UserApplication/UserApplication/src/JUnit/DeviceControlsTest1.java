package JUnit;

import Device.Device;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeviceControlsTest1 {

    ArrayList<Device> devices = new ArrayList<Device>();

    @Before
    public void setUp() throws Exception {
        Device device1 = new Device("device1", 1, "ANALOG", 1);
        Device device2 = new Device("device2", 1, "ANALOG", 1);
        Device device3 = new Device("device3", 1, "DIGITAL", 1);
        Device device4 = new Device("device4", 1, "ANALOG", 1);
        Device device5 = new Device("device5", 1, "DIGITAL", 1);

        devices.add(device1);
        devices.add(device2);
        devices.add(device3);
        devices.add(device4);
        devices.add(device5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setDeviceActivationState() {
        devices.get(2).setDeviceState("ON");
        assertEquals("ON", devices.get(2).getDeviceState());
    }

    @Test
    public void showState() {
        devices.get(0).setAnalogState(20);
        assertEquals(20, devices.get(0).getAnalogState());

        devices.get(2).setAnalogState(20);
        devices.get(2).setDeviceState("ON");
        assertEquals("ON", devices.get(2).getDeviceState());
    }

    @Test
    public void showAllDevices() {
        assertEquals("size of devicelist", 5, devices.size());
    }

}