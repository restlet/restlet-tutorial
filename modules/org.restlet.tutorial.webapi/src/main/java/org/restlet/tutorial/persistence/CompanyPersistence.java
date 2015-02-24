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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.tutorial.persistence.entity.Company;

/**
 * Handles operations on the table Company.
 * 
 * @author Guillaume Blondeau
 */
public class CompanyPersistence extends PersistenceService<Company> {

    // Singleton pattern.
    private static CompanyPersistence companyPersistence = new CompanyPersistence();

    public static synchronized CompanyPersistence getCompanyPersistence() {
        return companyPersistence;
    }

    private CompanyPersistence() {
    }

    @Override
    public Company add(Company toAdd) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method add() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement(
                            "insert into Company"
                                    + " (address, zip_code, company_creation, website, phone_number, city, name, duns)"
                                    + " values (?,?,?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, toAdd.getAddress());
            preparedStatement.setString(2, toAdd.getZipCode());
            preparedStatement.setDate(3,
                    toAdd.getCreationDate() != null ? new java.sql.Date(toAdd
                            .getCreationDate().getTime()) : null);
            preparedStatement.setString(4, toAdd.getWebsite());
            preparedStatement.setString(5, toAdd.getPhoneNumber());
            preparedStatement.setString(6, toAdd.getCity());
            preparedStatement.setString(7, toAdd.getName());
            preparedStatement.setString(8, toAdd.getDuns());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            toAdd.setId(generatedKeys.getString(1));

            Context.getCurrentLogger().finer("Added a new Company to the DB.");
            return toAdd;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method add() of CompanyPersistence finished.");
        }
    }

    @Override
    public Boolean delete(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method delete() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from Company where id=?");
            preparedStatement.setString(1, id);

            Integer result = preparedStatement.executeUpdate();
            // return false if no row has been updated.
            return result != 0;

        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method delete() of CompanyPersistence finished.");
        }
    }

    @Override
    public List<Company> findAll() throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findAll() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from Company");
            ResultSet rs = preparedStatement.executeQuery();

            List<Company> companies = new ArrayList<Company>();
            while (rs.next()) {
                Company company = new Company();
                companies.add(company);
                company.setAddress(rs.getString("address"));
                company.setDuns(rs.getString("duns"));
                company.setCity(rs.getString("city"));
                company.setCreationDate(rs.getDate("company_creation"));
                company.setId(rs.getString("id"));
                company.setPhoneNumber(rs.getString("phone_number"));
                company.setWebsite(rs.getString("website"));
                company.setZipCode(rs.getString("zip_code"));
                company.setName(rs.getString("name"));
            }

            return companies;
        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findAll() of CompanyPersistence finished.");
        }

    }

    @Override
    public Company findById(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findById() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from Company where id=?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Company company = new Company();
                company.setAddress(rs.getString("address"));
                company.setDuns(rs.getString("duns"));
                company.setCity(rs.getString("city"));
                company.setCreationDate(rs.getDate("company_creation"));
                company.setId(rs.getString("id"));
                company.setPhoneNumber(rs.getString("phone_number"));
                company.setWebsite(rs.getString("website"));
                company.setZipCode(rs.getString("zip_code"));
                company.setName(rs.getString("name"));

                return company;
            }
            return null;
        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findById() of CompanyPersistence finished.");
        }
    }

    @Override
    public Company update(Company toUpdate, String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method update() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update Company set "
                            + "address=?, phone_number=?, zip_code=?, company_creation=?, website=?, city=?, name=?, duns=? "
                            + "where id=?;");
            preparedStatement.setString(1, toUpdate.getAddress());
            preparedStatement.setString(2, toUpdate.getPhoneNumber());
            preparedStatement.setString(3, toUpdate.getZipCode());
            preparedStatement.setDate(4,
                    toUpdate.getCreationDate() != null ? new java.sql.Date(
                            toUpdate.getCreationDate().getTime()) : null);
            preparedStatement.setString(5, toUpdate.getWebsite());
            preparedStatement.setString(6, toUpdate.getCity());
            preparedStatement.setString(7, toUpdate.getName());
            preparedStatement.setString(8, toUpdate.getDuns());
            preparedStatement.setString(9, id);

            Integer result = preparedStatement.executeUpdate();
            if (result != 0) {
                return toUpdate;
            }
            
            return null;
        } finally {
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method update() of CompanyPersistence finished.");
        }
    }

}
