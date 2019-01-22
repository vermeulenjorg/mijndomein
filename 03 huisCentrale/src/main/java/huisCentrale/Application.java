package huisCentrale;

import Identification.centraleId;
import huisCentrale.Controllers.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class Application {
/**
 * Maakt de ID voor de server (MAC ADRES)
 * RUNT de webserver voor communicatie
 * RUNT alle devices die geregistreerd staan op deze huiscentrale
 */

    public static void main(String[] args) {
        centraleId.setId();
        SpringApplication.run(Application.class, args);
        DeviceController.run();


    }

}
