package ru.netology.cloudservicediplom.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservicediplom.dto.FileDTO;

import java.io.File;
import java.util.List;

public interface FileService {

    List<FileDTO> fileList(String token, int limit);

    File getFile(String token, String fileName);

    void renameFile(String token, String fileName, String newName);

    void postFile(String token, MultipartFile file, String fileName);

    void deleteFile(String token, String fileName);
}
