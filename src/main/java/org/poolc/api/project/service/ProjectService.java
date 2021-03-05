package org.poolc.api.project.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.repository.ProjectRepository;
import org.poolc.api.project.vo.ProjectCreateValues;
import org.poolc.api.project.vo.ProjectUpdateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;

    @Transactional
    public void createProject(ProjectCreateValues values) {
        checkMembersExist(values.getMemberLoginIDs());

        Project project = new Project(values.getName(), values.getDescription(), values.getGenre(), values.getDuration(), values.getThumbnailURL(), values.getBody());
        project.setMembers(values.getMemberLoginIDs());

        projectRepository.save(project);
    }

    public Project findOne(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 프로젝트가 없습니다."));
    }

    public List<Project> findProjects() {
        return projectRepository.findAll();
    }

    public List<Project> findProjectsByProjectMembers(String loginId) {
        return projectRepository.findProjectsByProjectMembers(loginId);
    }

    @Transactional
    public void updateProject(ProjectUpdateValues values, Long projectID) {
        Project project = findOne(projectID);
        checkMembersExist(values.getMemberLoginIDs());
        project.update(values);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.delete(findOne(projectId));
    }

    private void checkMembersExist(List<String> memberLoginIDs) {
        memberLoginIDs.forEach(memberService::findMemberbyLoginID);
    }
}