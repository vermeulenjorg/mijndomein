package SystemControls;

import Centrale.Centrale;
import Device.Device;
import User.UserControls;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CentraleControls {

    public DeviceControls dControls = new DeviceControls();

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

    public String getCentraleUrl(int centraleID) throws SQLException
    {
        Connection c = UserControls.connection;
        String url = "";
        String query = "SELECT centraleUrl FROM centrale WHERE centraleID = (?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setInt(1, centraleID);

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

        String query = "SELECT centraleID FROM centrale c LEFT JOIN usercentrales uc ON c.centraleID = uc.centraleID WHERE uc.userID = (?)";
        PreparedStatement p  = c.prepareStatement(query);
        p.setInt(1, userID);
        ResultSet result = p.executeQuery();

        while(result.next())
        {
            int centraleID = result.getInt("centraleID");
            Centrale centrale = centraleObject(centraleID);
            centrales.add(centrale);
        }
        return centrales;
    }

    public List showDevicesInCentrale(int centraleID) throws SQLException
    {
        ArrayList<Device> devices = new ArrayList<Device>();
        Connection c = UserControls.connection;

        String query = "SELECT deviceID FROM device WHERE centraleID = (?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setInt(1, centraleID);
        ResultSet result = p.executeQuery();

        while(result.next())
        {
            int deviceID = result.getInt("deviceID");
            Device device = dControls.deviceObject(deviceID);
            devices.add(device);
        }
        return devices;
    }

    public Centrale centraleObject(int centraleID) throws SQLException
    {
        Connection c = UserControls.connection;
        Centrale centrale = null;

        String selectQuery = "SELECT * FROM centrale WHERE centraleID = (?)";
        PreparedStatement p = c.prepareStatement(selectQuery);
        p.setInt(1, centraleID);

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

    public void updateCentrale(int centraleID) throws SQLException, IOException
    {
        Connection c = UserControls.connection;
        String url = getCentraleUrl(centraleID);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url + "/devices/authenticate");
        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

    }
}
