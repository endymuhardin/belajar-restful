# Template Project ArtiVisi #

## Project Modules ##

Project ini terdiri dari beberapa sub-project : 

* config : berisi jdbc.properties, dataSource, transactionManager, dsb
* domain : berisi @Entity dan service interface
* service : berisi @Service dan @Repository
* web : berisi @Controller, HTML, JavaScript, dsb


## Automated Test ##

Dalam project ini, kita pakai beberapa automated test : 

* service : JUnit biasa, tapi pakai failsafe plugin supaya jalannya pada phase integration-test

* web : rest-assured

	* http://code.google.com/p/rest-assured/wiki/Usage 
	* http://www.hascode.com/2011/10/testing-restful-web-services-made-easy-using-the-rest-assured-framework/ 

## Build dan Run ##

Untuk menjalankan projectnya : 

1. Siapkan database MySQL

    * nama db : belajar_development 
    * username : root 
    * password : admin 

2. Jalankan mvn clean install di top level folder
3. Masuk ke folder web, kemudian jalankan mvn jetty:run
4. Siap dibrowse di http://localhost:10000

## Konfigurasi ##

* Koneksi database : edit file `pom.xml` di top level folder, property `db.driver`, `db.url`, `db.username`, `db.password`
* Port Aplikasi : edit file `pom.xml` di top level folder, property `appserver.port.http`
* Context Path : edit file `pom.xml` di top level folder, property `appserver.deployment.context`

## Teknologi dan Library ##

### Framework dan Library ###

* Spring Framework 3.2.0
* Spring Security 3.1.3
* Spring Data JPA 1.1.0
* Hibernate 4.1.9
* Joda Time 2.1
* Logback 1.0.9
* AngularJS 1.0.3
* Twitter Bootstrap 2.2.1
* jQuery 1.8.3

### Tools ###

* Build Tool : Maven 2
* Database Schema : Liquibase
* Unit Test Runner : Maven Surefire Plugin
* Integration Test Runner : Maven Failsafe Plugin
* Functional Test : Rest-Assured
* Performance Monitoring : Javamelody
* Application Server : Jetty 6