package org.poolc.api.activity.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.activity.service.ActivityService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ActivityResponse>>> findBooks() {
        HashMap<String, List<ActivityResponse>> responseBody = new HashMap<>();
        responseBody.put("data", activityService.findActivities().stream()
                .map(a -> new ActivityResponse(a))
                .collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }
}
