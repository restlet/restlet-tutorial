package org.restlet.tutorial.utils;

import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyRepresentation;

public class CompanyUtils {

    public static Company companyRepresentationToCompany(
            CompanyRepresentation companyRepr) {
        if (companyRepr != null) {
            Company company = new Company();
            company.setAddress(companyRepr.getAddress());
            company.setCity(companyRepr.getCity());
            company.setCreationDate(companyRepr.getCreationDate());
            company.setName(companyRepr.getName());
            company.setPhoneNumber(companyRepr.getPhoneNumber());
            company.setWebsite(companyRepr.getWebsite());
            company.setZipCode(companyRepr.getZipCode());
            company.setDuns(companyRepr.getDuns());
            return company;
        }
        return null;
    }

    public static CompanyRepresentation companyToCompanyRepresentation(
            Company company) {
        if (company != null) {
            CompanyRepresentation companyRepr = new CompanyRepresentation();
            companyRepr.setAddress(company.getAddress());
            companyRepr.setDuns(company.getDuns());
            companyRepr.setCity(company.getCity());
            companyRepr.setCreationDate(company.getCreationDate());
            companyRepr.setName(company.getName());
            companyRepr.setPhoneNumber(company.getPhoneNumber());
            companyRepr.setWebsite(company.getWebsite());
            companyRepr.setZipCode(company.getZipCode());
            companyRepr.setDuns(company.getDuns());
            return companyRepr;
        }
        return null;
    }

}
