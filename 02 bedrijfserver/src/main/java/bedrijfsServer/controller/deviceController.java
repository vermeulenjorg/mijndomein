package bedrijfsServer.controller;


import database.DBConnection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class deviceController {
    public static Connection c;

    @RequestMapping("/updateDevice")
    public void updateDevice(@RequestParam(value="deviceId", defaultValue="1") int id, @RequestParam(value="state", defaultValue="off") String state, @RequestParam(value="degrees", defaultValue="0") String degrees) {
        System.out.println("id: " + id);
        System.out.println("state: " + state);
        System.out.println("degrees: " +degrees);
    }

    @RequestMapping("/setDeviceState")
    public void setDeviceActivationState(@RequestParam(value="deviceId", defaultValue="1") int deviceID, @RequestParam(value="state", defaultValue="off") String state, @RequestParam(value="degrees", defaultValue="0") String degrees) {
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();
        try
        {
            String query = "SELECT c.centraleUrl FROM centraledevice cd left join centrale c on c.centraleId = cd.centraleId where cd.deviceID = (?)";
            PreparedStatement pstate = c.prepareStatement(query);
            pstate.setInt(1, deviceID);
            ResultSet result = pstate.executeQuery();
            while(result.next())
            {
                String url = result.getString("centraleUrl") + "/devices/update?deviceID=" + deviceID;
                sendGet(url);
            }

        c.close();
    }
            catch (SQLException e)
    {
        e.printStackTrace();
    }
            catch (NullPointerException n)
    {
        n.printStackTrace();
    } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void sendGet(String url) throws Exception {

       URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.getResponseCode();

    }
    }
