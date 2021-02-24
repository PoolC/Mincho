package org.poolc.api.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
    @GetMapping("/error")
    public ResponseEntity<String> unauthenticatedHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 접근입니다");
    }
}
