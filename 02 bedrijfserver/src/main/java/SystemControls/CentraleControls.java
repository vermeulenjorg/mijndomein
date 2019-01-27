package SystemControls;

import Centrale.Centrale;
import Database.DBConnection;
import Device.Device;
import hello.Controls.UserControls;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CentraleControls {

//    public DeviceControls dControls = new DeviceControls();

    public CentraleControls()
    {

    }

    public void deleteCentrale(int centraleID) throws SQLException
    {
        Connection c = UserControls.connection;

        String deleteQuery = "DELETE FROM centrale WHERE centraleID = (?)";
        PreparedStatement pstate = c.prepareStatement(deleteQuery);
        pstate.setInt(1, centraleID);
        pstate.executeUpdate();
    }

    public String getCentraleUrl(String centraleNaam) throws SQLException
    {
        Connection c = UserControls.connection;
        String url = "";
        String query = "SELECT centraleUrl FROM centrale WHERE centraleNaam = (?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, centraleNaam);

        ResultSet result = p.executeQuery();
        if(result.next())
        {
            url = result.getString("centraleUrl");
        }
        return url;
    }

    public List showAllCentrale() throws SQLException
    {
        Connection c = UserControls.connection;
        int userID = UserControls.userID;
        ArrayList<Centrale> centrales = new ArrayList<Centrale>();

        String query = "SELECT c.centraleID, c.centraleNaam FROM centrale c LEFT JOIN usercentrales uc ON c.centraleID = uc.centraleID WHERE uc.userID = (?)";
        PreparedStatement p  = c.prepareStatement(query);
        p.setInt(1, userID);
        ResultSet result = p.executeQuery();

        while(result.next())
        {
            int centraleID = result.getInt("centraleID");
            String centraleNaam = result.getString("centraleNaam");
            Centrale centrale = centraleObject(centraleNaam);
            centrales.add(centrale);
        }
        return centrales;
    }

    public List showDevicesInCentrale(String centraleNaam) throws SQLException
    {
        DeviceControls dControls = new DeviceControls();

        ArrayList<Device> devices = new ArrayList<Device>();
        Connection c = UserControls.connection;

        String query = "SELECT deviceID FROM device WHERE centraleID = (?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, centraleNaam);
        ResultSet result = p.executeQuery();

        while(result.next())
        {
            int deviceID = result.getInt("deviceID");
            Device device = dControls.deviceObject(deviceID);
            devices.add(device);
        }
        return devices;
    }

    public Centrale centraleObject(String centraleNaam) throws SQLException
    {
        Connection c = UserControls.connection;
        Centrale centrale = null;

        String selectQuery = "SELECT * FROM centrale WHERE centraleNaam = (?)";
        PreparedStatement p = c.prepareStatement(selectQuery);
        p.setString(1, centraleNaam);

        ResultSet result = p.executeQuery();

        while (result.next()) {
            int cID = result.getInt("centraleID");
            String centraleName = result.getString("centraleNaam");
            String centraleUrl = result.getString("centraleUrl");
            String centraleMac = result.getString("centraleMac");

            centrale = new Centrale(centraleName, centraleUrl, centraleMac);
            centrale.setCentraleID(cID);
        }
        return centrale;
    }

    public void updateCentrale(String centraleID) throws SQLException, IOException
    {
        Connection c = UserControls.connection;
        String url = getCentraleUrl(centraleID);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url + "/devices/authenticate");
        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

    }

    public void addCentrale(String huisCentrale) throws SQLException {
        DBConnection c = new DBConnection();
        Connection conn = c.connection();


        if (getCentrale(huisCentrale)){
            //do niks

        }
        else{
            //insert

            String insertQuery = "INSERT INTO centrale (centraleID, centraleNaam, centraleURL) VALUES (NULL, (?),(?))";
            PreparedStatement p = conn.prepareStatement(insertQuery);
            p.setString(1, huisCentrale);
            p.setString(2, "http://localhost:56788");
            p.executeUpdate();

        }
        conn.close();


    }

    private boolean getCentrale(String huisCentrale) throws SQLException {
        boolean exists= false;
        DBConnection c = new DBConnection();
        Connection conn = c.connection();
        Device device = null ;
        String selectQuery = "SELECT * FROM centrale WHERE centraleNaam = (?)";
        PreparedStatement p = conn.prepareStatement(selectQuery);
        p.setString(1, huisCentrale);
        ResultSet result = p.executeQuery();

        if(result.next())
        {

            exists= true;
        }
        conn.close();
        return exists;

    }
}
