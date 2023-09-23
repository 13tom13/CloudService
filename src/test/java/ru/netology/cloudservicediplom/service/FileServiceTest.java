package ru.netology.cloudservicediplom.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.cloudservicediplom.model.CloudFile;
import ru.netology.cloudservicediplom.repository.CloudRepository;
import ru.netology.cloudservicediplom.repository.FileRepository;
import ru.netology.cloudservicediplom.security.JWTUtil;
import ru.netology.cloudservicediplom.service.implementation.FileServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileServiceTest {

    private static FileService fileService;

    private static final String TEST = "token test";
    private static final String USER = "user";
    private static final Date NOW = new Date(System.currentTimeMillis());
    private static final CloudFile CLOUD_FILE_TEST = new CloudFile(1L, NOW, NOW, "test", USER, "path", 10L);
    private static final List<CloudFile> CLOUD_FILES = List.of(
            new CloudFile(1L, NOW, NOW, "name1", USER, "path", 100L),
            new CloudFile(2L, NOW, NOW, "name2", USER, "path", 150L)
    );

    @BeforeAll
    public static void init() {
        var fileRepository = Mockito.mock(FileRepository.class);
        var JWTUtil = Mockito.mock(JWTUtil.class);
        var cloudRepository = Mockito.mock(CloudRepository.class);
        Mockito.when(fileRepository.findByUsername(USER))
                .thenReturn(CLOUD_FILES);
        Mockito.when(JWTUtil.extractUsername(TEST)).thenReturn(USER);
        Mockito.when(fileRepository.findByUsernameAndFilename(USER, "test"))
                .thenReturn(Optional.of(CLOUD_FILE_TEST));
        fileService = new FileServiceImpl(JWTUtil, fileRepository, cloudRepository);
    }

    @Test
    void fileListTest() {
        var filesDTO = fileService.fileList(TEST, 3);
        assertEquals(filesDTO.size(), 2);
        assertEquals("name1", filesDTO.get(0).getFilename());
        assertEquals("name2", filesDTO.get(1).getFilename());
    }

    @Test
    void getFileTest() {
        var javaFile = fileService.getFile(TEST, "test");
        assertEquals("test", javaFile.getName());
        assertEquals("path\\test", javaFile.getPath());
    }

}
