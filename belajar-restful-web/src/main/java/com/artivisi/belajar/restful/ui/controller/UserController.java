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

import com.artivisi.belajar.restful.domain.User;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

@Controller
public class UserController {

    @Autowired
    private BelajarRestfulService belajarRestfulService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<String> FILE_EXTENSION = Arrays.asList("png", "jpg", "jpeg");
    private final String SESSION_KEY_IMAGE = "sessionKeyPathImage";

    @RequestMapping("/user/{id}")
    @ResponseBody
    public User findById(@PathVariable String id) {
        User x = belajarRestfulService.findUserById(id);
        if (x == null) {
            throw new IllegalStateException();
        }
        fixLie(x);
        return x;
    }
    
    @RequestMapping(value = "/user/photo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(value="photo", required=true) MultipartFile multipartFile, 
        HttpServletRequest request, HttpSession session){
        Map<String, Object> result = new HashMap();
        
        if(multipartFile.isEmpty() || multipartFile == null){
            result.put("msg", "No file uploaded");
            result.put("status", "400");
        }else {
            String extension = tokenizer(multipartFile.getOriginalFilename(), ".");
            if(FILE_EXTENSION.contains(extension.toLowerCase())){
                try {
                    String filename = multipartFile.getOriginalFilename();
                    String targetFile = File.separator + "tmp" + File.separator + filename;
                    multipartFile.transferTo(new File(targetFile));
                    session.setAttribute(SESSION_KEY_IMAGE, targetFile);
                    result.put("msg", "Upload Succeded");
                    result.put("status", "200");
                } catch (IOException ex) {
                    result.put("msg", ex.getMessage());
                    result.put("status", "500");
                } catch (IllegalStateException ex) {
                    result.put("msg", ex.getMessage());
                    result.put("status", "500");
                }
            } else {
                result.put("msg", "File extensions not permitted");
                result.put("status", "400");
            }
        }
        return result;
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid User x, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String defaultPhoto = "img/user/no_photo.jpg";
        String pathPhoto = "img" + File.separator + "user"; // img/user
        
        if(session.getAttribute(SESSION_KEY_IMAGE) != null){
            defaultPhoto = (String) session.getAttribute(SESSION_KEY_IMAGE);
        }
        
        // ganti ke img/user/pic_endy.png
        String filename = "pic_" + x.getUsername().toLowerCase() + "." + tokenizer(defaultPhoto, ".");
        x.setPhoto(pathPhoto + File.separator + filename);
        logger.debug("SET PHOTO PATH : " + x.getPhoto());
        
        File tmp = new File(defaultPhoto);
        File newFile = new File(request.getSession().getServletContext().getRealPath(pathPhoto), filename);
        if(tmp.exists()){
            logger.info("FILE MOVED TO : " + newFile.getAbsolutePath() + File.separator + filename);
            tmp.renameTo(newFile);
        }
        
        belajarRestfulService.save(x);
        
        session.removeAttribute(SESSION_KEY_IMAGE);
        
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, x.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid User x, HttpServletRequest request, HttpSession session) {
        User a = belajarRestfulService.findUserById(id);
        if (a == null) {
            logger.warn("User dengan id [{}] tidak ditemukan", id);
            throw new IllegalStateException();
        }
        x.setId(a.getId());
        
        String defaultPhoto = "img/user/no_photo.jpg";
        String pathPhoto = "img" + File.separator + "user"; // img/user
        
        if(session.getAttribute(SESSION_KEY_IMAGE) != null){
            defaultPhoto = (String) session.getAttribute(SESSION_KEY_IMAGE);
        }
        
        // ganti ke img/user/pic_endy.png
        String filename = "pic_" + x.getUsername().toLowerCase() + "." + tokenizer(defaultPhoto, ".");
        x.setPhoto(pathPhoto + File.separator + filename);
        logger.debug("SET PHOTO PATH : " + x.getPhoto());
        
        File tmp = new File(defaultPhoto);
        File newFile = new File(request.getSession().getServletContext().getRealPath(pathPhoto), filename);
        if(tmp.exists()){
            logger.info("FILE MOVED TO : " + newFile.getAbsolutePath() + File.separator + filename);
            tmp.renameTo(newFile);
        }
        
        belajarRestfulService.save(x);
        session.removeAttribute(SESSION_KEY_IMAGE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        User a = belajarRestfulService.findUserById(id);
        if (a == null) {
            logger.warn("User dengan id [{}] tidak ditemukan", id);
            throw new IllegalStateException();
        }
        belajarRestfulService.delete(a);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public List<User> findAll(
            Pageable pageable,
            HttpServletResponse response) {
        List<User> hasil = belajarRestfulService.findAllUsers(pageable).getContent();

        for(User u : hasil){
            fixLie(u);
        }

        return hasil;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Resource dengan URI tersebut tidak ditemukan");
    }

    private void fixLie(User x){
        x.getRole().setPermissionSet(null);
        x.getRole().setMenuSet(null);
    }
    
    private String tokenizer(String filename, String token){
        StringTokenizer st = new StringTokenizer(filename, token);
        String result = "";
        while(st.hasMoreTokens()){
            result = st.nextToken();
        }
        return result;
    }
}
