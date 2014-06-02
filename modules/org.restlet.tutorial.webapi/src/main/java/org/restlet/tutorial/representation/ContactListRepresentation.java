package org.restlet.tutorial.representation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ContactListRepresentation {

    @JacksonXmlProperty(localName = "contact")
    private List<ContactRepresentation> list;

    public List<ContactRepresentation> getList() {
        if (list == null) {
            list = new ArrayList<ContactRepresentation>();
        }
        return list;
    }

    public void setList(List<ContactRepresentation> list) {
        this.list = list;
    }

}
