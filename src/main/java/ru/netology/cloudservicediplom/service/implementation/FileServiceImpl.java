package ru.netology.cloudservicediplom.service.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservicediplom.dto.FileDTO;
import ru.netology.cloudservicediplom.exception.*;
import ru.netology.cloudservicediplom.model.CloudFile;
import ru.netology.cloudservicediplom.repository.CloudRepository;
import ru.netology.cloudservicediplom.repository.FileRepository;
import ru.netology.cloudservicediplom.security.JWTUtil;
import ru.netology.cloudservicediplom.service.FileService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${data.file_storage.path}")
    private String path;

    private final static String PATH_FORMAT = "%s\\%s\\";

    private final JWTUtil jwtUtil;

    private final FileRepository fileRepository;

    private final CloudRepository cloudRepository;

    @PostConstruct
    private void init() {
        var checkPath = Paths.get(path);
        if (!Files.exists(checkPath)) {
            var file = new File(path);
            file.mkdir();
        }
    }


    @Override
    @Transactional
    public List<FileDTO> fileList(String token, int limit) {
        String user = jwtUtil.extractUsername(token);
        var files = fileRepository.findByUsername(user);
        return files.stream()
                .limit(limit)
                .map(this::fromCloudFileToFileDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public File getFile(String token, String filename) {
        var username = jwtUtil.extractUsername(token);
        var fullPath = fileRepository.findByUsernameAndFilename(username, filename)
                .orElseThrow(() ->
                        new CloudServiceFileNotFoundException(filename))
                .getPath();
        return new File(fullPath + "//" + filename);
    }

    @Override
    @Transactional
    public void renameFile(String token, String filename, String newFilename) {
        var username = jwtUtil.extractUsername(token);
        var file = fileRepository.findByUsernameAndFilename(username, filename)
                .orElseThrow(() ->
                        new CloudServiceFileNotFoundException(filename));
        if (cloudRepository.renameFile(filename, file.getPath(), newFilename)) {
            file.setFilename(newFilename);
            fileRepository.save(file);
        } else {
            throw new CloudServiceErrorInputData(format("File with name=[%s] not renamed", filename));
        }
    }

    @Override
    @Transactional
    public void postFile(String token, MultipartFile multipartFile, String filename) {
        var username = jwtUtil.extractUsername(token);
        var fullPath = String.format(PATH_FORMAT, path, username);
        try {
            if (cloudRepository.saveFile(multipartFile, filename, fullPath)) {
                var now = new Date(System.currentTimeMillis());
                var file = CloudFile.builder()
                        .filename(filename)
                        .path(fullPath)
                        .username(username)
                        .size(multipartFile.getBytes().length)
                        .created(now)
                        .updated(now)
                        .build();
                fileRepository.save(file);
            } else {
                throw new CloudServiceErrorUploadFile(format("File with name: [%s] not saved in FileRepository", filename));
            }
        } catch (IOException e) {
            throw new CloudServiceErrorUploadFile("File not save");
        }
    }

    @Override
    @Transactional
    public void deleteFile(String token, String filename) {
        var username = jwtUtil.extractUsername(token);
        var fullPath = format(PATH_FORMAT, path, username);
        if (cloudRepository.deleteFile(filename, fullPath)) {
            var file = fileRepository.findByUsernameAndFilename(username, filename)
                    .orElseThrow(() ->
                            new CloudServiceFileNotFoundException(filename));

            fileRepository.delete(file);
        } else {
            throw new CloudServiceErrorDeleteFile(filename);
        }
    }

    private FileDTO fromCloudFileToFileDTO(CloudFile file) {
        return FileDTO.builder()
                .filename(file.getFilename())
                .size(file.getSize())
                .build();
    }
}
