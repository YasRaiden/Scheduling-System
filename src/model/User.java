package model;

/** Class utilized for managing users.*/
public class User {
    private int id;
    private String userName;

    /**
     * @param id user id from users table in database
     * @param userName username from users table in database
     */
    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    /**
     * @return id for user
     */
    public int getUserID() {
        return id;
    }

    /**
     * @return username for user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return overload text return when string is requested for user object
     */
    public String toString(){
        return userName;
    }
}
