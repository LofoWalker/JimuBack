package com.mochi.back.service;

import com.mochi.back.config.FileStorageProperties;
import com.mochi.back.exception.FileStorageException;
import com.mochi.back.exception.MyFileNotFoundException;
import com.mochi.back.model.JimuFile;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir());
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public ArrayList<JimuFile> getFiles() throws IOException {
        ArrayList<JimuFile> jimuFiles = new ArrayList<>();
        File[] files = new File(String.valueOf(this.fileStorageLocation)).listFiles();

        for (File tmp : files) {
            jimuFiles.add(new JimuFile(tmp.getName(), getCreationDate(tmp), Files.size(Path.of(tmp.getAbsolutePath()))));
        }

        return jimuFiles;
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(String fileName) {

        System.out.println("Delete file : " + fileName);
        // Normalize file name
        try {
            Files.delete(Path.of(fileStorageLocation +  "/" + fileName));
        } catch (IOException ex) {
            throw new MyFileNotFoundException("Could not delete file " + fileName);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Resource createAndGetZipFile(List<String> srcFiles) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileStorageLocation + "/" + "MesFichiers.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();

        return loadFileAsResource("MesFichiers.zip");
    }

    private LocalDateTime getCreationDate(File file) {
        try {
            Path filePath = Paths.get(file.getPath());
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            FileTime fileTime = attr.creationTime();
            return LocalDateTime.ofInstant( fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
