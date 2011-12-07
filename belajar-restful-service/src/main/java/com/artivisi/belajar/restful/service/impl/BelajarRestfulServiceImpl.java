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

@SuppressWarnings("unchecked")
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
		if(ac == null || ac.getId() == null) {
			return;
		}
		sessionFactory.getCurrentSession().delete(ac);
	}

	@Override
	public ApplicationConfig findApplicationConfigById(String id) {
		if(!StringUtils.hasText(id)) {
			return null;
		}
		return (ApplicationConfig) sessionFactory.getCurrentSession()
				.get(ApplicationConfig.class, id);
	}

	@Override
	public List<ApplicationConfig> findAllApplicationConfigs(Long start, Integer rows) {
		return sessionFactory.getCurrentSession().createQuery("from ApplicationConfig ac order by ac.name")
				.setFirstResult(start.intValue())
				.setMaxResults(rows)
			   .list();
	}

	@Override
	public Long countAllApplicationConfigs() {
		return (Long) sessionFactory.getCurrentSession()
				.createQuery("select count(ac) from ApplicationConfig ac order by ac.name")
			   .uniqueResult();
	}

}
