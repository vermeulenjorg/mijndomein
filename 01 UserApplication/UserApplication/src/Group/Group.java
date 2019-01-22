package Group;

import User.UserControls;
import java.sql.*;

public class Group
{
    public String name;
    public int groupID;
    String activationState;

    /**
     * Constructor.
     * Sets the groupname in the Constructor.
     * @param groupName Name for new group.
     */
    public Group(String groupName)
    {
        this.name = groupName;
    }

    /**
     * Adds a new group to the database.
     * Gets the connection from UserControls.
     * Adds group to database and adds current user as owner.
     */
    public void saveGroup() throws SQLException
    {
        Connection c = UserControls.connection;

        int userID = UserControls.userID;
        int groupID = 0;

        String query = "INSERT INTO groep (groupName) VALUES (?)";
        PreparedStatement pstate = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstate.setString(1, this.name);
        pstate.executeUpdate();

        ResultSet result = pstate.getGeneratedKeys();
        while (result.next())
        {
            groupID = result.getInt(1);
        }

        String userQuery = "INSERT INTO usergroups (userID, groupID) VALUES (?, ?)";
        PreparedStatement pst = c.prepareStatement(userQuery);
        pst.setInt(1, userID);
        pst.setInt(2, groupID);
        pst.executeUpdate();
    }

    public int getGroupID()
    {
        return groupID;
    }

    public void setGroupID(int groupID)
    {
        this.groupID = groupID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getActivationState()
    {
        return activationState;
    }

    public void setActivationState(String activationState)
    {
        this.activationState = activationState;
    }
}
