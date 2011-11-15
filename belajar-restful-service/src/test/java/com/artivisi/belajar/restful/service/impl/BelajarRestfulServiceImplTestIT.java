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
package com.artivisi.belajar.restful.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.service.BelajarRestfulService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:com/artivisi/**/applicationContext.xml"})
@TransactionConfiguration(defaultRollback=true)
public class BelajarRestfulServiceImplTestIT {
	@Autowired private BelajarRestfulService service;
	@Autowired private DataSource dataSource;
	
	@Before
	public void resetDatabase() throws Exception {
		Connection conn = dataSource.getConnection();
		DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(conn), 
				new FlatXmlDataSetBuilder().build(new File("src/test/resources/sample-data.xml")));
	}
	
	@Test public void testFindByName(){
		ApplicationConfig ac = service.findApplicationConfigByName("applicationversion");
		assertNotNull(ac);
		assertEquals("applicationversion", ac.getName());
		assertEquals("Application Version", ac.getLabel());
		assertEquals("1.0", ac.getValue());
	}
	
	@Test public void testSaveNew(){
		ApplicationConfig ac = new ApplicationConfig();
		ac.setName("base.path");
		ac.setLabel("Installation Path");
		ac.setValue("/opt");
		
		service.save(ac);
		assertNotNull(ac.getId());
	}
	
	@Test public void testSaveExisting(){
		ApplicationConfig ac = service.findApplicationConfigByName("applicationversion");
		assertNotNull(ac);
		ac.setLabel("Versi Aplikasi");
		ac.setValue("2.0");
		
		service.save(ac);

		ApplicationConfig ac1 = service.findApplicationConfigByName("applicationversion");
		assertNotNull(ac1);
		assertEquals("Versi Aplikasi", ac1.getLabel());
		assertEquals("2.0", ac1.getValue());
	}
	
	@Test public void testDeleteExisting(){
		ApplicationConfig ac = service.findApplicationConfigByName("applicationversion");
		assertNotNull(ac);
		
		service.delete(ac);

		ApplicationConfig ac1 = service.findApplicationConfigByName("applicationversion");
		assertNull(ac1);
	}

}
