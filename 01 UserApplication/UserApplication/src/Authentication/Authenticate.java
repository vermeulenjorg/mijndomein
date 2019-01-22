package Authentication;

import Database.DBConnection;
import PwdHash.PwdHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Authenticate {

    private boolean loginState;

    /**
     * Constructor Authenticate.
     */
    public Authenticate()
    {

    }

    /**
     * Checks user credentials in the database.
     * Connection is always closed after checking credentials.
     * @param userName Provided username.
     * @param password Provided password.
     * @return Returns true if username and password are correct else false.
     */
    public boolean verifyUser(String userName, String password)
    {
        try
        {
            DBConnection conn = new DBConnection();
            Connection c = conn.connection();
            boolean verified = false;

            int logRounds = 12;
            PwdHash hash = new PwdHash(logRounds);
            String hashPwd = hash.hash(password);


            String query = "SELECT * FROM user WHERE userName = (?)";
            PreparedStatement pstate = c.prepareStatement(query);
            pstate.setString(1, userName);
//            pstate.setString(2, password);

            ResultSet rset = pstate.executeQuery();

            if(rset.next())
            {
                String pass = rset.getString("password");


                if(hash.verifyHash(password, pass)) {
                    loginState = true;
                    System.out.println(hash.verifyHash(password, pass));
                }
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

}
