package main;
import arduino.*;
import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

//import java.util.concurrent.TimeUnit;


public class Arduino_test {
    static int mod = 0;

    public static void main(String[] args) throws InterruptedException {
        String ArduinoPort = "COM5"; //Your port name here
        SerialPort userPort = SerialPort.getCommPort(ArduinoPort);
        userPort.setBaudRate(9600);

        userPort.openPort();

        if (userPort.isOpen()) {
            System.out.println("Port initialized!");


            //timeout not needed for event based reading
            //userPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        } else {
            System.out.println("Port not available");
            return;
        }

        userPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                byte[] newData = new byte[100];;
//                newData = new byte[userPort.bytesAvailable()];
                userPort.readBytes(newData,userPort.bytesAvailable());



//                int numRead = userPort.readBytes(newData, newData.length);
//                System.out.println("Read " + numRead + " bytes.");
//                System.out.println(newData);

                String s = new String(newData);
                System.out.print(s);

            }
        });
        for(int i= 0; i <10; i++){

            mod = i % 2;

            String str = Integer.toString(mod);
            System.out.println("Sending: " + str);
            byte[] b = str.getBytes();
            userPort.writeBytes(b,5);
            TimeUnit.SECONDS.sleep(10);
        }





//        String ArduinoPort = "COM5"; //Your port name here
//        int BAUD_RATE = 9600;
//        Arduino arduino = new Arduino(ArduinoPort, BAUD_RATE);
//        arduino.openConnection();
//        System.out.println(arduino.getPortDescription());
////        arduino.serialWrite('1'); //serialWrite is an overridden method, allowing both characters and strings.
////        arduino.serialWrite('1', 20); //its second parameter even allows delays. more details can be found in the documentation.
//        System.out.println("Connected");
//        for(int i = 0; i < 100;i++){
//            System.out.println("send" + i);
////            System.out.println(arduino.serialRead(2));
//
//            arduino.serialWrite(Integer.toString(i));
////            System.out.println(arduino.serialRead(0b1));
//            TimeUnit.SECONDS.sleep(10);
        }



    }
