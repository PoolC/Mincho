package org.poolc.api.project.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.project.dto.ProjectMemberResponse;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.dto.ProjectWithMemberResponse;
import org.poolc.api.project.service.ProjectService;
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
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ProjectResponse>>> findProjects() {
        HashMap<String, List<ProjectResponse>> responseBody = new HashMap<>();
        responseBody.put("data", projectService.findProjects().stream()
                .map(p -> new ProjectResponse(p))
                .collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, ProjectWithMemberResponse>> findOneProject(@PathVariable("projectID") Long id) {
        HashMap<String, ProjectWithMemberResponse> responseBody = new HashMap<>();
        responseBody.put("data", new ProjectWithMemberResponse(projectService.findProjectWithMember(id)));
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ProjectMemberResponse>>> findMembersForProject(HttpServletRequest request) {
        checkAdmin(request);
        HashMap<String, List<ProjectMemberResponse>> responseBody = new HashMap<>();
        responseBody.put("data", projectService.findMembersByName(request.getParameter("name"))
                .stream()
                .map(m -> new ProjectMemberResponse(m)).collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void checkAdmin(HttpServletRequest request) {
        if (request.getAttribute("isAdmin").equals("false")) {
            throw new UnauthenticatedException("임원진이 아닙니다");
        }
    }
}
