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

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.service.BelajarRestfulService;

@Service("belajarRestfulService")
@Transactional
public class BelajarRestfulServiceImpl implements BelajarRestfulService {

	@Autowired private SessionFactory sessionFactory;
	
	@Override
	public void save(ApplicationConfig ac) {
		sessionFactory.getCurrentSession().saveOrUpdate(ac);
	}

	@Override
	public void delete(ApplicationConfig ac) {
		if(ac == null || ac.getId() == null) return;
		sessionFactory.getCurrentSession().delete(ac);
	}

	@Override
	public ApplicationConfig findApplicationConfigByName(String name) {
		if(!StringUtils.hasText(name)) return null;
		return (ApplicationConfig) sessionFactory.getCurrentSession().createQuery("from ApplicationConfig ac where ac.name = :name")
				.setString("name", name)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationConfig> findAllApplicationConfigs() {
		return sessionFactory.getCurrentSession().createQuery("from ApplicationConfig ac order by ac.name")
			   .list();
	}

}
