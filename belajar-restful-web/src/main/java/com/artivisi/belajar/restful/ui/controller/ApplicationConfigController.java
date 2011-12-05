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

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import com.artivisi.belajar.restful.ui.helper.Range;

@Controller
@RequestMapping("/config")
public class ApplicationConfigController {
	
	@Autowired private BelajarRestfulService belajarRestfulService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/{name}")
	@ResponseBody
	public ApplicationConfig findApplicationConfigByName(@PathVariable String name){
		ApplicationConfig config = belajarRestfulService.findApplicationConfigByName(name);
		if(config == null){
			throw new IllegalStateException();
		}
		return config;
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@ModelAttribute ApplicationConfig config, HttpServletRequest request, HttpServletResponse response){
		belajarRestfulService.save(config);
		String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}{id}").expand(requestUrl, config.getId());
        response.setHeader("Location", uri.toASCIIString());
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable String name, @ModelAttribute ApplicationConfig config){
		ApplicationConfig a = belajarRestfulService.findApplicationConfigByName(name);
		if(a == null){
			logger.warn("Config dengan nama [{}] tidak ditemukan", name);
			throw new IllegalStateException();
		}
		config.setId(a.getId());
		config.setName(name);
		belajarRestfulService.save(config);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String name){
		ApplicationConfig a = belajarRestfulService.findApplicationConfigByName(name);
		if(a == null){
			logger.warn("Config dengan nama [{}] tidak ditemukan", name);
			throw new IllegalStateException();
		}
		belajarRestfulService.delete(a);
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	public List<ApplicationConfig> findAll(@RequestHeader(value="Range", required=false) String range, 
			HttpServletResponse response){
		logger.debug("Range : [{}]", range);
		
		Range requestRange = Range.fromRequestHeader(range);
		Long countAll = belajarRestfulService.countAllApplicationConfigs();
		Range responseRange = new Range(requestRange.getFrom(), requestRange.getTo(), countAll);
		response.setHeader("Content-Range", responseRange.toResponseHeader());
		
		return belajarRestfulService.findAllApplicationConfigs(responseRange.getFrom().longValue(), responseRange.getTo() - responseRange.getFrom());
	}
	
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
		logger.debug("Konfig dengan nama tersebut tidak ditemukan");
    }
	
}
