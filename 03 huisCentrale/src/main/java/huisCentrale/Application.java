package huisCentrale;


import huisCentrale.Controllers.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Timer;

/**
 * <h1>HuisCentrale Mijn Domein</h1>
 * Iedere huiscentrale heeft een eigen ID welke meegeleverd wordt op de doos.
 * Als eerste zal er een spring boot applicatie gestart worden welke alle netwerkverkeer mogelijk maakt.
 * Na het opstarten zal na 1 seconde de huiscentrale beginnen te werken en start een deviceController
 * Iedere seconde zal er worden gekeken of er nieuwe apparaten zijn aangesloten
 * Deze deviceController start in zijn eigen Thread.
 * Ieder aangesloten apparaat zal zichzelf bij de server melden met de authenticatie van de huiscentrale.
 * Deze handeld de request verder af.
 *
 *
 * @author  Groep 2 Mijn Domein
 * @version 1.0
 * @since   2019-01-01
 */

@SpringBootApplication
public class Application {
    private static long delay  = 1000L;
    private static long period = 1000L;
    public static String id = "386dae03-d604-4166-aa17-a8a0af0b0848";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        DeviceController pm = new DeviceController();
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(pm, delay, period);
     }

}
