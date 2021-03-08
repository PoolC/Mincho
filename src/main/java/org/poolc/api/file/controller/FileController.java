package org.poolc.api.file.controller;

import io.restassured.internal.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    @Value("${file.file-dir}")
    private String fileDir;

    @Value("${file.fetch-url}")
    private String fetchUrl;

    @GetMapping(value = "/{fileName}")
    public ResponseEntity sendFile(@PathVariable(name = "fileName") String fileName) {
        String filePath = fileDir + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                InputStream inputStream = new FileInputStream(filePath);
                byte[] out = IOUtils.toByteArray(inputStream);
                return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                        .body(out);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 파일입니다");
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@ModelAttribute MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일을 찾을수 없습니다");
            }
            String fileNameWithOutSpace = file.getOriginalFilename().replace(' ', '-');
            Path path = Paths.get(fileDir + fileNameWithOutSpace);
            if (Files.exists(path)) {
                return ResponseEntity.badRequest().body("이미 존재하는 파일명입니다. 파일 명을 수정해주세요");
            }
            file.transferTo(path);
            return ResponseEntity.ok().body(fetchUrl + "file/" + URLEncoder.encode(fileNameWithOutSpace, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
