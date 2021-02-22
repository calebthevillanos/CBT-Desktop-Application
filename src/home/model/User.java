package home.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User
{

    //DECLARED USER CLASS VARIABLE
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String gender;
    private String dateCreated;

    //NO-ARGUMENT CONSTRUCTOR
    public User()
    {
    }

    //CONSTRUCTOR OVERLOADING
    public User(String firstName, String lastName, String userName)
    {
        this(0, firstName, lastName, userName, null, null);
    }

    //CONSTRUCTOR OVERLOADING
    public User(String firstName, String lastName, String userName, String gender)
    {
        this(0, firstName, lastName, userName, gender, null);
    }

    //CONSTRUCTOR OVERLOADING
    public User(int id, String firstName, String lastName, String userName, String gender)
    {
        this(id, firstName, lastName, userName, gender, null);
    }

    //FULL ARGUMENT CONSTRUCTOR 
    public User(int id, String firstName, String lastName, String userName, String gender, String dateCreated)
    {
        this.id = id;

        if (validateName(firstName))
        {
            this.firstName = firstName;
        }

        if (validateName(lastName))
        {
            this.lastName = lastName;
        }

        if (validateUserName(userName))
        {
            this.userName = userName;
        }

        this.gender = gender;

        this.dateCreated = dateCreated;
    }

    //SET ID METHOD
    public void setId(int id)
    {
        this.id = id;
    }

    //SET FIRSTNAME METHOD  
    public void setFirstName(String firstName)
    {
        //VALIDATE NAME METHOD CALLED WHICH VALIDATES STRING FIRSTNAME
        if (validateName(firstName))
        {
            this.firstName = firstName;
        }
    }

    //SET LASTNAME METHOD  
    public void setLastName(String lastName)
    {
        //VALIDATE NAME METHOD CALLED WHICH VALIDATES STRING LASTNAME
        if (validateName(lastName))
        {
            this.lastName = lastName;
        }
    }

    //SET USERNAME METHOD  
    public void setUserName(String userName)
    {
        //VALIDATE USERNAME METHOD CALLED WHICH VALIDATES STRING USERNAME
        if (validateUserName(userName))
        {
            this.userName = userName;
        }
    }

    //SET GENDER METHOD
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    //SET DATECREATED METHOD
    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    //GET ID METHOD
    public int getId()
    {
        return id;
    }

    //GET FIRSTNAME METHOD
    public String getFirstName()
    {
        return firstName;
    }

    //GET LASTNAME METHOD
    public String getLastName()
    {
        return lastName;
    }

    //GET USERNAME METHOD
    public String getUserName()
    {
        return userName;
    }

    //GET GENDER METHOD
    public String getGender()
    {
        return gender;
    }

    //GET DATECREATED
    public String getDateCreated()
    {
        return dateCreated;
    }

    //METHOD THAT VALIDATES STRING VARIABLE, NAME(EITHER FIRSTNAME OR LASTNAME)
    private static boolean validateName(String name)
    {
        if (name != null && !name.matches("^[A-Z][a-z\\.\\-]+"))
        {
            throw new IllegalArgumentException("Name is invalid");
        }
        return true;
    }

    //METHOD THAT VALIDATES STRING VARIABLE, USERNAME)
    private static boolean validateUserName(String name)
    {
        if (name != null && !name.matches("^[A-Z][a-z\\s]+"))
        {
            throw new IllegalArgumentException("Name is invalid");
        }
        return true;
    }

    //DATABASE METHOD THAT GETS ALL USERACCOUNTS FROM THE DB
    public static ArrayList<User> getUsers()
    {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM UserAccounts"))
        {
            while (resultSet.next())
            {
                users.add(new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                ));
            }
        } catch (SQLException e)
        {
            System.err.println("Could not retrieve users: " + e.getMessage());
        }
        return users;
    }

    //METHOD THAT GET A PARTICULAR USER IF THE SPECIFIED PARAMETERS ARE TRUE 
    private boolean getUser() throws SQLException
    {
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(
                        "SELECT * FROM UserAccounts WHERE FirstName = ? AND LastName = ? AND UserName = ?"))
        {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
            }
        }
        return false;
    }

    //METHOD THAT ADDS NEW USER TO DATABASE TABLE USERACCOUNT
    public int addUser()
    {
        int rowAdded = 0;

        boolean userFound;

        try
        {
            userFound = getUser();
        } catch (SQLException e)
        {
            System.err.println("Could not verify User: "
                    + e.getMessage());
            return 0;
        }

        if (userFound)
        {
            return UserStatus.USER_EXISTS;
        }

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(
                        "INSERT INTO UserAccounts(FirstName, LastName, UserName, Gender) VALUES (?,?,?,?)"))
        {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);
            statement.setString(4, gender);

            rowAdded = statement.executeUpdate();

        } catch (SQLException e)
        {
            System.err.println("Could not add user: "
                    + e.getMessage());
        }
        return rowAdded;
    }

    //METHODS RETURNS A USER OBJECT AFTER SPECIDIED ID HAS BEEN SET
    public static User getUser(int id)
    {
        User user = null;

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(
                        "SELECT * FROM UserAccounts WHERE UserId = ?"))
        {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    user = new User(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)
                    );
                }
            }
        } catch (SQLException e)
        {
            System.err.println("Could not retrieve user details: "
                    + e.getMessage());
        }
        return user;
    }

    public static int deleteUser(int id)
    {
        if (getUser(id) == null)
        {
            return UserStatus.USER_NOT_FOUND;
        }

        int rowDeleted = 0;

        try (Connection con = DatabaseConnection.getConnection();
                Statement statement = con.createStatement())
        {
            rowDeleted = statement.executeUpdate(
                    "DELETE FROM UserAccounts WHERE UserId = " + id);
        } catch (SQLException e)
        {
            System.err.println("Could not delete user: "
                    + e.getMessage());
        }
        return rowDeleted;
    }

    @Override
    public String toString()
    {
        return String.format("Student %d: %s %s %s %s %n%s",
                id, firstName, userName, lastName, gender, dateCreated);
    }

    public static class UserStatus
    {

        public static final int USER_EXISTS = -1;
        public static final int DATABASE_ERROR = 0;
        public static final int USER_NOT_FOUND = -2;
    }
}
