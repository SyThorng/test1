package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {

    private final Path root = Paths.get("src/main/resources/images/");

    @Value("${image.url}")
    private String url;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return  filename;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new RuntimeException("Could not read the file!");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
