package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection
{
    /**
     * Constructor.
     */
    public DBConnection()
    {

    }

    /**
     * Makes a connection with the Database.
     * @return Returns a Database connection.
     */
    public Connection connection()
    {
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mijndomein?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return conn;
    }

}