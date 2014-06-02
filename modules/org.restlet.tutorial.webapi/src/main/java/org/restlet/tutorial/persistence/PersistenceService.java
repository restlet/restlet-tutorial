package org.restlet.tutorial.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.restlet.Context;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.persistence.entity.Contact;

public abstract class PersistenceService<T> {

    /*
     * Initialize database settings.
     * 
     * Example made with mySQL but you are free to use your favorite DBMS.
     */

    public static final String DRIVER = com.mysql.jdbc.Driver.class.getName();

    public static final String URL = "jdbc:mysql://localhost:3306/restletWebApi";

    public static final String USER = "test";

    public static final String PASSWORD = "test";

    public static void initialize() {
        /*
         * Ensure DB configuration is correct
         */
        try {
            Class.forName(com.mysql.jdbc.Driver.class.getName());
        } catch (ClassNotFoundException e) {
            Context.getCurrentLogger().severe(
                    "JDBC Driver not found : application aborted");
            System.exit(1);
        }
    }

    /*
     * Define CRUD functions for childen classes.
     */

    public abstract List<T> findAll() throws SQLException;

    public abstract List<T> findBy(String fieldName, String fieldValue)
            throws SQLException;

    public abstract T add(T toAdd) throws SQLException;

    public abstract Boolean delete(String id) throws SQLException;

    public abstract T findById(String id) throws SQLException;

    public abstract T update(T toUpdate, String id) throws SQLException;

    /*
     * Create and release connection
     */

    protected Connection createConnection() throws SQLException {
        Context.getCurrentLogger().finer(
                "Method createConnection() of PersistenceService called.");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    protected void releaseConnection(Connection connection) throws SQLException {
        Context.getCurrentLogger().finer(
                "Method releaseConnection() of PersistenceService called.");
        connection.close();
    }

    /*
     * Generate a random ID.
     */

    protected String generateStringId() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    /*
     * Factories
     */

    public static PersistenceService<Contact> getContactPersistence() {
        Context.getCurrentLogger().finer(
                "Method getContactPersistence() of PersistenceService called.");
        return ContactPersistence.getContactPersistence();
    }

    public static PersistenceService<Company> getCompanyPersistence() {
        Context.getCurrentLogger().finer(
                "Method getCompanyPersistence() of PersistenceService called.");
        return CompanyPersistence.getCompanyPersistence();
    }

}
