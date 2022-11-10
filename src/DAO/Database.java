package DAO;

import java.sql.*;

/** Class for handling database connection */
public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    public static Connection connection;



    /** Starts connection to database */
    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        }
        catch (SQLException | ClassNotFoundException se){
            System.out.println("Error:" + se.getMessage());
        }
    }

    /** Closes connection to database */
    public static void closeConnection()  {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Prepares simple SQL SELECT statements to run against database.
     * @param query SELECT SQL query to run again database
     * @return results of select statement
     */
    public static ResultSet selectStatement(String query) {
        Statement statement;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            if (query.toUpperCase().startsWith("SELECT"))
                resultSet = statement.executeQuery(query);
            return resultSet;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

}
