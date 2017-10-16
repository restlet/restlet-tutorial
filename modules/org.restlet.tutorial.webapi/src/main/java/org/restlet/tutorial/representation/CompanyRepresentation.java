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

package org.restlet.tutorial.representation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Acts as container for the representation of a company.<br>
 * It is mainly used in order to gain fine control on the way we generate XML
 * and JSON document via Jackson.<br>
 * 
 * This sample code leverages several annotations in order to control the
 * serialization:
 * <ul>
 * <li>the {@link JacksonXmlRootElement} annotation : the root element of the
 * XML representation will be called "company".</li>
 * <li>the {@link JsonInclude} annotation: any null or empty attributes won't be
 * serialized.</li>
 * </ul>
 * <br>
 * 
 * @see https://github.com/FasterXML/jackson-dataformat-xml#known-limitations
 * 
 * @author Manuel Boillod
 * @author Guillaume Blondeau
 */

@JacksonXmlRootElement(localName = "company")
@JsonInclude(Include.NON_NULL)
public class CompanyRepresentation {

    // http://en.wikipedia.org/wiki/Data_Universal_Numbering_System
    private String duns;

    private String address;

    private String zipCode;

    private Date creationDate;

    private String website;

    private String phoneNumber;

    private String city;

    private String name;

    /** The URL of this resource. */
    private String self;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuns() {
        return duns;
    }

    public void setDuns(String duns) {
        this.duns = duns;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

}
