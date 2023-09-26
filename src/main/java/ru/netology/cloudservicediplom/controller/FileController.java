package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservicediplom.dto.FileDTO;
import ru.netology.cloudservicediplom.dto.NewFileNameDTO;
import ru.netology.cloudservicediplom.service.FileService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<List<FileDTO>> fileList(@RequestHeader("auth-token") String authToken,
                                                  @RequestParam("limit") int limit) {
        return new ResponseEntity<>(fileService.fileList(authToken, limit), HttpStatus.OK);
    }

    @PostMapping("/file")
    public ResponseEntity<?> postFile(@RequestHeader("auth-token") String authToken,
                                      @RequestParam("filename") String filename, MultipartFile file) {
        fileService.postFile(authToken, file, filename);
        log.info(format("File [%s] uploaded", filename));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String fileName) {
        fileService.deleteFile(authToken, fileName);
        log.info(format("File [%s] deleted", fileName));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/file")
    @SneakyThrows
    public ResponseEntity<byte[]> getFile(@RequestHeader("auth-token") String authToken,
                                          @RequestParam("filename") String fileName) {
        var file = fileService.getFile(authToken, fileName);
        var path = Paths.get(file.getAbsolutePath());
        var bytes = Files.readAllBytes(path);
        var probeContentType = Files.probeContentType(path);
        log.info(format("File [%s] has been sent", fileName));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(file.getName()).build().toString())
                .contentType(probeContentType != null ? MediaType.valueOf(probeContentType) : MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> renameFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String fileName,
                                        @RequestBody NewFileNameDTO newFileName) {
        fileService.renameFile(authToken, fileName, newFileName.getNewName());
        log.info(format("File [%s] has been renamed (new name: %s)", fileName, newFileName.getNewName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
