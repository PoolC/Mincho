package org.poolc.api.project.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.repository.ProjectRepository;
import org.poolc.api.project.vo.ProjectCreateValues;
import org.poolc.api.project.vo.ProjectUpdateValues;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;

    public void createProject(ProjectCreateValues values) {
        checkMembersExist(values.getMemberUUIDs());

        Project project = new Project(values.getName(), values.getDescription(), values.getGenre(), values.getDuration(), values.getThumbnailURL(), values.getBody());
        project.setMembers(values.getMemberUUIDs());

        projectRepository.save(project);
    }

    public Project findOne(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 프로젝트가 없습니다."));
    }

    public List<Project> findProjects() {
        return projectRepository.findAll();
    }

    public void updateProject(ProjectUpdateValues projectUpdateValues, Long projectID) {
        Project project = findOne(projectID);
        project.update(projectUpdateValues);
    }

    public void deleteProject(Long projectId) {
        projectRepository.delete(findOne(projectId));
    }

    private void checkMembersExist(List<String> memberUUIDs) {
        memberUUIDs.forEach(memberService::findMemberbyUUID);
    }
}