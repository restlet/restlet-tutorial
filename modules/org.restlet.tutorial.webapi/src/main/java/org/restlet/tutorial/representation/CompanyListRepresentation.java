package org.restlet.tutorial.representation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "companies")
public class CompanyListRepresentation {

    @JacksonXmlProperty(localName = "company")
    private List<CompanyRepresentation> list;

    public List<CompanyRepresentation> getList() {
        if (list == null) {
            list = new ArrayList<CompanyRepresentation>();
        }
        return list;
    }

    public void setList(List<CompanyRepresentation> list) {
        this.list = list;
    }

}