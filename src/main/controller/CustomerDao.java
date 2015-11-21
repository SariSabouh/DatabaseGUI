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
    
    public boolean logIn(){
    	try {
            String dbName = "database";
            String jdbcUrl = "jdbc:hsqldb:file:" + dbName;
            conn = DriverManager.getConnection(jdbcUrl, username,
                    password);
        } catch (SQLException ex) {
            System.err.println("Couldn't connect to database");
            return false;
        }
    	return true;
    }
    
    public void disconnect(){
    	try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Couldn't close cleanly.");
            ex.printStackTrace();
        }
    }
    
    public int getCount(){
        String query = "SELECT count(*) as cnt FROM CUSTOMER;";
        int count = -1;
        Statement stat = null;
        ResultSet results = null;
        try {
            stat = conn.createStatement();
            
            results = stat.executeQuery(query);
            while (results.next()) {
                count = results.getInt("cnt");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                results.close();
                stat.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
        return count;
    }
    
    public Customer selectById(int id){
        String query = "SELECT * FROM CUSTOMER where customerid = ?;";
        PreparedStatement stat = null;
        ResultSet results = null;
        Customer customer = new Customer();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setInt(1, id);
            results = stat.executeQuery();
            while (results.next()) {
                customer.setAddress(results.getString("street"));
                customer.setCity(results.getString("city"));
                customer.setFirstName(results.getString("firstname"));
                customer.setLastName(results.getString("lastname"));
                customer.setId(results.getInt("customerid"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                results.close();
                stat.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
        return customer;
    }
    
    public boolean insert(Customer customer){
        String query = "insert into customer(CUSTOMERID, firstName, lastName, street, city)values(?, ?, ?, ?, ?)";
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
            System.out.println(results + " were inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                stat.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    public boolean update(Customer customer){
        String query = "insert customer set fisrtName = ?, lastName = ?, street = ?, city = ? where customerid = ?";
        PreparedStatement stat = null;
        logIn();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setInt(5, customer.getId());
            stat.setString(1, customer.getFirstName());
            stat.setString(2, customer.getLastName());
            stat.setString(3, customer.getAddress());
            stat.setString(4, customer.getCity());
            int results = stat.executeUpdate();
            System.out.println(results + " were updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                stat.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    public void delete(int id){
        String query = "delete from customer where customerid = ?";
        PreparedStatement stat = null;
        logIn();
        try {
            stat = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setInt(1, id);
            int results = stat.executeUpdate();
            System.out.println(results + " were updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stat.close();
            } catch (SQLException ex) {
                System.err.println("Couldn't close cleanly.");
                ex.printStackTrace();
            }
        }
    }
    
    
}
