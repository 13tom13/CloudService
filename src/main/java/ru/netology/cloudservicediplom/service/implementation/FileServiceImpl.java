package ru.netology.cloudservicediplom.service.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservicediplom.dto.FileDTO;
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
                .orElseThrow(() -> new RuntimeException(format("File with name=[%s] not found.", filename)))
                .getPath();
        return new File(fullPath + "//" + filename);
    }

    @Override
    @Transactional
    public void renameFile(String token, String fileName, String newFilename) {
        var username = jwtUtil.extractUsername(token);
        var file = fileRepository.findByUsernameAndFilename(username, fileName)
                .orElseThrow(() -> new RuntimeException(format("File with name=[%s] not found.", fileName)));
        if (cloudRepository.renameFile(fileName, file.getPath(), newFilename)) {
            file.setFilename(newFilename);
            fileRepository.save(file);
        } else {
            throw new RuntimeException("Exception, the file is not renamed");
        }
    }

    @Override
    @Transactional
    public boolean postFile(String token, MultipartFile multipartFile, String filename) {
        var username = jwtUtil.extractUsername(token);
        var fullPath = String.format(PATH_FORMAT, path, username);
        boolean fileSavedToStorage;
        try {
            fileSavedToStorage = cloudRepository.saveFile(multipartFile, filename, fullPath);
            if (fileSavedToStorage) {
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
                throw new RuntimeException("File not save");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("File not save");
        }
        return fileSavedToStorage;
    }

    @Override
    @Transactional
    public void deleteFile(String token, String filename) {
        var username = jwtUtil.extractUsername(token);
        var fullPath = format(PATH_FORMAT, path, username);
        if (cloudRepository.deleteFile(filename, fullPath)) {
            var file = fileRepository.findByUsernameAndFilename(username, filename)
                    .orElseThrow(() -> new RuntimeException(format("File with name=[%s] not found.", filename)));
            fileRepository.delete(file);
        }
    }

    private FileDTO fromCloudFileToFileDTO(CloudFile file) {
        return FileDTO.builder()
                .filename(file.getFilename())
                .size(file.getSize())
                .build();
    }
}
