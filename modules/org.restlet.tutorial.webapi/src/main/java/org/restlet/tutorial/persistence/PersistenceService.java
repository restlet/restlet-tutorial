package org.restlet.tutorial.persistence;

import org.h2.tools.Console;
import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.restlet.Context;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.persistence.entity.Contact;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class PersistenceService<T> {

    /*
     * Initialize database settings.
     * 
     * Example made with H2 database but you are free to use your favorite DBMS.
     */

    public static final String URL = "jdbc:h2:mem:restletWebApi";

    public static final String USER = "sa";

    public static final String PASSWORD = "";

    public static void initialize() {
        // Ensure DB configuration is correct
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Connection conn = DriverManager.getConnection(URL + ";DB_CLOSE_DELAY=-1", USER, PASSWORD);

            // Execute initialization script
            RunScript.execute(conn, new InputStreamReader(
                    ClassLoader.getSystemClassLoader().getResourceAsStream("db-schema.sql"))
            );

            // now start the H2 Console
            Console.main("-web");

            Context.getCurrentLogger().info(
                    "H2 Console available at http://localhost:8082. Use URL JDBC string: " + URL + ";IFEXISTS=TRUE");

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public static ContactPersistence getContactPersistence() {
        Context.getCurrentLogger().finer(
                "Method getContactPersistence() of PersistenceService called.");
        return ContactPersistence.getContactPersistence();
    }

    public static CompanyPersistence getCompanyPersistence() {
        Context.getCurrentLogger().finer(
                "Method getCompanyPersistence() of PersistenceService called.");
        return CompanyPersistence.getCompanyPersistence();
    }

}
