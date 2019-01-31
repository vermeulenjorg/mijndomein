package Device;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import huisCentrale.Application;
import huisCentrale.Controllers.DeviceController;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Device HuisCentrale Mijn Domein</h1>
 * Ieder device wordt aangestuurd vanuit de deviceController
 * Ieder device heeft een comport, baudrate en serialport. Aan de hand van de comport wordt
 * de serialport (verbinding) gemaakt. Deze zijn allen nodig om de verbinding te maken.
 * Daarnaast is ieder device van een bepaald type en heeft het een state en
 * measure. Die laatste geldt alleen als het een analoog device is.
 *
 * @author  Groep 2 Mijn Domein
 * @version 1.0
 * @since   2019-01-01
 */
public class Device {
    private String comPort;
    private int baudRate;
    private SerialPort serialPort;
    private String deviceState;
    private String deviceType;
    StringBuilder message;
    private String deviceName;
    private String deviceMeasure;

    public Device(int deviceID, String comPort, int baudRate, String deviceState, String type){
        message = new StringBuilder();
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.serialPort = SerialPort.getCommPort(this.comPort);
    }

    public Device(String comPort, int baudRate){
        message = new StringBuilder();
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.serialPort = SerialPort.getCommPort(this.comPort);
    }

    /**
     * Deze methode haalt de deviceName op. Deze wordt gebruikt om een device te identificeren
     * @return String deviceName de naam van het device
     */
    public String getDeviceName(){
        return deviceName;
    }

    /**
     * Deze methode start het device door deze te connecten en vervolgens een listener toe te voegen op de port.
     */
    public void start(){
        connect();
        System.out.println(serialPort.isOpen());
        addListener();
    }

    /**
     * Deze methode stopt het device door de listener te verwijderen en vervolgens de connectie op de poort te sluiten.
     */
    public void stop(){
            serialPort.removeDataListener();
            serialPort.closePort();
            System.out.println("Closed" + serialPort.getDescriptivePortName());
    }

    /**
     * Deze methode stuurt berichten door naar het aangesloten device
     * @param Command  Dit is de waarde die naar het apparaat moet worden doorgestuurd.
     */
    public void sendCommand(String Command){
        Command = Command + "\n";
        byte[] b = encryt(Command);
        this.serialPort.writeBytes(b,b.length);
    }

    /**
     * Deze methode encrypt berichten die over de serieele port gestuurd moeten worden.
     * Dit zijn namelijke bytes en geen Strings
     * @param message  Het te encrypten bericht
     * @return Byte[] de encrypte byte Array
     */
    public byte[] encryt(String message){
        byte[] b = message.getBytes();
        return b;
    }

    /**
     * Deze methode set alle waarden voor een connectie over een serieele poort op deze port en open deze port vervolgens
     */
    public void connect(){
        this.serialPort.setBaudRate(this.baudRate);
        this.serialPort.setNumDataBits(8);
        this.serialPort.openPort();
    }

    /**
     * Deze methode stuurt de berichten van de device door naar de bedrijfsserver mits deze volledig zijn.
     */
    public void sendToServer (String toProcess){
        String[] parts = toProcess.substring(1).split("/");
        if (parts.length == 4){
            if (parts[0].length() == 6){
                deviceName = parts[0];
                deviceType = parts[1];
                deviceState = parts[2];
                deviceMeasure = parts[3];
                System.out.println(toProcess);
                try {
                    String url = "http://localhost:8080";
                    CloseableHttpClient client = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost(url + "/adding");

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("huisCentrale", Application.id));
                    params.add(new BasicNameValuePair("deviceName", deviceName));
                    params.add(new BasicNameValuePair("deviceType", deviceType));
                    params.add(new BasicNameValuePair("deviceState", deviceState));
                    params.add(new BasicNameValuePair("deviceMeasure", deviceMeasure));
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    CloseableHttpResponse response = client.execute(httpPost);
                    client.close();
                }catch (Exception e){

                }
            }
        }
    }

    /**
     * Deze listener methode wordt uitgevoerd zolang de serieele port open is en de listener is toegevoegd.
     * Deze luistert naar de waarden die binnen komen als een byte array.
     * Vervolgens decode hij deze naar een string. Deze wordt ook weer gecontroleerd op volledigheid omdat
     * er soms bytes verloren kunnen gaan. Dit gebeurdt meestal alleen bij het starten of sluiten van de port omdat
     * het zenden vanuit de port zijn eigen frequentie heeft en deze listener er op een willekeurig de berichten begint
     * te ontvangen. Zodra er een goed bericht is ontvangen wordt deze doorgestuurd door middel van een post
     */
    public void addListener() {
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                try{
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(buffer, serialPort.bytesAvailable());
                    for (byte b: buffer) {
                        message.append((char) b);
                        if ((b == 13 || b == 10)&& message.length() > 1) {
                            String toProcess = message.toString();
                            sendToServer(toProcess);
                            message.setLength(0);
                        }
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    System.out.println("Device Disconnected");
                    stop();
                    DeviceController.removeDevice(comPort);
                }
            }
        });
    }
}
