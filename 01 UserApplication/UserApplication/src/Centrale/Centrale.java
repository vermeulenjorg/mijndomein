package Centrale;

import SystemControls.CentraleControls;
import User.UserControls;

import java.io.IOException;
import java.sql.*;

public class Centrale {

    CentraleControls controls = new CentraleControls();

    public int centraleID;
    public String centraleNaam;
    public String centraleUrl;
    public String centraleMac;

    public Centrale(String centraleNaam, String centraleUrl, String centraleMac)
    {
        this.centraleNaam = centraleNaam;
        this.centraleUrl = centraleUrl;
        this.centraleMac = centraleMac;
    }

    public void saveCentrale() throws SQLException, IOException
    {
        Connection c = UserControls.connection;
        int centraleID = 0;
        int userID = UserControls.userID;

        String saveQuery = "INSERT INTO centrale (centraleNaam, centraleUrl, centraleMac) VALUES (?, ?, ?))";
        PreparedStatement p = c.prepareStatement(saveQuery, Statement.RETURN_GENERATED_KEYS);
        p.setString(1, getCentraleNaam());
        p.setString(2, getCentraleUrl());
        p.setString(1, getCentraleMac());

        ResultSet result = p.getGeneratedKeys();
        if(result.next())
        {
            centraleID = result.getInt("centraleID");
        }

        String centraleQuery = "INSERT INTO usercentrale (userID, centraleID) VALUES (?, ?)";
        PreparedStatement pt = c.prepareStatement(centraleQuery);
        pt.setInt(1, userID);
        pt.setInt(2, centraleID);
        pt.executeUpdate();
        controls.updateCentrale(centraleID);
    }

    /* GETTERS AND SETTERS */
    public int getCentraleID()
    {
        return centraleID;
    }

    public void setCentraleID(int centraleID)
    {
        this.centraleID = centraleID;
    }

    public String getCentraleNaam()
    {
        return centraleNaam;
    }

    public void setCentraleNaam(String centraleNaam)
    {
        this.centraleNaam = centraleNaam;
    }

    public String getCentraleUrl() {
        return centraleUrl;
    }

    public void setCentraleUrl(String centraleUrl)
    {
        this.centraleUrl = centraleUrl;
    }

    public String getCentraleMac()
    {
        return centraleMac;
    }

    public void setCentraleMac(String centraleMac)
    {
        this.centraleMac = centraleMac;
    }
}
