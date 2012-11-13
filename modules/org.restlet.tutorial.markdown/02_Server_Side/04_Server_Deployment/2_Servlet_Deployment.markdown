## Deploying applications within servlet containers ##

Now you have a Restlet applications implemented based on previous described use cases:

* Implementing a simple Web API
* Implementing a Web application

it's time to see deploy and execute them within a server. Restlet provides several approaches
for this issue and we will focus here on the one based on a standalone server.

### Organizing the Web application ###

There is nothing specific here to Restlet but you need to follow the organization of Web applications
of Java EE, as below:

    WebAppRoot
      +- WEB-INF
           +- classes
           +- lib
           -- web.xml

The `WEB-INF/classes` folder must contain the classes of your Web application, `WEB-INF/lib` the jar files of
the libraries and frameworks used and the `web.xml`, the configuration of the Web application.

You can notice that, in the case of GAE (Google App Engine), you need to add an additional file named
appengine-web.xml within the `WEB-INF` folder for configurations related to the GAE environment.

### Configuring the Web application ###

Restlet provides a servlet adapter for servlet container and Restlet applications must be configured in this case
within the `web.xml` file. You will find all elements that can be configured on a Restlet component since this
servlet corresponds to a wrapper upon it.

The following code describes the minimal content of the `web.xml` file to configure Restlet within a servlet
environment:

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="http://java.sun.com/xml/ns/javaee"
             xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                      http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
             id="myApplication" version="2.5">
        <display-name>My Application</display-name>

        <context-param>
            <param-name>org.restlet.application</param-name>
            <param-value>org.restlet.tutorial.MyApplication</param-value>
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

You can enhance this configuration by adding the client protocols you want to use with the `org.restlet.clients` attribute
of the servlet, as described below:

    <web-app (...)>
        <servlet>
            (...)
            <init-param>
                <param-name>org.restlet.clients</param-name>
                <param-value>CLAP,HTTP</param-value>
            </init-param>
        </servlet>
    </web-app>

You probably have all you need with this approach but you also can choose to define your own component object and configure
it within the `web.xml` file, as described below. In this case, initialization will take it rather than initialization parameters.

    <web-app (...)>
        <servlet>
            (...)
            <init-param>
                <param-name>org.restlet.component</param-name>
                <param-value>org.restlet.tutorial.MyComponent</param-value>
            </init-param>
        </servlet>
    </web-app>

In the case of deployment for GAE, the appengine-web.xml file contains configurations related to GAE
and there is nothing regarding Restlet. The following code describes a minimal content for this file
and you don't provide here more details. See the GAE documentation if necessary.

    <?xml version="1.0" encoding="utf-8"?>
    <appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
        <application>myapplication</application>
        <version>1</version>
        <threadsafe>true</threadsafe>
        <sessions-enabled>false</sessions-enabled>

        <system-properties>
            <property name="java.util.logging.config.file" value="WEB-INF/logging.properties"/>
        </system-properties>

        <env-variables>
            <env-var name="DEFAULT_ENCODING" value="ISO-8859-1" />
        </env-variables>
    </appengine-web-app>

Let's deploy now the application.

### Deploying the Web application ###

Deployment of the application follows mechanisms provided by servlet container and different execution
environment. For more details, have a look at the corresponding documentations.
