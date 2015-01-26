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

package org.restlet.tutorial.utils;

import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyRepresentation;

public class CompanyUtils {

    public static Company toCompany(CompanyRepresentation companyRepr) {
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

    public static CompanyRepresentation toCompanyRepresentation(Company company) {
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
