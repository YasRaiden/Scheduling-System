package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for getting contact information from contacts table in database */
public class ContactDaoImpl {

    /** Used for receiving all contacts in contacts table database
     * @return all contacts listed in contacts table in database
     * @throws SQLException
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        ResultSet contactList = Database.selectStatement("SELECT * FROM contacts");
        Contact selectedContact;
        while (contactList.next()){
            int id = contactList.getInt("Contact_ID");
            String name = contactList.getString("Contact_Name");
            String email = contactList.getString("Email");
            selectedContact = new Contact(id, name, email);
            allContacts.add(selectedContact);
        }
        return allContacts;
    }

    /** Used for receiving single contact object in contacts table in database
     * @param searchContactID contact ID to search on
     * @return single contact object from contact ID
     * @throws SQLException
     * @throws Exception
     */
    public static Contact getContact(int searchContactID) throws SQLException, Exception{
        ResultSet contactList = Database.selectStatement("SELECT * FROM contacts WHERE " +
                "Contact_ID  = '" + searchContactID+ "'");
        Contact selectedContact;
        while(contactList.next()){
            int id = contactList.getInt("Contact_ID");
            String name = contactList.getString("Contact_Name");
            String email = contactList.getString("Email");
            selectedContact = new Contact(id, name, email);
            return selectedContact;
        }
        return null;
    }
}
