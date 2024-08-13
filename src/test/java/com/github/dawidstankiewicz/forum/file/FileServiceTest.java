package com.github.dawidstankiewicz.forum.file;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    public static final String TARGET_TMP = "target/tmp/";
    @Mock
    private MultipartFile multipartFile;
    private FileService fileService;

    @Before
    public void setUp() {
        fileService = new FileService(TARGET_TMP);
    }

    @Test
    public void shouldSaveFile() {
        //given
        String subdir = "/test/";
        doReturn("someImage.jpg").when(multipartFile).getOriginalFilename();
        //when
        File result = fileService.saveFile(multipartFile, subdir);
        //then
        assertTrue(result.exists());
        File targetDirectory = new File(TARGET_TMP + subdir);
        assertTrue(targetDirectory.exists());
        assertNotNull(targetDirectory.listFiles());
        assertEquals(1, targetDirectory.listFiles().length);
        //clean up
        Stream.of(targetDirectory.listFiles()).forEach(file -> {
            System.out.println(file.delete());
        });
    }

}
