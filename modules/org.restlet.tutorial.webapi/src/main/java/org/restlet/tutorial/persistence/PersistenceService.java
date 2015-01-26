/**
 * Copyright 2005-2015 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.tutorial.persistence;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.h2.tools.Console;
import org.h2.tools.RunScript;
import org.restlet.Context;

/**
 * Persistence layer for a single class of entities. For the matter of example,
 * it leverages the H2 database but you are free to use your favorite DBMS.
 * 
 * @author Guillaume Blondeau
 * @author Manuel Boillod
 * 
 * @param <T>
 *            The class of objects to persist in database.
 */
public abstract class PersistenceService<T> {

    private static final String PASSWORD = "";

    private static final String URL = "jdbc:h2:mem:restletWebApi";

    private static final String USER = "sa";

    /**
     * Returns a persistence layer for managing Companies.
     * 
     * @return A persistence layer for managing Companies.
     */
    public static CompanyPersistence getCompanyPersistence() {
        Context.getCurrentLogger().finer(
                "Get the persistence layer for Companies.");
        return CompanyPersistence.getCompanyPersistence();
    }

    /**
     * Returns a persistence layer for managing Contacts.
     * 
     * @return A persistence layer for managing Contacts.
     */
    public static ContactPersistence getContactPersistence() {
        Context.getCurrentLogger().finer(
                "Get the persistence layer for Contacts.");
        return ContactPersistence.getContactPersistence();
    }

    public static void initialize() {
        // Ensure DB configuration is correct
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            // Initialize database settings.
            Connection conn = DriverManager.getConnection(URL
                    + ";DB_CLOSE_DELAY=-1", USER, PASSWORD);

            // execute initialization script thanks to classloader
            RunScript.execute(conn, new InputStreamReader(ClassLoader
                    .getSystemClassLoader()
                    .getResourceAsStream("db-schema.sql")));

            // start the H2 Console
            Console.main("-web");

            Context.getCurrentLogger().info(
                    "H2 Console available at http://localhost:8082. Use URL JDBC string: "
                            + URL + ";IFEXISTS=TRUE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a random ID.
     * 
     * @return A random ID.
     */
    protected String generateStringId() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    protected Connection getConnection() throws SQLException {
        Context.getCurrentLogger().finer("Get a fresh connection to database");
        Connection result = DriverManager.getConnection(URL, USER, PASSWORD);
        Context.getCurrentLogger().finer("Got a fresh connection to database");
        return result;
    }

    protected void releaseConnection(Connection connection) throws SQLException {
        Context.getCurrentLogger().finer(
                "Release connection: " + Objects.toString(connection));
        if (connection != null) {
            connection.close();
            Context.getCurrentLogger().finer(
                    "Connection released: " + Objects.toString(connection));
        }

    }

    /**
     * Adds a new entity to the database.
     * 
     * @param toAdd
     *            The entity to add.
     * @return The newly added entity, especially with its technical identifier,
     *         in case it is computed.
     * @throws SQLException
     */
    public abstract T add(T toAdd) throws SQLException;

    /**
     * Removes an entity from the database.
     * 
     * @param id
     *            The identifier of the entity to remove.
     * @return True if the entity has been removed, false if the entity was
     *         already removed.
     * @throws SQLException
     */
    public abstract Boolean delete(String id) throws SQLException;

    public abstract List<T> findAll() throws SQLException;

    public abstract T findById(String id) throws SQLException;

    /**
     * Updates an existing entity.
     * 
     * @param toUpdate
     *            The new state of the entity.
     * @param id
     *            the identifier of the ientity to update.
     * @return The updated entity.
     * @throws SQLException
     */
    public abstract T update(T toUpdate, String id) throws SQLException;

}
