## Deploying in a servlet container ##

### Implementing a simple web.xml ###


    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="http://java.sun.com/xml/ns/javaee"
             xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
             id="restletTutorial" version="2.5">
        <display-name>Restlet tutorial</display-name>

        <context-param>
            <param-name>org.restlet.application</param-name>
            <param-value>org.restlet.tutorial.server.RestletTutorialApplication</param-value>
        </context-param>

        <servlet>
            <servlet-name>ServerServlet</servlet-name>
            <servlet-class>org.restlet.ext.servlet.ServerServlet</servlet-class>
        </servlet>

        <servlet-mapping>
            <servlet-name>ServerServlet</servlet-name>
            <url-pattern>/*</url-pattern>
        </servlet-mapping>
    </web-app>


### Configuring Restlet application from web.xml ###

TODO: configuring client connectors
