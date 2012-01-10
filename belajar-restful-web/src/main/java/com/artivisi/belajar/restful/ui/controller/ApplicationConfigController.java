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

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import com.artivisi.belajar.restful.ui.helper.Range;

@Controller
@RequestMapping("/config")
public class ApplicationConfigController {
	
	@Autowired private BelajarRestfulService belajarRestfulService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/{id}/files")
	@ResponseBody
	public Map<String, String> uploadFiles(
			@PathVariable String id,
			@RequestParam String keterangan, 
			@RequestParam MultipartFile cv, 
			@RequestParam MultipartFile foto
			) throws Exception {
		ApplicationConfig config = belajarRestfulService.findApplicationConfigById(id);
		if(config == null){
			throw new IllegalStateException();
		}
		
		Map<String, String> result = new HashMap<String, String>();
		
		logger.info("CV => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
				cv.getContentType(), cv.getOriginalFilename(), cv.getSize()});
		
		logger.info("Foto => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
				foto.getContentType(), foto.getOriginalFilename(), foto.getSize()});
		
		File cvTarget = File.createTempFile(cv.getOriginalFilename().split("\\.")[0], "."+cv.getOriginalFilename().split("\\.")[1]);
		File fotoTarget = File.createTempFile(foto.getOriginalFilename().split("\\.")[0], "."+foto.getOriginalFilename().split("\\.")[1]);
		cv.transferTo(cvTarget);
		foto.transferTo(fotoTarget);
		
		logger.info("CV disimpan ke {}",cvTarget.getAbsolutePath());
		logger.info("Foto disimpan ke {}",fotoTarget.getAbsolutePath());
		
		if(cv.getSize() == 51987l) {
			result.put("cv", "success");
		} else {
			result.put("cv", "error size");
		}
		
		if(foto.getSize() == 583738l) {
			result.put("foto", "success");
		} else {
			result.put("foto", "error size");
		}
		
		if("File Endy".equals(keterangan)) {
			result.put("keterangan", "success");
		} else {
			result.put("keterangan", "salah content");
		}
		
		return result;
	}
	
	@RequestMapping("/{id}")
	@ResponseBody
	public ApplicationConfig findApplicationConfigById(@PathVariable String id){
		ApplicationConfig config = belajarRestfulService.findApplicationConfigById(id);
		if(config == null){
			throw new IllegalStateException();
		}
		return config;
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ApplicationConfig config, HttpServletRequest request, HttpServletResponse response){
		belajarRestfulService.save(config);
		String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}{id}").expand(requestUrl, config.getId());
        response.setHeader("Location", uri.toASCIIString());
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable String id, @RequestBody ApplicationConfig config){
		ApplicationConfig a = belajarRestfulService.findApplicationConfigById(id);
		if(a == null){
			logger.warn("Config dengan id [{}] tidak ditemukan", id);
			throw new IllegalStateException();
		}
		config.setId(a.getId());
		belajarRestfulService.save(config);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String id){
		ApplicationConfig a = belajarRestfulService.findApplicationConfigById(id);
		if(a == null){
			logger.warn("Config dengan id [{}] tidak ditemukan", id);
			throw new IllegalStateException();
		}
		belajarRestfulService.delete(a);
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	public List<ApplicationConfig> findAll(@RequestHeader(value="Range", required=false) String range, 
			@RequestParam(required=false) String search,
			HttpServletResponse response){
		logger.debug("Range : [{}]", range);
		
		Range requestRange = Range.fromRequestHeader(range);
		
		if(StringUtils.hasText(search)){
			Long countAll = belajarRestfulService.countApplicationConfigs(search);
			Range responseRange = new Range(requestRange.getFrom(), requestRange.getTo(), countAll);
			logger.debug("Response Range : {}", responseRange.toString());
			response.setHeader("Content-Range", responseRange.toResponseHeader());
			
			return belajarRestfulService.findApplicationConfigs(search, responseRange.getFrom().longValue()-1, 
					(responseRange.getTo() - responseRange.getFrom()+1));
		} else {
			Long countAll = belajarRestfulService.countAllApplicationConfigs();
			Range responseRange = new Range(requestRange.getFrom(), requestRange.getTo(), countAll);
			response.setHeader("Content-Range", responseRange.toResponseHeader());
			
			return belajarRestfulService.findAllApplicationConfigs(responseRange.getFrom().longValue() -1 , 
					(responseRange.getTo() - responseRange.getFrom()+1));
		}
		
		
	}
	
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
		logger.debug("Konfig dengan nama tersebut tidak ditemukan");
    }
	
}
