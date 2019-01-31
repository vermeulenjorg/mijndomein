package Authentication;

import Database.DBConnection;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Authenticate {

    private boolean loginState;
    private static String id;

    /**
     * Constructor.
     */
    public Authenticate()
    {

    }

    /**
     * Checks user credentials in the Database.
     * Connection is always closed after checking credentials.
     * @return Returns true if username and password are correct else false.
     */
    public boolean verifyCentrale()
    {

        try
        {
            DBConnection conn = new DBConnection();
            Connection c = conn.connection();
            String query = "SELECT * FROM centrale WHERE centraleMac = (?)";
            PreparedStatement pstate = c.prepareStatement(query);
            pstate.setString(1, id);
            ResultSet rset = pstate.executeQuery();

            if(rset.next())
            {
                loginState = true;
            }
            else
            {
                loginState = false;
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return loginState;
    }

    public static String getId() {
        return id;
    }
    /**
     * set de Id
     */
    private static void setId(){
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            id = sb.toString();
            System.out.println(id);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }
    }

}
