package com.mochi.back.controller;

import com.mochi.back.model.JimuFile;
import com.mochi.back.model.UploadFileResponse;
import com.mochi.back.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    @Autowired
    FileController(FileService fileService) {
        logger.info("Initiate FileController");

        this.fileService = fileService;
    }

    @GetMapping("/files")
    public List<JimuFile> filesName() throws IOException {
        logger.info("Send all files name in directory");

        return fileService.getFiles();
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        logger.info("Send file to front for download");

        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/file")
    public UploadFileResponse newFile(@RequestParam("file") MultipartFile file) {
        logger.info("Receive file from front : " + file.getName());

        String fileName = fileService.storeFile(file);

        return new UploadFileResponse(fileName, file.getContentType(), file.getSize());
    }

    @PostMapping("/files")
    public List<UploadFileResponse> newFiles(@RequestParam("files") MultipartFile[] files) {
        logger.info("Receive multiple files");

        return Arrays.stream(files)
                .map(this::newFile)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/file/{fileName}")
    public void file(@PathVariable String fileName) {
        logger.info("Delete file : " + fileName);

        fileService.deleteFile(fileName);
    }
}
