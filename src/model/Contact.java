package model;

/** Class utilized for managing contacts.*/
public class Contact {
    private int id;
    private String name;
    private String email;

    /**
     * @param id contact id from contacts table in database
     * @param name contact name from contacts table in database
     * @param email contact email from contacts table in database
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * @return id for contact
     */
    public int getId() {
        return id;
    }

    /**
     * @return name for contact
     */
    public String getName() {
        return name;
    }

    /**
     * @return email for contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return overload text return when string is requested for contact object
     */
    public String toString(){
        return name;
    }
}
