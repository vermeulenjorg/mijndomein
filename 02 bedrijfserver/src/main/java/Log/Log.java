package Log;

import Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Log {

    private String log;

    public Log(String log) throws SQLException {
        this.log = log;
        writeLog();

    }

    public void setLog (String log){
        this.log = log;
    }

    public String getLog(){
        return log;
    }

    public void writeLog() throws SQLException {

        DBConnection c = new DBConnection();
        Connection conn = c.connection();
        String insertQuery = "INSERT INTO devicelogging (measure) VALUES ((?))";
        PreparedStatement p = conn.prepareStatement(insertQuery);
        p.setString(1, log);
        p.executeUpdate();

    }
}


