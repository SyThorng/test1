package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.service.FileUploadFileService;
import com.kshrd.soccer_date.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final FileUploadFileService fileUploadFileService;

    private final ImageService imageService;

    public ImageController(FileUploadFileService fileUploadFileService, ImageService imageService) {
        this.fileUploadFileService = fileUploadFileService;
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload-image", consumes = {"multipart/form-data"})
    @Operation(summary = "Update Image")
    public ResponseEntity<Map<String,String>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Map<String,String> image=new HashMap<>();
//        image.put("image",fileUploadFileService.saveFile(file));
        image.put("image", imageService.upload(file));
        return ResponseEntity.ok().body(image);
    }

    @GetMapping("/get-image")
    @Operation(summary = "Get Image")
    public ResponseEntity<Resource> getImage(
            @RequestParam("file") String fileName
    ) {
        Path path = Paths.get("src/main/resources/images/" + fileName);
        try {
//            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.load(fileName));
        } catch (Exception e) {
            System.out.println("Error message {} " + e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }







}
