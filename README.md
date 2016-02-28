# Workspace Setup

## Java
* Use JDK 1.8

## MySQL setup
* Download and install the latest MySQL https://dev.mysql.com/downloads/mysql
* (*NIX users) Install Sequel Pro http://www.sequelpro.com/download
* Create the "active_bean_fitness" database, default charset utf-8.

## Elasticsearch setup
* Download Elasticsearch 2.1.0 at https://www.elastic.co/downloads/elasticsearch
* Extract the archive to any directory.  Installed!
* (*NIX users) Execute "cd /path/to/elasticsearch/bin" and then "sh elasticsearch".  Ctrl+C to quit the process.
* (Windows users) Execute "cd C:\path\to\elasticsearch\bin\" and then "elasticsearch".  Close the terminal window to quit the process.

## Install Git
* SourceTree app recommended.

## Install Maven
* Use your installation method of choice: homebrew, or downloading manually (https://maven.apache.org/download.cgi).
** (*NIX users) If you install Maven manually, add the Maven binaries to the $PATH by adding the following to your ~/.bash_profile: export PATH="${PATH}:/path/to/my/maven/bin"
** (Windows users) If you install Maven manually, ...??
* Ensure 'mvn' is on your executable path.  Open a terminal and execute 'mvn -v'
* Ensure 'mvn -v' shows the desired JDK version is coupled with Maven (such as "Java version: 1.8.0_45, vendor: Oracle Corporation").

## Code setup
* git clone https://github.com/ActiveBeanCoders/active-bean-fitness
* There are multiple projects inside the active-bean-fitness folder.  Each sub-folder is a separate project.  If you're using Eclipse, each of those folders is a new project inside your workspace.  If you're using IntelliJ, each folder is a separate module inside your project.

## Database setup
Your database should already be created (see MySQL section above).  There are some JUnit tests you can run to get the tables created.  Those tests are:
* hibernate-service/src/integration_test/com/activebeancoders/fitness/HibernateServiceTableCreatorTestIT.java
* security-service/src/integration_test/com/activebeancoders/fitness/security/SecurityServiceTableCreatorTestIT.java

## Run project
See below: "Run project locally".

## Build random data 
* Run the project.
* Log-in.
* Go to "Home" page.
* Click button to reload data.  If you wish to change the amount of records, change the source code in `app.js`.

## View data

### Elasticsearch
* http://localhost:8080/resource/activity/{id}
* http://localhost:9200/com.activebeancoders.entity/Activity/{id}
* http://localhost:9200/com.activebeancoders.entity/Activity/_search
* see https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html for more on how to use the Elasticsearch REST API

### MySQL
* Use MySQL 5.6!
* Use Sequel Pro or some other MySQL app.

## activebeancoders.com server stuff
* (*NIX users) To log-in, execute "ssh <username>@activebeancoders.com".
* (Windows users) To log-in, use PuTTY (http://www.putty.org/).
* To change your password, use 'passwd' command.
* JDK by default points to /opt/jdk, which is a symbolic link.  Changing this symbolic link will change the JDK for all users.
* Apache web server config files: /etc/apache2/sites-enabled/000-default.conf, /etc/apache2/httpd.conf.
* Apache web server document root: /var/www/
* Apache Tomcat root: /var/lib/tomcat7/
* Apache Tomcat config: /etc/tomcat7/
* Projects located in /www

## Common Problems / FAQ

#### java.io.FileNotFoundException: class path resource [application.properties] cannot be opened because it does not exist
The file exists in the project source directory, but not the build directory.  In IntelliJ, recompile the project (Cmd+Shift+F9).

#### IntelliJ module dependencies are missing!
Try to build your project.  IntelliJ will take you to the first error location.  Click on the error text in red underline, click the red light bulb, choose the first option which is probably "add dependency on module xyz".  Wait, your errors should go away in a few seconds.  Rebuild the project (AKA "make project").  Repeat as necessary.

Other things that might help:
1. Right click module > Maven > re-import
2. Click that same module, in menubar: Build > Compile Module.
3. In menubar: Build > Make Project.

# Project properties

Each project has its own properties file.  gateway has "gateway.properties", security-api has "security-api.properties" and so on.  Each of these files are in the project and will be bundled with the jar/war.  Each project also reads files from an external directory.  The property files in this external directory will add to or override the project's bundled properties.  Example:

security-service reads properties from (potentially) 2 files in this order:  
1. `security-service/src/main/resources/security-service.properties`  
2. `/activebeancoders/security-service.properties` (optional)

Properties in #2 will override those in #1.

Here is an example /activebeancoders/security-service.properties file:
```properties
# This file overrides project properties files packaged within the jar/war files.
# This file can be overridden by ~/activebeancoders/<my-project>.properties.

hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.format_sql=false
hibernate.password=
hibernate.show_sql=false
hibernate.url=jdbc:mysql://localhost:3306/active_bean_fitness
hibernate.username=root

logging.level.*=WARN
logging.level.com.activebeancoders=DEBUG
logging.level.org.elasticsearch=WARN
logging.level.org.hibernate=WARN
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework=WARN
```

# Run the project locally

## Architecture overview

The app is split into several services, all of which should be running, all of which use a separate Tomcat server and separate port number.  The services are:
* **gateway**: receives all requests and routes them to correct server and port.
* **data-access-service**: abstraction layer, moves data in/out of storage system(s).
* **ui-angular**: the user interface.
* **elasticsearch-service**: moves data in/out of Elasticsearch.
* **hibernate-service**: moves data in/out of MySQL.
* **security-service**: log-in, log-out, authenticates every request.

## Build APIs
The services depend on APIs that need to be built and installed into your local Maven repository.  There is a script located in the `scripts` directory that can be used to build all the APIs.  Execute it from the project root directory (that is, `active-bean-fitness`):
```
sh scripts/build-apis.sh
```

Make sure it completes successfully.  If it fails, the last line should read "Dying...".

If you're not running Linux/UNIX, open the script in a text editor to see what it's doing and reproduce the steps on your machine however you want.  If you find a way that works well on non *NIX machines, please add a new script.

## Copy HTTPS keystore and truststore
* Copy the config/keystore.p12 file to /activebeancoders.
* Copy the config/truststore.jks to /activebeancoders.

## Start services
Open a new terminal window in each of the 6 service folders.  For each service, run the command
```
mvn clean spring-boot:run
```

## Access in browser
Go to https://localhost:8080  (don't forget the http*S*!)

## Sign up / Log-in
Log-in, or sign up with any username/password you want.

