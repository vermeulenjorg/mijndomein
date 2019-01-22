package database;

import java.sql.*;

public class DBConnection
{
    /**
     * Constructor.
     */
    public DBConnection()
    {

    }

    /**
     * Makes a connection with the database.
     * @return Returns a database connection.
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