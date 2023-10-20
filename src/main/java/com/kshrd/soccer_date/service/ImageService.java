package com.kshrd.soccer_date.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface ImageService {

    String upload(MultipartFile file) throws IOException;
    Resource load(String filename);

}
