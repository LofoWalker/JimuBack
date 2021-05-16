package com.mochi.back.service;

import com.mochi.back.config.FileStorageProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FileServiceTest {

    private final String PATH = "/tmp/JimuTest/";

    private FileService fileService;
    private ArrayList<String> filesName;

    @Mock
    private FileStorageProperties fileStorageProperties;

    @Before
    public void setUp() throws IOException {
        Files.createDirectories(Path.of(PATH));
        this.filesName = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            new File(PATH + "file" + i + ".txt").createNewFile();
            filesName.add(PATH + "file" + i + ".txt");
        }

        initMocks(this);

        when(fileStorageProperties.getUploadDir()).thenReturn(PATH);

        fileService = new FileService(fileStorageProperties);
    }

    @Test
    public void should_return_list_of_file() {
        File file = new File(PATH);

        File[] files = file.listFiles();

        assertThat(files.length).isEqualTo(15);
    }

    @Test
    public void should_create_zip_file_then_delete_it() throws IOException {
        fileService.createAndGetZipFile(filesName);
        File[] files = new File(PATH).listFiles();

        assertThat(files.length).isEqualTo(16);

        fileService.deleteFile("MesFichiers.zip");
        files = new File(PATH).listFiles();
        assertThat(files.length).isEqualTo(15);
    }

    @After
    public void should_delete_all_files() {
        File[] files = new File(PATH).listFiles();
        Arrays.stream(files).forEach(file -> fileService.deleteFile(file.getName()));
    }

}
