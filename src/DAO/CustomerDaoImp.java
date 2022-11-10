package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for adding, getting, updating and deleting customer information from customers table in database */
public class CustomerDaoImp {

    /** Used for receiving all customers in customers table in database
     * @return all customers listed in customers table in database
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ResultSet customerList = Database.selectStatement("SELECT * FROM customers");
        Customer selectedCustomer;
        while (customerList.next()){
            int id = customerList.getInt("Customer_ID");
            String name = customerList.getString("Customer_Name");
            String address = customerList.getString("Address");
            String postalCode = customerList.getString("Postal_Code");
            String phone = customerList.getString("Phone");
            int divisionId = customerList.getInt("Division_ID");
            selectedCustomer = new Customer(id, name, address, postalCode, phone, divisionId);
            allCustomers.add(selectedCustomer);
        }
        return allCustomers;
    }

    /** Used for receiving single customer object in customers table in database
     * @param searchCustomerID customer ID to search on
     * @return single customer object from customer ID
     * @throws SQLException
     * @throws Exception
     */
    public static Customer getCustomer(int searchCustomerID) throws SQLException, Exception{
        ResultSet customerList = Database.selectStatement("SELECT * FROM customers WHERE " +
                "Customer_ID  = '" + searchCustomerID+ "'");
        Customer selectedCustomer;
        while(customerList.next()){
            int id = customerList.getInt("Customer_ID");
            String name = customerList.getString("Customer_Name");
            String address = customerList.getString("Address");
            String postalCode = customerList.getString("Postal_Code");
            String phone = customerList.getString("Phone");
            int divisionId = customerList.getInt("Division_ID");
            selectedCustomer = new Customer(id, name, address, postalCode, phone, divisionId);
            return selectedCustomer;
        }
        return null;
    }

    /** Used for adding customer record in customers table in database
     * @param selectedCustomer pass customer object to create new customer
     * @param currentUser current user who updated record for database auditing
     * @return amount of rows updated in database indicating add was successful
     * @throws SQLException
     */
    public static int insert(Customer selectedCustomer, User currentUser) throws SQLException {
        String statement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,NOW(),?,NOW()," +
                "?,?)";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setString(1, selectedCustomer.getName());
        preparedStatement.setString(2, selectedCustomer.getAddress());
        preparedStatement.setString(3, selectedCustomer.getPostalCode());
        preparedStatement.setString(4, selectedCustomer.getPhone());
        preparedStatement.setString(5, currentUser.getUserName());
        preparedStatement.setString(6, currentUser.getUserName());
        preparedStatement.setInt(7, selectedCustomer.getDivisionId());
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

    /** Used for updating customer record in customers table in database
     * @param selectedCustomer pass customer object to update customer
     * @param currentUser current user who updated record for database auditing
     * @return amount of rows updated in database indicating update was successful
     * @throws SQLException
     */
    public static int update(Customer selectedCustomer, User currentUser) throws SQLException {
        String statement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone= ?, " +
                "Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setString(1, selectedCustomer.getName());
        preparedStatement.setString(2, selectedCustomer.getAddress());
        preparedStatement.setString(3, selectedCustomer.getPostalCode());
        preparedStatement.setString(4, selectedCustomer.getPhone());
        preparedStatement.setString(5, currentUser.getUserName());
        preparedStatement.setInt(6, selectedCustomer.getDivisionId());
        preparedStatement.setInt(7, selectedCustomer.getId());
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

    /** Used for deleting customer from customers table in database
     * @param id of customer record
     * @return amount of rows effected for delete indicating if delete was successful
     * @throws SQLException
     */
    public static int delete(int id) throws SQLException {

        String statement = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setInt(1, id);
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }




}
