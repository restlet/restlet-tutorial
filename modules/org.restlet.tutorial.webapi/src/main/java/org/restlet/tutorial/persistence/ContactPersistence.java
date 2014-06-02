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
 * Makes operations on the table Contact. (CRUD)
 * 
 * @author Guillaume Blondeau
 * 
 */
public class ContactPersistence extends PersistenceService<Contact> {

    /*
     * Singleton pattern
     */

    private static ContactPersistence contactPersistence = null;

    public static synchronized ContactPersistence getContactPersistence() {
        if (contactPersistence == null) {
            contactPersistence = new ContactPersistence();
        }
        return contactPersistence;
    }

    private ContactPersistence() {
    };

    @Override
    public List<Contact> findAll() throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findAll() of ContactPersistence called.");

        List<Contact> contacts = new ArrayList<Contact>();
        Connection connection = null;
        try{
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Contact");
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
    public List<Contact> findBy(String fieldName, String fieldValue)
            throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findBy() of ContactPersistence called.");

        List<Contact> contacts = new ArrayList<Contact>();
        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Contact where "
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
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Contact where id=?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Contact contact = new Contact();
                contact.setCompanyId(rs.getString("company_id"));
                contact.setEmail(rs.getString("email"));
                contact.setFirstName(rs.getString("firstname"));
                contact.setId(rs.getString("id"));
                contact.setName(rs.getString("name"));
                contact.setAge(rs.getInt("age"));
                return contact;
            } else {
                return null;
            }
        } finally {

            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findById() of ContactPersistence finished.");
        }
    }

    @Override
    public Contact add(Contact toAdd) throws SQLException,
            ResourceException {

        Context.getCurrentLogger().finer(
                "Method add() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Insert into Contact "
                            + "(id, email, age, name, firstname, company_id) values ("
                            + "?,?,?,?,?,?);");

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
    public Contact update(Contact toUpdate, String id) throws SQLException,
            ResourceException {

        Context.getCurrentLogger().finer(
                "Method update() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
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
            if (result == 0) {
                return null;
            } else {
                return toUpdate;
            }
        } finally {

            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method update() of ContactPersistence finished.");
        }
    }

    @Override
    public Boolean delete(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method delete() of ContactPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from Contact where id=?");
            preparedStatement.setString(1, id);

            Integer result = preparedStatement.executeUpdate();
            if (result == 0) {
                return false;
            }
            return true;
        } finally {

            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method delete() of ContactPersistence called.");
        }
    }

}
