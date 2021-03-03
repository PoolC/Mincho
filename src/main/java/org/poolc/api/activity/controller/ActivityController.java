package org.poolc.api.activity.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.*;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.service.SessionService;
import org.poolc.api.activity.vo.*;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final SessionService sessionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<ActivityResponse>>> findActivities(@RequestParam Optional<String> when) {
        Map<String, List<ActivityResponse>> result = new HashMap<>();
        when.ifPresentOrElse(
                (val)
                        -> {
                    result.put("data", activityService.findActivitiesInSemester(val).stream()
                            .map(ActivityResponse::of)
                            .collect(toList()));
                },
                () -> {
                    result.put("data", activityService.findActivities().stream()
                            .map(ActivityResponse::of)
                            .collect(toList()));
                }
        );
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<String>>> getYears() {
        return ResponseEntity.ok().body(Collections.singletonMap("data", activityService.findActivityYears().stream().map(a -> a.toString()).distinct().sorted(Comparator.reverseOrder()).collect(toList())));
    }

    @GetMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ActivityResponse>> findOneActivity(@PathVariable("activityID") Long id) {
        return ResponseEntity.ok().body(Collections.singletonMap("data", ActivityResponse.of(activityService.findOneActivity(id))));
    }

    @GetMapping(value = "/session/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<SessionResponse>>> findSessions(@PathVariable("activityID") Long id) {
        return ResponseEntity.ok().body(Collections.singletonMap("data", sessionService.findSessionsByActivityID(id).stream()
                .map(SessionResponse::of)
                .collect(toList())));
    }

    @GetMapping(value = "/onesession/{sessionID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SessionResponse> findOneSession(@PathVariable("sessionID") Long id) {
        return ResponseEntity.ok().body(SessionResponse.of(sessionService.findOneSessionByID(id)));
    }

    @GetMapping(value = "/member/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<MemberResponse>>> getActivityMembers(@PathVariable("activityID") Long id) {
        return ResponseEntity.ok().body(Collections.singletonMap("data", activityService.findActivityMembers(id).stream()
                .map(MemberResponse::of)
                .collect(toList())));
    }

    @GetMapping(value = "/check/{sessionID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<AttendanceResponse>>> getAttendanceCheck(@PathVariable("sessionID") Long id) {
        HashMap<String, List<AttendanceResponse>> responseBody = new HashMap<>();
        List<AttendanceResponse> list = new ArrayList<>();
        Map<Member, Boolean> c = activityService.findActivityMembersWithAttendance(id);
        list.addAll(c.entrySet().stream()
                .map(e -> new AttendanceResponse(e.getKey(), e.getValue())).collect(toList()));
        responseBody.put("data", list);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivity(@AuthenticationPrincipal Member member, @RequestBody ActivityRequest requestBody) {
        activityService.createActivity(new ActivityCreateValues(requestBody), member);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/session")
    public ResponseEntity<SessionResponse> addSession(@AuthenticationPrincipal Member member, @RequestBody SessionCreateRequest requestBody) {
        return ResponseEntity.ok().body(SessionResponse.of(sessionService.createSession(member, new SessionCreateValues(requestBody))));
    }

    @PostMapping(value = "/apply/{activityID}")
    public ResponseEntity<String> applyToActivity(@AuthenticationPrincipal Member member, @PathVariable("activityID") Long id) {
        activityService.apply(id, member.getUUID());
        return ResponseEntity.ok().body("수강신청에 성공하였습니다.");
    }

    @PostMapping(value = "/check")
    public ResponseEntity<String> attendanceCheck(@AuthenticationPrincipal Member member, @RequestBody AttendanceRequest requestBody) {
        sessionService.attend(member.getUUID(), new AttendanceValues(requestBody));
        return ResponseEntity.ok().body("출석체크에 성공하였습니다");
    }

    @DeleteMapping(value = "/{activityID}")
    public ResponseEntity<Void> deleteActivity(@AuthenticationPrincipal Member member, @PathVariable("activityID") Long id) {
        activityService.deleteActivity(id, member);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{activityID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateActivity(@AuthenticationPrincipal Member member, @RequestBody ActivityRequest requestBody, @PathVariable("activityID") Long id) {
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
    public ResponseEntity<String> notAllowedException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
