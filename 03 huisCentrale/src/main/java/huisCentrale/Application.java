package huisCentrale;

import huisCentrale.Controllers.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Timer;

@SpringBootApplication
public class Application {
    private static long delay  = 1000L;
    private static long period = 1000L;
    public static String id = "386dae03-d604-4166-aa17-a8a0af0b0848";
/**
 * Maakt de ID voor de server (MAC ADRES)
 * RUNT de webserver voor communicatie
 * RUNT alle devices die geregistreerd staan op deze huiscentrale
 */

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // kan een test worden:
//        Device device = new Device(1, "COM5", 9600, "OFF", "ANALOG", "10");
//        device.start();
/** Dit stuk maakt een nieuwe Thread aan die scant of er nieuwe devices worden aangesloten. Deze worden dan ook automatisch aangezet.
 *  Het luisteren naar nieuwe poorten gebeurd iedere 10 seconden.
 // */
        DeviceController pm = new DeviceController();
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(pm, delay, period);






        // werkend:
//             for(SerialPort sp: SerialPort.getCommPorts()){
//            String key = sp.getSystemPortName();
////            if(first) {
////            if (!DeviceController.deviceExists(key)){
//                Device device = new Device(1, sp.getSystemPortName(), sp.getBaudRate(),"OFF", "ANALOG");
//                device.start();
//                DeviceController.addDevice(device);
////                device.start();
////                first = false;
////            }
//
//        }
//







//        Authenticate authenticate = new Authenticate();
//        DeviceController.getAllConnectedDevices();



//        PortManager pm = new PortManager();
//        Timer timer = new Timer("Timer");
//        timer.scheduleAtFixedRate(pm, delay, period);

//        allDevices.add(ar);

//        DeviceController.run();






//        System.out.println(authenticate.verifyCentrale());
//        if(authenticate.verifyCentrale()){
//
//        }
     }

}
