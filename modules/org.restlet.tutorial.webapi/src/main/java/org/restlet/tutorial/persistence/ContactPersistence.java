/*
 * Copyright 2005-2017 Restlet
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.resource.ResourceException;
import org.restlet.tutorial.persistence.entity.Contact;

/**
 * Handles operations on the table Company.
 * 
 * @author Guillaume Blondeau
 */
public class ContactPersistence extends PersistenceService<Contact> {

    // Singleton pattern
    private static ContactPersistence contactPersistence = new ContactPersistence();

    public static synchronized ContactPersistence getContactPersistence() {
        return contactPersistence;
    }

    private ContactPersistence() {
    };

    @Override
    public Contact add(Contact toAdd) throws SQLException, ResourceException {

        Context.getCurrentLogger().finer(
                "Method add() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into Contact "
                            + "(id, email, age, name, firstname, company_id) "
                            + "values (?,?,?,?,?,?);");

            // Auto generated id
            toAdd.setId(generateStringId());
            preparedStatement.setString(1, toAdd.getId());

            preparedStatement.setString(2, toAdd.getEmail());
            preparedStatement.setInt(3, toAdd.getAge());
            preparedStatement.setString(4, toAdd.getName());
            preparedStatement.setString(5, toAdd.getFirstName());
            preparedStatement.setString(6, toAdd.getCompanyId());

            preparedStatement.executeUpdate();

            return toAdd;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findById() of ContactPersistence finished.");
        }
    }

    @Override
    public Boolean delete(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method delete() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from Contact where id=?");
            preparedStatement.setString(1, id);

            Integer result = preparedStatement.executeUpdate();
            // return false if no row has been updated.
            return result != 0;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method delete() of ContactPersistence called.");
        }
    }

    @Override
    public List<Contact> findAll() throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findAll() of ContactPersistence called.");

        List<Contact> contacts = new ArrayList<Contact>();
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from Contact");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contacts.add(contact);
                contact.setCompanyId(rs.getString("company_id"));
                contact.setEmail(rs.getString("email"));
                contact.setFirstName(rs.getString("firstname"));
                contact.setId(rs.getString("id"));
                contact.setName(rs.getString("name"));
                contact.setAge(rs.getInt("age"));
            }
            return contacts;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findAll() of ContactPersistence finished.");
        }
    }

    public List<Contact> findByEmail(String fieldValue) throws SQLException {
        return findBy("email", fieldValue);
    }

    public List<Contact> findByCompany(String fieldValue) throws SQLException {
        return findBy("company_id", fieldValue);
    }

    /**
     * Let this method as private in order to prevent SQL injection, since the
     * SQL request is obtained by concatenation.
     * 
     * @param fieldName
     * @param fieldValue
     * @return
     * @throws SQLException
     */
    private List<Contact> findBy(String fieldName, String fieldValue)
            throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findBy() of ContactPersistence called.");

        List<Contact> contacts = new ArrayList<Contact>();
        Connection connection = null;
        try {
            connection = getConnection();
            // take care, may
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from Contact where "
                            + fieldName + "=?");
            preparedStatement.setString(1, fieldValue);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contacts.add(contact);
                contact.setCompanyId(rs.getString("company_id"));
                contact.setEmail(rs.getString("email"));
                contact.setFirstName(rs.getString("firstname"));
                contact.setId(rs.getString("id"));
                contact.setName(rs.getString("name"));
                contact.setAge(rs.getInt("age"));
            }
            return contacts;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findAll() of ContactPersistence finished.");
        }
    }

    @Override
    public Contact findById(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findById() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Contact where id=?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Contact contact = new Contact();
                contact.setCompanyId(rs.getString("company_id"));
                contact.setEmail(rs.getString("email"));
                contact.setFirstName(rs.getString("firstname"));
                contact.setId(rs.getString("id"));
                contact.setName(rs.getString("name"));
                contact.setAge(rs.getInt("age"));
                return contact;
            }

            return null;
        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findById() of ContactPersistence finished.");
        }
    }

    @Override
    public Contact update(Contact toUpdate, String id) throws SQLException,
            ResourceException {

        Context.getCurrentLogger().finer(
                "Method update() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Update Contact SET "
                            + "name=?, email=?, age=?, firstname=?, company_id=? "
                            + "WHERE id=?;");
            preparedStatement.setString(1, toUpdate.getName());
            preparedStatement.setString(2, toUpdate.getEmail());
            preparedStatement.setInt(3, toUpdate.getAge());
            preparedStatement.setString(4, toUpdate.getFirstName());
            preparedStatement.setString(5, toUpdate.getCompanyId());
            preparedStatement.setString(6, id);

            Integer result = preparedStatement.executeUpdate();
            if (result != 0) {
                return toUpdate;
            }
            return null;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method update() of ContactPersistence finished.");
        }
    }

}
