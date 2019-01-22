package bedrijfsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try {
            Process process = new ProcessBuilder("C:\\mosquitto\\mosquitto.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(Application.class, args);

    }
}
