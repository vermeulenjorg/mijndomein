package huisCentrale.Controllers;

import Device.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fazecast.jSerialComm.SerialPort;
import java.util.*;

/**
 * <h1>DeviceController HuisCentrale Mijn Domein</h1>
 * De devicecontroller draait in een eigen thread volgens een timertask.
 * Dit zorgt ervoor dat deze niet continu draait maar alleen op een vooraf
 * ingesteld interval zijn code uitvoerd.
 *
 * @author  Groep 2 Mijn Domein
 * @version 1.0
 * @since   2019-01-01
 */

@RestController
public class DeviceController extends TimerTask {

    public static HashMap<String, Device> devices = new HashMap<String, Device>();

    /**
     * Deze methode voegt een apparaat toe aan de Hashmap devices zodat de
     * DeviceContoller weet welke apparaten zjin aangesloten en bestuurd moeten worden.
     * @param comport Ieder apparaat werkt op een eigen comport. Deze zal als identifier worden gebruikt
     * @param device  Dit is het toe te voegen apparaat
     */
    public static void addDevice(String comport, Device device){
        devices.put(comport,device);
    }

    /**
     * Deze methode verwijderd een device uit de hashmap
     * @param comport Ieder apparaat werkt op een eigen comport. Deze zal als identifier worden gebruikt om te verwijderen
     */
    public static void removeDevice(String comport){
        devices.remove(comport);
    }

    /**
     * Deze methode verwijderd alle devices en voert voor elk device ook een stop methode uit zodat deze ook goed gesloten wordt.
     */
    public static void clearDevices(){
        for(Device an: devices.values()){
            an.stop();
        }
        devices.clear();
    }

    /**
     * Deze methode wordt aangeroepen vanuit de bedrijfsserver. Deze zorgt ervoor dat de waarde die vanuit
     * deze server wordt verstuurd naar het juiste apparaat wordt doorgestuurd.
     * DeviceContoller weet welke apparaten zjin aangesloten en bestuurd moeten worden.
     * @param deviceName Ieder apparaat werkt heeft een unieke naam waarmee deze geidentificeerd kan worden.
     * @param setValue  Dit is de waarde die naar het apparaat moet worden doorgestuurd.
     * @return ResponseEntity Stuurt een bericht terug naar de bedrijfsserver dat alles goed ontvangen is.
     */
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

    /**
     * Deze methode is de daadwerkelijke run methode die elke seconde wordt gedraaid
     * Deze detecteerd of er een wijziging is geweest in het aantal aangesloten apparaten
     * en mocht dit zo zijn dan verwijderd deze ze allemaal.
     * Voor ieder aangesloten apparaat op een serieele port wordt vervolgens gekeken of deze al
     * toegevoegd is. Als dit niet zo is dan gebeurd dit alsnog en wordt het apparaat gestart en zal
     * deze in zijn eigen thread verder communiceren met de bedrijfsserver.
     */
    @Override
    public void run() {
        if (!(devices.size() == SerialPort.getCommPorts().length)){
            clearDevices();
        }
        for(SerialPort sp: SerialPort.getCommPorts()){
            String key = sp.getSystemPortName();
            if(!devices.containsKey(key)){
                System.out.println(key);
                Device device = new Device(sp.getSystemPortName(), sp.getBaudRate());
                addDevice(key, device);
                device.start();
            }
            else{
                // do nothing
            }
        }
    }
}
