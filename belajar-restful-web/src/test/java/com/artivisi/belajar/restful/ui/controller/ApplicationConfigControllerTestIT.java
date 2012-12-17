/**
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.artivisi.belajar.restful.ui.controller;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.domain.User;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import com.jayway.restassured.authentication.FormAuthConfig;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

public class ApplicationConfigControllerTestIT {
    private static final String username = "endy";
    private static final String password = "123";

    private String target = "http://localhost:10000/config";
    private String login = "http://localhost:10000/j_spring_security_check";

    @Test
    public void testSaveInvalid() {
        ApplicationConfig u = new ApplicationConfig();

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(u)
                .expect().statusCode(400).when().post(target);
    }

    @Test
    public void testSaveUpdateDelete() {

        String id = testSave(target);
        System.out.println("Id : " + id);
        testGetExistingById(id, "coba", "Konfigurasi Percobaan", "test");
        testUpdateExisting(id, "coba", "Konfigurasi Percobaan 001", "test123");
        testGetExistingById(id, "coba", "Konfigurasi Percobaan 001", "test123");
        testDeleteExistingById(id);
    }

    private String testSave(String target) {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("coba");
        config.setLabel("Konfigurasi Percobaan");
        config.setValue("test");

        String location = given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(config)
                .expect().statusCode(201).when().post(target)
                .getHeader("Location");

        assertNotNull(location);
        assertTrue(location.startsWith(target));

        String[] locationSplit = location.split("/");
        String id = locationSplit[locationSplit.length - 1];

        return id;
    }

    private void testGetExistingById(String id, String name, String label,
            String value) {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("name", equalTo(name), "label", equalTo(label), "value",
                equalTo(value)).when().get(target + "/" + id);
    }

    private void testUpdateExisting(String id, String name, String label,
            String value) {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(name);
        config.setLabel(label);
        config.setValue(value);

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(config)
                .expect()
                .statusCode(200).when().put(target + "/" + id);
    }

    private void testDeleteExistingById(String id) {
        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(200).when().delete(target + "/" + id);

        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + id);
    }

    @Test
    public void testGetExistingConfigById() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("id", equalTo("abc123"),
                "name", equalTo("applicationname"),
                "label", equalTo("Application Name"),
                "value", equalTo("Belajar Restful")).when()
                .get(target + "/" + "abc123");
    }

    @Test
    public void testGetNonExistentConfigById() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + "/nonexistentconfig");
    }

    @Test
    public void testFindAll() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .header("Accept", "application/json").expect().statusCode(200)
                .body("id", hasItems("abc123", "def456")).when().get(target);
    }

    @Test
    public void testSearch() {
        with()
                .header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .param("search", "name")
                .expect().statusCode(200)
                .body("id", hasItems("abc123")).when().get(target);

        with()
                .header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .param("search", "xx")
                .expect().statusCode(200)
                .when().get(target);
    }

    @Test
    public void testUploadFile() {
        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .multiPart("foto", new File("src/test/resources/foto-endy.jpg"))
                .multiPart("cv", "cv-endy.pdf", ApplicationConfig.class.getResourceAsStream("/resume-endy-en.pdf"))
                .formParam("keterangan", "File Endy")
                .expect()
                .body(
                "keterangan", is("success"),
                "cv", is("success"),
                "keterangan", is("success"))
                .when()
                .post(target + "/" + "/abc123/files");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUploadPakaiRestTemplate() {
        RestTemplate rest = new RestTemplate();

        String jsessionid = rest.execute(login, HttpMethod.POST,
                new RequestCallback() {
                    @Override
                    public void doWithRequest(ClientHttpRequest request) throws IOException {
                        request.getBody().write(("j_username="+username+"&j_password="+password).getBytes());
                    }
                }, new ResponseExtractor<String>() {
            @Override
            public String extractData(ClientHttpResponse response) throws IOException {
                List<String> cookies = response.getHeaders().get("Cookie");

                // assuming only one cookie with jsessionid as the only value
                if (cookies == null) {
                    cookies = response.getHeaders().get("Set-Cookie");
                }

                String cookie = cookies.get(cookies.size() - 1);

                int start = cookie.indexOf('=');
                int end = cookie.indexOf(';');

                return cookie.substring(start + 1, end);
            }
        });



        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
        form.add("foto", new FileSystemResource("src/test/resources/foto-endy.jpg"));
        form.add("Filename", "cv-endy.pdf");
        form.add("cv", new FileSystemResource("src/test/resources/resume-endy-en.pdf"));
        form.add("keterangan", "File Endy");
        Map<String, String> result = rest.postForObject(target + "/abc123/files;jsessionid=" + jsessionid, form, Map.class);

        assertEquals("success", result.get("cv"));
        assertEquals("success", result.get("foto"));
        assertEquals("success", result.get("keterangan"));
    }
}
