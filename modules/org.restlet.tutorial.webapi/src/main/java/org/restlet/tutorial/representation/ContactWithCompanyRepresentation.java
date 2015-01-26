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

package org.restlet.tutorial.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Acts as container for the representation of a contact.<br>
 * It is mainly used in order to gain fine control on the way we generate XML
 * and JSON document via Jackson.<br>
 * 
 * This sample code leverages several annotations in order to control the
 * serialization:
 * <ul>
 * <li>the {@link JacksonXmlRootElement} annotation : the root element of the
 * XML representation will be called "contact".</li>
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
@JacksonXmlRootElement(localName = "contact")
@JsonInclude(Include.NON_NULL)
public class ContactWithCompanyRepresentation {

    private String email;

    private int age;

    private String name;

    private String firstName;

    private CompanyRepresentation company;

    /** The URL of this resource. */
    private String self;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public CompanyRepresentation getCompany() {
        return company;
    }

    public void setCompany(CompanyRepresentation company) {
        this.company = company;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
