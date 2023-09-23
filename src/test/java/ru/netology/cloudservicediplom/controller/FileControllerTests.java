package ru.netology.cloudservicediplom.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.cloudservicediplom.dto.FileDTO;
import ru.netology.cloudservicediplom.service.FileService;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FileControllerTests {
    MockMvc mockMvc;

    @MockBean
    private static FileService fileService;

    private static final String FILENAME = "test.txt";

    private static final String TOKEN = "Bearer test-token";

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new FileController(fileService)).build();
    }

    @Test
    @SneakyThrows
    void fileListTest() {
        var list = List.of(FileDTO.builder().filename(FILENAME).size(10L).build());
        when(fileService.fileList(TOKEN, 1)).thenReturn(list);
        mockMvc.perform(get("/list")
                        .header("auth-token", "TOKEN")
                        .param("limit", "1")
                )
                .andExpect(status().isOk());
    }


    @Test
    @SneakyThrows
    void getFileTest() {
        String PATH = "test\\";
        when(fileService.getFile(TOKEN, FILENAME))
                .thenReturn(new File(PATH + FILENAME));

        mockMvc.perform(get("/file")
                        .header("auth-token", TOKEN)
                        .param("filename", FILENAME)
                )
                .andExpect(status().isOk());
    }


}
