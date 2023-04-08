# dwcj-install-maven-plugin
A plug-in that automates installation of your project file in the application server. 
The plugin executes during the ```mvn install``` step, and passes the JAR over HTTP(S) to a servlet running in the 
application server. The output of the servlet is displayed in the log, so the user can verify
the success of the installation steps. 

## Configuration

The following snippet shows a typical configuration in a DWCJ project. Include this in the 
```<plugins>``` section of your DWCJ project:

```xml
            <plugin>
                <groupId>org.dwcj</groupId>
                <artifactId>dwcj-install-maven-plugin</artifactId>
                <version>0.1.0</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>install</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <deployurl>http://localhost:8888/dwcj-install</deployurl>
                    <classname>samples.HelloWorldJava</classname>
                    <username>admin</username>
                    <password>admin123</password>
                    <publishname>hworld</publishname>
                    <debug>true</debug>
                </configuration>
            </plugin>
```

The ```<configuration>``` supports the following entries:

| Tag                                                               | Description                                                                                                                                                                                                    |
|-------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ``` <deployurl>http://localhost:8888/dwcj-install</deployurl> ``` | The URL under which the DWCJ endpoint for the project installation can be reached.                                                                                                                             |
| ``` <username>admin</username> ```                                | The username of the admin user or a user that is enabled to deploy classpath entries and web apps.                                                                                                             |
| ``` <password>admin123</password>```                              | The password for the admin account.                                                                                                                                                                            |                                 
| ``` <token>admin123</token>```                                    | Instead of passing username and password, you can use a deploy token that you can generate in BBj's EM.                                                                                                        |                                       
| ``` <publishname>hworld</publishname>```                          | Optional: The name for the deployed application that becomes also part of the URL. If not provided, the name of the JAR file is used.                                                                          |                             
| ``` <classname>samples.HelloWorldJava</classname>```              | Optional: The name of the class to be launched directly. If not provided, the app server will determine if there are other App implementations available in the classpath and might provide a list to the user |                 
| ``` <debug>true</debug>```                                        | Optional: If set to ```true``` the installed application will run in DEBUG mode and list error information in the console.                                                                                     |                                           