package org.poolc.api.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class DataDto {
    private Long id;
    private String fileName;
    private String fileBase64;

    @JsonCreator
    public DataDto(Long id, String fileName, String fileBase64) {
        this.id = id;
        this.fileName = fileName;
        this.fileBase64 = fileBase64;
    }
}