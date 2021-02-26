package org.poolc.api.activity.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.ActivityMember;
import org.poolc.api.activity.dto.*;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.service.SessionService;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.activity.vo.SessionUpdateValues;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final SessionService sessionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<ActivityResponse>>> findActivities() {
        return ResponseEntity.ok().body(Collections.singletonMap("data", activityService.findActivities().stream()
                .map(ActivityResponse::of)
                .collect(toList())));
    }

    @GetMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ActivityResponse>> findOneActivity(@PathVariable("activityID") Long id) {
        return ResponseEntity.ok().body(Collections.singletonMap("data", ActivityResponse.of(activityService.findOneActivity(id))));
    }

    @GetMapping(value = "/session/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<SessionResponse>>> findSessions(@AuthenticationPrincipal Member member, @PathVariable("activityID") Long id) {
        HashMap<String, List<SessionResponse>> responseBody = new HashMap<>();
        List<SessionResponse> list = new ArrayList<>();
        list.addAll(sessionService.findSessionsByActivityID(member, id).stream()
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

    @GetMapping(value = "/check/{sessionID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<AttendanceCheckResponse>>> getAttendanceCheck(HttpServletRequest request, @PathVariable("sessionID") Long id) {
        HashMap<String, List<AttendanceCheckResponse>> responseBody = new HashMap<>();
        List<AttendanceCheckResponse> list = new ArrayList<>();
        Map<ActivityMember, Boolean> c = activityService.findActivityMembersWithAttendance(id);
        list.addAll(c.entrySet().stream()
                .map(e -> new AttendanceCheckResponse(e.getKey(), e.getValue())).collect(toList()));
        responseBody.put("data", list);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivity(@AuthenticationPrincipal Member member, @RequestBody ActivityCreateRequest requestBody) {
        activityService.createActivity(new ActivityCreateValues(requestBody), member);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/session")
    public ResponseEntity<Void> addSession(@AuthenticationPrincipal Member member, @RequestBody SessionCreateRequest requestBody) {
        sessionService.createSession(member, new SessionCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/apply/{activityID}")
    public ResponseEntity<String> applyToActivity(@AuthenticationPrincipal Member member, @PathVariable("activityID") Long id) {
        activityService.apply(id, member.getUUID());
        return ResponseEntity.ok().body("수강신청에 성공하였습니다.");
    }

    @PostMapping(value = "/check")
    public ResponseEntity<String> attendanceCheck(HttpServletRequest request, @RequestBody AttendanceRequest requestBody) {
        sessionService.attend(request.getAttribute("UUID").toString(), requestBody);
        return ResponseEntity.ok().body("출석체크에 성공하였습니다");
    }

    @DeleteMapping(value = "/{activityID}")
    public ResponseEntity<Void> deleteActivity(@AuthenticationPrincipal Member member, @PathVariable("activityID") Long id) {
        activityService.deleteActivity(id, member);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateActivity(@AuthenticationPrincipal Member member, @RequestBody ActivityUpdateRequest requestBody, @PathVariable("activityID") Long id) {
        activityService.updateActivity(member, id, new ActivityUpdateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/session/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateSession(@AuthenticationPrincipal Member member, @RequestBody SessionUpdateRequest requestBody, @PathVariable("activityID") Long id) {
        sessionService.updateSession(id, new SessionUpdateValues(requestBody, member.getUUID()));
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
