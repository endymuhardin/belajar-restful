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

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import com.google.common.io.Files;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

@Controller
public class ApplicationConfigController {

    private static final String ESC_CHAR_TITIK = "\\.";
    private static final long UKURAN_FILE_CV = 51987l;
    private static final long UKURAN_FILE_FOTO = 583738l;
    @Autowired
    private BelajarRestfulService belajarRestfulService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/config/{id}/files")
    @ResponseBody
    public Map<String, String> uploadFiles(
            @PathVariable String id,
            @RequestParam String keterangan,
            @RequestParam MultipartFile cv,
            @RequestParam MultipartFile foto) throws Exception {
        ApplicationConfig config = belajarRestfulService.findApplicationConfigById(id);
        if (config == null) {
            throw new IllegalStateException();
        }

        Map<String, String> result = new HashMap<String, String>();

        logger.info("CV => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
                    cv.getContentType(), cv.getOriginalFilename(), cv.getSize()});

        logger.info("Foto => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
                    foto.getContentType(), foto.getOriginalFilename(), foto.getSize()});

        File cvTarget = File.createTempFile(cv.getOriginalFilename().split(ESC_CHAR_TITIK)[0], "." + cv.getOriginalFilename().split(ESC_CHAR_TITIK)[1]);
        File fotoTarget = File.createTempFile(foto.getOriginalFilename().split(ESC_CHAR_TITIK)[0], "." + foto.getOriginalFilename().split(ESC_CHAR_TITIK)[1]);
        cv.transferTo(cvTarget);
        foto.transferTo(fotoTarget);

        logger.info("CV disimpan ke {}", cvTarget.getAbsolutePath());
        logger.info("Foto disimpan ke {}", fotoTarget.getAbsolutePath());

        if (cv.getSize() == UKURAN_FILE_CV) {
            result.put("cv", "success");
        } else {
            result.put("cv", "error size");
        }

        if (foto.getSize() == UKURAN_FILE_FOTO) {
            result.put("foto", "success");
        } else {
            result.put("foto", "error size");
        }

        if ("File Endy".equals(keterangan)) {
            result.put("keterangan", "success");
        } else {
            result.put("keterangan", "salah content");
        }

        return result;
    }

    @RequestMapping("/config/{id}")
    @ResponseBody
    public ApplicationConfig findApplicationConfigById(@PathVariable String id) {
        ApplicationConfig config = belajarRestfulService.findApplicationConfigById(id);
        if (config == null) {
            throw new IllegalStateException();
        }
        return config;
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ApplicationConfig config, HttpServletRequest request, HttpServletResponse response) {
        belajarRestfulService.save(config);
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, config.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/config/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid ApplicationConfig config) {
        ApplicationConfig a = belajarRestfulService.findApplicationConfigById(id);
        if (a == null) {
            logger.warn("Config dengan id [{}] tidak ditemukan", id);
            throw new IllegalStateException();
        }
        config.setId(a.getId());
        belajarRestfulService.save(config);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/config/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        ApplicationConfig a = belajarRestfulService.findApplicationConfigById(id);
        if (a == null) {
            logger.warn("Config dengan id [{}] tidak ditemukan", id);
            throw new IllegalStateException();
        }
        belajarRestfulService.delete(a);
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @ResponseBody
    public List<ApplicationConfig> findAll(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse response) {
        
        if (StringUtils.hasText(search)) {
            return belajarRestfulService.findApplicationConfigs(search, pageable).getContent();
        } else {
            return belajarRestfulService.findAllApplicationConfigs(pageable).getContent();
        }

    }

    @RequestMapping(value = "/config/upload", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> testUpload(@RequestParam(value = "uploadedfiles[]") List<MultipartFile> daftarFoto) throws Exception {

        logger.debug("Jumlah file yang diupload {}", daftarFoto.size());

        List<Map<String, String>> hasil = new ArrayList<Map<String, String>>();

        for (MultipartFile multipartFile : daftarFoto) {
            logger.debug("Nama File : {}", multipartFile.getName());
            logger.debug("Nama File Original : {}", multipartFile.getOriginalFilename());
            logger.debug("Ukuran File : {}", multipartFile.getSize());

            Map<String, String> keterangan = new HashMap<String, String>();
            keterangan.put("Nama File", multipartFile.getOriginalFilename());
            keterangan.put("Ukuran File", Long.valueOf(multipartFile.getSize()).toString());
            keterangan.put("Content Type", multipartFile.getContentType());
            keterangan.put("UUID", UUID.randomUUID().toString());
            File temp = File.createTempFile("xxx", "xxx");
            multipartFile.transferTo(temp);
            keterangan.put("MD5", createMD5Sum(temp));
            hasil.add(keterangan);
        }

        return hasil;
    }

    private String createMD5Sum(File file) throws Exception {
        byte[] checksum = Files.getDigest(file, MessageDigest.getInstance("MD5"));
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < checksum.length; i++) {
            result.append(Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Konfig dengan nama tersebut tidak ditemukan");
    }
}
