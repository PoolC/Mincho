package org.poolc.api.project.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.dto.RegisterProjectRequest;
import org.poolc.api.project.dto.UpdateProjectRequest;
import org.poolc.api.project.service.ProjectService;
import org.poolc.api.project.vo.ProjectCreateValues;
import org.poolc.api.project.vo.ProjectUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewProject(@RequestBody RegisterProjectRequest requestBody) {
        projectService.createProject(new ProjectCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ProjectResponse>>> findProjects() {
        HashMap<String, List<ProjectResponse>> responseBody = new HashMap<>() {{
            put("data", projectService.findProjects().stream()
                    .map(ProjectResponse::of)
                    .collect(Collectors.toList()));
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectResponse> findOneProject(@PathVariable Long projectID) {
        return ResponseEntity.ok().body(ProjectResponse.of(projectService.findOne(projectID)));
    }

    @GetMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<ProjectResponse>>> findMembersForProject(@RequestParam String name) {
        HashMap<String, List<ProjectResponse>> responseBody = new HashMap<>() {{
            put("data", Collections.emptyList());
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @PutMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProject(@RequestBody UpdateProjectRequest requestBody, @PathVariable Long projectID) {
        projectService.updateProject(new ProjectUpdateValues(requestBody), projectID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteOneProject(@PathVariable Long projectID) {
        projectService.deleteProject(projectID);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
