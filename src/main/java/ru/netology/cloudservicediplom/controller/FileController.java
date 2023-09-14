package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class FileController {

    @PostMapping("/file")
    public ResponseEntity<?> postFile(@RequestParam("filename") String filename) {
        return null;
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        return null;
    }


    @GetMapping(value = "/file")
    public ResponseEntity<?> getFile(@RequestParam("filename") String filename) throws IOException {
        return null;
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<?> putFile(@RequestParam("filename") String filename) {
        return null;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList() {
        return null;
    }
}
