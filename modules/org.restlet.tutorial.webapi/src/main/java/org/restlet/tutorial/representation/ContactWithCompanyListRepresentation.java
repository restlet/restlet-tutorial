package org.restlet.tutorial.representation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ContactWithCompanyListRepresentation {

    @JacksonXmlProperty(localName = "contact")
    private List<ContactWithCompanyRepresentation> list;

    public List<ContactWithCompanyRepresentation> getList() {
        if (list == null) {
            list = new ArrayList<ContactWithCompanyRepresentation>();
        }
        return list;
    }

    public void setList(List<ContactWithCompanyRepresentation> list) {
        this.list = list;
    }

}
