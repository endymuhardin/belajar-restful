/**
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artivisi.belajar.restful.ui.controller;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ApplicationConfigControllerTestIT {
	private String target = "http://localhost:10000/config/";
	
	@Test public void testSaveUpdateDelete(){
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "coba");
		params.put("label", "Konfigurasi Percobaan");
		params.put("value", "test");
		
		testSave(target, params);
		testGetExistingByName("coba", "Konfigurasi Percobaan", "test");
		testUpdateExisting("coba", "Konfigurasi Percobaan 001", "test123");
		testGetExistingByName("coba", "Konfigurasi Percobaan 001", "test123");
		testDeleteExistingByName("coba");
	}
	
	private String testSave(String target, Map<String, String> params) {
		String location = with().
		parameters(params).
		expect().
		statusCode(201).
		when().post(target).getHeader("Location");
		
		assertNotNull(location);
		assertTrue(location.startsWith(target));
		
		String[] locationSplit = location.split("/");
		String id = locationSplit[locationSplit.length - 1];
		
		return id;
	}
	
	private void testGetExistingByName(String name, String label, String value){
		expect().
		statusCode(200).
		body(
				"name", equalTo(name), 
				"label", equalTo(label), 
				"value", equalTo(value)
		).
		when().get(target+name);
	}
	
	private void testUpdateExisting(String name, String label, String value){
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("label", label);
		params.put("value", value);
		
		with().parameters(params).
		expect().
		statusCode(200).
		when().put(target+name);
	}
	
	private void testDeleteExistingByName(String name){
		expect().
		statusCode(200).
		when().delete(target+name);
		
		expect().
		statusCode(404).
		when().get(target+name);
	}
	
	@Test
	public void testGetExistingConfigByName(){
		expect().
		statusCode(200).
		body(
				"id", equalTo("abc123"), 
				"name", equalTo("applicationname"), 
				"label", equalTo("Application Name"), 
				"value", equalTo("Belajar Restful")
		).
		when().get(target+"applicationname");
	}
	
	@Test
	public void testGetNonExistentConfigByName(){
		expect().
		statusCode(404).
		when().get(target+"/nonexistentconfig");
	}
	
	@Test
	public void testFindAll(){
		with()
		.header("Range", "items=0-5")
		.expect()
		.statusCode(200)
		.header("Content-Range", "items 0-2/2")
		.body(
				"id", hasItems("abc123", "def456")
		)
		.when().get(target);
	}
}
