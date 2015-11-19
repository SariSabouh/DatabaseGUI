package controller;

import java.sql.*;

public class CustomerDao {
	
	Connection conn;
	String username, password;
	
	public CustomerDao(String username, String password){
		this.username = username;
        this.password = password;
	}
	
    public static void main(String[] args) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Couldn't load the driver class.");
            System.exit(1);
        }
    }
    
    public void logIn(){
    	try {
            String dbName = "database";
            String jdbcUrl = "jdbc:hsqldb:file:" + dbName;
            conn = DriverManager.getConnection(jdbcUrl, username,
                    password);
        } catch (SQLException ex) {
            System.err.println("Couldn't connect to database");
            ex.printStackTrace();
        }
    }
    
    public void selectAll(){
        String query = "SELECT * FROM CUSTOMER;";
        
        Statement stat = null;
        ResultSet results = null;
        logIn();
        try {
            stat = conn.createStatement();
            
            results = stat.executeQuery(query);
            while (results.next()) {
                int customerID = results.getInt("CustomerID");
                String firstName = results.getString("FirstName");
                String lastName = results.getString("LastName");
                
                System.out.println("CustomerID: " + customerID
                        + "  Name: " + firstName + " " + lastName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                results.close();
                stat.close();
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
    }
    
    public void selectByFullName(Customer customer){
        String query = "SELECT * FROM CUSTOMER where firstName = ? and lastName = ?;";
        PreparedStatement stat = null;
        ResultSet results = null;
        logIn();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setString(1, customer.getFirstName());
            stat.setString(2, customer.getLastName());
            results = stat.executeQuery(query);
            while (results.next()) {
                int customerID = results.getInt("CustomerID");
                System.out.println("CustomerID: " + customerID
                        + "  Name: " + customer.getFirstName() + " " + customer.getLastName());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                results.close();
                stat.close();
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
    }
    
    public void insert(Customer customer){
        String query = "insert into customer(CUSTOMERID, fisrtName, lastName, street, city)values(?, ?, ?, ?, ?)";
        PreparedStatement stat = null;
        logIn();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setInt(1, customer.getId());
            stat.setString(2, customer.getFirstName());
            stat.setString(3, customer.getLastName());
            stat.setString(4, customer.getAddress());
            stat.setString(5, customer.getCity());
            int results = stat.executeUpdate();
            System.out.println(results + " were updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stat.close();
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
    }
    
    public void delete(Customer customer){
        String query = "delete from customer where customerid = ?";
        PreparedStatement stat = null;
        logIn();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setInt(1, customer.getId());
            int results = stat.executeUpdate();
            System.out.println(results + " were updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stat.close();
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
    }    
}
