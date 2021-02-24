package org.poolc.api.activity.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.*;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.service.SessionService;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.activity.vo.SessionUpdateValues;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final SessionService sessionService;

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
        responseBody.put("data", new OneActivityResponse(activityService.findOneActivity(id)));
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/session/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<SessionResponse>>> findSessions(HttpServletRequest request, @PathVariable("activityID") Long id) {
        HashMap<String, List<SessionResponse>> responseBody = new HashMap<>();
        List<SessionResponse> list = new ArrayList<>();
        list.addAll(sessionService.findSessionsByActivityID(request.getAttribute("UUID").toString(), id).stream()
                .map(s -> new SessionResponse(s)).collect(toList()));
        responseBody.put("data", list);
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/member/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ActivityMemberResponse>>> getActivityMembers(HttpServletRequest request, @PathVariable("activityID") Long id) {
        HashMap<String, List<ActivityMemberResponse>> responseBody = new HashMap<>();
        List<ActivityMemberResponse> list = new ArrayList<>();
        list.addAll(activityService.findActivityMembers(id).stream()
                .map(m -> new ActivityMemberResponse(m)).collect(toList()));
        responseBody.put("data", list);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivity(HttpServletRequest request, @RequestBody ActivityCreateRequest requestBody) {
        activityService.createActivity(new ActivityCreateValues(requestBody, request.getAttribute("UUID").toString()));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/session")
    public ResponseEntity<Void> addSession(HttpServletRequest request, @RequestBody SessionCreateRequest requestBody) {
        sessionService.createSession(request.getAttribute("UUID").toString(), new SessionCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/apply/{activityID}")
    public ResponseEntity<String> applyToActivity(HttpServletRequest request, @PathVariable("activityID") Long id) {
        activityService.apply(id, request.getAttribute("UUID").toString());
        return ResponseEntity.ok().body("수강신청에 성공하였습니다.");
    }

    @DeleteMapping(value = "/{activityID}")
    public ResponseEntity<Void> deleteActivity(HttpServletRequest request, @PathVariable("activityID") Long id) {
        activityService.deleteActivity(id, request.getAttribute("UUID").toString(), request.getAttribute("isAdmin").toString());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateActivity(HttpServletRequest request, @RequestBody ActivityUpdateRequest requestBody, @PathVariable("activityID") Long id) {
        activityService.updateActivity(request.getAttribute("isAdmin").toString(), id, new ActivityUpdateValues(requestBody, request.getAttribute("UUID").toString()));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/session/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateSession(HttpServletRequest request, @RequestBody SessionUpdateRequest requestBody, @PathVariable("activityID") Long id) {
        sessionService.updateSession(id, new SessionUpdateValues(requestBody, request.getAttribute("UUID").toString()));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UnauthenticatedException.class})
    public ResponseEntity<String> notAllowdException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
