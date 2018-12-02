package database;
import javax.xml.transform.Result;
import java.sql.*;

public class Database {
    static Connection conn = getConnection();

    public static Connection getConnection(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mijn_domein?user=root&password=");
            System.out.println("Connected");
            System.out.println(conn.getMetaData());

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("SQLException: " + e1.getMessage());
            System.out.println("SQLState: " + e1.getSQLState());
            System.out.println("VendorError: " + e1.getErrorCode());
        }
        return conn;
    }

    public static ResultSet ExecuteStatement(String statement) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(statement);

            // or alternatively, if you don't know ahead of time that
            // the query will be a SELECT...

//            if (stmt.execute("SELECT foo FROM bar")) {
//                rs = stmt.getResultSet();
//            }

            // Now do something with the ResultSet ....
//            ResultSet resultSet = statement.executeQuery("SELECT * from foo");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }


        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
        return rs;
    }

    public static void insertStatement(String table, String topic, String value){
        String state = "INSERT INTO " + table + "(topic, message) VALUES ('" + topic + "','" + value + "')";
//        System.out.println(state);
        try (Statement statement = conn.createStatement()) {

            statement.executeUpdate(state);

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    public static void main(String[] args) {
        System.out.println("Start");
       Connection conny = getConnection();
       ResultSet rs = ExecuteStatement("SELECT * FROM logging");
        Database.insertStatement("logging","hoi","hallo");
        System.out.println(rs);



    }




}
