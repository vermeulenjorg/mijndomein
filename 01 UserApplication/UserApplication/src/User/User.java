package User;

import Database.DBConnection;
import PwdHash.PwdHash;

import java.sql.*;

public class User
{
    public String userName;
    protected String password;

    /**
     * Constructor User.
     */
    public User()
    {

    }

    /**
     * Get the ID of the current user.
     * @param userName The provided username.
     * @param password The provided password.
     * @return Returns the userID.
     */
    public int getUserID(String userName, String password)
    {
        int userID = 0;

        try
        {
            DBConnection conn = new DBConnection();
            Connection c = conn.connection();

            String query = "SELECT ID FROM user WHERE userName = (?) AND password = (?)";
            PreparedStatement pst = c.prepareStatement(query);
            pst.setString(1, userName);
            pst.setString(2, password);

            ResultSet result = pst.executeQuery();

            while(result.next())
            {
                userID = result.getInt("ID");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userID;
    }

    public void saveUser() throws SQLException
    {
        int logRounds = 12;
        PwdHash hash = new PwdHash(logRounds);
        DBConnection conn = new DBConnection();
        Connection c = conn.connection();

        String pass = hash.hash(getPassword());
        String query = "INSERT INTO user (userName, password) VALUES (?, ?)";
        PreparedStatement pt = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pt.setString(1, getUserName());
        pt.setString(2, pass);
        pt.executeUpdate();
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
