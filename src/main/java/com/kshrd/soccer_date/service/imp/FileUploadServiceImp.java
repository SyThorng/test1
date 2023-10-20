package com.kshrd.soccer_date.service.imp;


import com.kshrd.soccer_date.service.FileUploadFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
@AllArgsConstructor
public class FileUploadServiceImp implements FileUploadFileService {
    @Override
    public String saveFile(MultipartFile file) {
        Path path = Paths.get("src/main/resources/images");
        String fileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();

        fileName = uuid + ".png";
        Path resolvePath = path;

        if(!fileName.isEmpty()){
            resolvePath = path.resolve(fileName);
        }
        try {
            Files.copy(file.getInputStream(), resolvePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println("Error message {} " + e.getMessage());
        }
        return fileName;
    }
}
