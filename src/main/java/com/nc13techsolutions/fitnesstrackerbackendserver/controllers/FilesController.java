package com.nc13techsolutions.fitnesstrackerbackendserver.controllers;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.FileData;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.FilesService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;

    @PostMapping("/add")
    public ResponseEntity<String> addFile(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileType") String fileType) {

        Path resultPath = filesService
                .saveFile(new FileData(multipartFile, fileName, fileType));
        if (resultPath != null) {
            return ResponseEntity
                    .ok()
                    .header("attachment; filename=\"" + resultPath.getFileName().toString() + "\"")
                    .body(resultPath.getFileName().toString());
        }

        return ResponseEntity
                .notFound()
                .build();
    }

    @GetMapping("/images/view/{fileName}")
    public ResponseEntity<Resource> getImageFile(@PathVariable String fileName) {
        Path resultPath = filesService.getFile(new FileData(fileName, "images"));
        if (resultPath != null) {
            Resource resource = filesService.loadAsResource(resultPath);
            if (resource != null) {
                return ResponseEntity
                        .ok()
                        .header("attachment; filename=\"" + resultPath.getFileName().toString() + "\"")
                        .body(resource);
            }
        }

        return ResponseEntity
                .notFound()
                .build();

    }

    @GetMapping("/videos/view/{fileName}")
    public ResponseEntity<Resource> getVideoFile(@PathVariable String fileName) {
        Path resultPath = filesService.getFile(new FileData(fileName, "videos"));
        if (resultPath != null) {
            Resource resource = filesService.loadAsResource(resultPath);
            if (resource != null) {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                responseHeaders.add("X-Content-Type-Options", "nosniff");
                return ResponseEntity
                        .ok()
                        .header("attachment; filename=\"" + resultPath.getFileName().toString() + "\"")
                        .headers(responseHeaders)
                        .body(resource);
            }
        }

        return ResponseEntity
                .notFound()
                .build();

    }

    @DeleteMapping("/images/{fileName}")
    public int deleteImageFile(@PathVariable String fileName) {
        return filesService.deleteFile(new FileData(fileName, "images"));

    }

    @DeleteMapping("/videos/{fileName}")
    public int deleteVideoFile(@PathVariable String fileName) {
        return filesService.deleteFile(new FileData(fileName, "videos"));
    }
}
