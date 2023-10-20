package com.kshrd.soccer_date.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadFileService {
    String saveFile(MultipartFile file);
}
