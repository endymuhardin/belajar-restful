Template Project ArtiVisi
=========================

Project Modules
---------------

Project ini terdiri dari beberapa sub-project : 
* config : konfigurasi (misalnya jdbc.properties)
* deployer : ini untuk deploy, sementara bisa diignore dulu
* domain : entity class, dan service interface kita pakai Hibernate, bukan JPA
* service : service implementation, pakai Spring untuk transaction
* web : controller Spring MVC

Automated Test
--------------
Dalam project ini, kita pakai beberapa automated test : 
* service : JUnit biasa, tapi pakai failsafe plugin supaya jalannya pada phase integration-test
* web : rest-assured
  http://code.google.com/p/rest-assured/wiki/Usage
  http://www.hascode.com/2011/10/testing-restful-web-services-made-easy-using-the-rest-assured-framework/

Build dan Run
-------------
Untuk menjalankan projectnya : 
1. Siapkan database MySQL
   * nama db : belajar_development
   * username : root
   * password : admin

2. Jalankan mvn clean install di top level folder
3. Masuk ke folder web, kemudian jalankan mvn cargo:run
4. Siap dibrowse di http://localhost:10000
