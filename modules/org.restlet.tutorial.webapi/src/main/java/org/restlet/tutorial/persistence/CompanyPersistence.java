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
 * Makes operations on the table Company. (CRUD)
 * 
 * @author Guillaume Blondeau
 * 
 */
public class CompanyPersistence extends PersistenceService<Company> {

    /*
     * Singleton pattern.
     */
    private static CompanyPersistence companyPersistence;

    public static synchronized CompanyPersistence getCompanyPersistence() {
        if (companyPersistence == null) {
            companyPersistence = new CompanyPersistence();
        }
        return companyPersistence;
    }

    private CompanyPersistence() {
    };

    @Override
    public List<Company> findAll() throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findAll() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Company");
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
    public List<Company> findBy(String fieldName, String fieldValue)
            throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findBy() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Company where "
                            + fieldName + "=?");
            preparedStatement.setString(1, fieldValue);
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
                    "Method findBy() of CompanyPersistence finished.");
        }

    }

    @Override
    public Company findById(String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method findById() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * FROM Company where id=?");
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
            } else {
                return null;
            }
        } finally {

            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method findById() of CompanyPersistence finished.");
        }
    }

    @Override
    public Company add(Company toAdd) throws SQLException{

        Context.getCurrentLogger().finer(
                "Method add() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Company "
                            + "(address, zip_code, company_creation, website, phone_number, city, name, duns) VALUES "
                                    + "(?,?,?,?,?,?,?,?)",
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
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from Company where id=?");
            preparedStatement.setString(1, id);

            Integer result = preparedStatement.executeUpdate();
            if (result == 0) {
                /*
                 * False is returned if no result is updated. So if the id does
                 * not exist.
                 */
                return false;
            }
            return true;
        } finally {
            
            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method delete() of CompanyPersistence finished.");
        }
    }

    @Override
    public Company update(Company toUpdate, String id) throws SQLException {

        Context.getCurrentLogger().finer(
                "Method update() of CompanyPersistence called.");

        Connection connection = null;
        try {
            connection = createConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Update Company SET "
                            + "address=?, phone_number=?, zip_code=?, company_creation=?, website=?, city=?, name=?, duns=? "
                            + "WHERE id=?;");
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
            if (result == 0) {
                return null;
            } else {
                return toUpdate;
            }

        } finally {

            releaseConnection(connection);
            Context.getCurrentLogger().finer(
                    "Method update() of CompanyPersistence finished.");
        }
    }

}
