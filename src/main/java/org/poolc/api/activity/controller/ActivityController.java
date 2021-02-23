package org.poolc.api.activity.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.ActivityCreateRequest;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.activity.dto.OneActivityResponse;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ActivityResponse>>> findActivities() {
        HashMap<String, List<ActivityResponse>> responseBody = new HashMap<>();
        responseBody.put("data", activityService.findActivities().stream()
                .map(a -> new ActivityResponse(a))
                .collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, OneActivityResponse>> findOneActivity(@PathVariable("activityID") Long id) {
        HashMap<String, OneActivityResponse> responseBody = new HashMap<>();
        System.out.println(id);
        responseBody.put("data", new OneActivityResponse(activityService.findOneActivity(id)));
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivity(HttpServletRequest request, @RequestBody ActivityCreateRequest requestBody) {
        activityService.createActivity(new ActivityCreateValues(requestBody, request.getAttribute("UUID").toString()));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
