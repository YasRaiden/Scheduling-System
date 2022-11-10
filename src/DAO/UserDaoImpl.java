package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for getting user information from user table in database and managing authentication */
public class UserDaoImpl {
    private static User loginUser;

    /** Used for receiving all users in users table in database
     * @return all users listed in users table in database
     * @throws SQLException
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        ResultSet userList = Database.selectStatement("SELECT User_ID, User_Name FROM users");
        User selectedUser;
        while (userList.next()){
            int id = userList.getInt("User_ID");
            String userName = userList.getString("User_Name");

            selectedUser = new User(id, userName);
            allUsers.add(selectedUser);
        }
        return allUsers;
    }

    /** Used for receiving single user object in users table in database
     * @param searchUserID user ID to search on
     * @return single user object from customer ID
     * @throws SQLException
     * @throws Exception
     */
    public static User getUser(int searchUserID) throws SQLException, Exception{
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        ResultSet userList = Database.selectStatement("SELECT User_ID, User_Name FROM users WHERE User_ID = " +
                searchUserID);
        User selectedUser;
        while (userList.next()){
            int id = userList.getInt("User_ID");
            String userName = userList.getString("User_Name");
            selectedUser = new User(id, userName);
            return selectedUser;
        }
        return null;
    }

    /** Authenticates user to application using information in database
     * @param username username of authenticating user
     * @param password password of authenticating user
     * @return user object to hold authenticated user
     */
    public static User authUser(String username, String password) {
        ResultSet authentication;
        try {
            authentication = Database.selectStatement("SELECT User_ID, User_Name FROM users " +
                    "WHERE User_Name = '" + username + "' AND Password = '" + password + "'");
            if(authentication.next()) {
                loginUser = new User(authentication.getInt("User_ID"), authentication.getString("User_Name"));
                return loginUser;
            }
            else
                return null;
        }
        catch (Exception e){
            System.out.println("ERROR ERROR ERROR");
            return null;
        }
    }

    /**
     * @return currently authenticated user
     */
    public static User getLoginUser(){
        return loginUser;
    }

    /** Used during logout workflow clearing logged in user object */
    public static void clearLoginUser(){
        loginUser = null;
    }

}
