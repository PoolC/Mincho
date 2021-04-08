package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.SessionUpdateRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SessionUpdateValues {
    private final String uuid;
    private final LocalDate date;
    private final String description;
    private final List<String> fileList;

    public SessionUpdateValues(SessionUpdateRequest request, String uuid) {
        this.date = request.getDate();
        this.description = request.getDescription();
        this.uuid = uuid;
        this.fileList = request.getFileList();
    }
}
