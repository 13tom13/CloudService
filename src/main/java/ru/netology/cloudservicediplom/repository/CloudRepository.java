package ru.netology.cloudservicediplom.repository;

import lombok.Cleanup;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservicediplom.exception.CloudServiceErrorUploadFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;

@Repository
public class CloudRepository {

    public boolean saveFile(MultipartFile multipartFile, String fileName, String path) throws IOException {
        var file = new File(path + fileName);
        if (file.exists() || multipartFile.isEmpty()) {
            return false;
        } else {


            var checkPath = Paths.get(path);
            if (!Files.exists(checkPath)) {
                var dir = new File(path);
                dir.mkdir();
            }

            byte[] bytes = multipartFile.getBytes();
            try (BufferedOutputStream stream =
                         new BufferedOutputStream(new FileOutputStream(file))) {
                stream.write(bytes);
            } catch (RuntimeException e) {
                throw new CloudServiceErrorUploadFile(
                        format("File with name=[%s] not saved in CloudRepository", fileName));
            }
            @Cleanup BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(bytes);
            return true;
        }
    }

    public boolean deleteFile(String fileName, String path) {
        var file = new File(path + fileName);
        if (!file.exists()) return true;
        return file.delete();
    }

    public boolean renameFile(String fileName, String path, String newName) {
        var file = new File(path + fileName);
        if (!file.exists()) return false;
        return file.renameTo(new File(path + "//" + newName));
    }


}
