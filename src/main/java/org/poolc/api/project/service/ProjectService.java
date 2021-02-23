package org.poolc.api.project.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.domain.ProjectMember;
import org.poolc.api.project.repository.ProjectRepository;
import org.poolc.api.project.vo.ProjectCreateValues;
import org.poolc.api.project.vo.ProjectUpdateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    @Transactional
    public void createProject(ProjectCreateValues values) {
        Project project = new Project(values.getName(), values.getDescription(), values.getGenre(), values.getDuration(), values.getThumbnailURL(), values.getBody());
        projectRepository.save(project);
        project.setMembers(values.getMembers().stream()
                .map(m -> memberRepository.findById(m).orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다")))
                .map(member -> new ProjectMember(project, member))
                .collect(Collectors.toList()));
    }

    @Transactional
    public void updateProject(ProjectUpdateValues projectUpdateValues, Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 프로젝트가 없습니다"));
        project.update(projectUpdateValues);
        em.flush();
        project.addMembers(projectUpdateValues.getMembers().stream()
                .map(m -> new ProjectMember(project,
                        memberRepository.findById(m)
                                .orElseThrow(() -> new NoSuchElementException("해당하는 회원이 없습니다"))))
                .collect(Collectors.toList()));
    }
    
    public void deleteProject(Long projectId) {
        projectRepository.delete(projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 프로젝트가 없습니다.")));
    }

    public List<Project> findProjects() {
        return projectRepository.findAll();
    }

    public List<Member> findMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    public Project findProjectWithMember(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 프로젝트가 없습니다."));
        project.setMembers(em.createQuery("select pm from ProjectMember pm join fetch pm.member where pm.project.id=:id")
                .setParameter("id", projectId)
                .getResultList());
        return project;
    }

    private void checkMembersExist(List<String> ids) {
        ids.stream()
                .map(m -> memberRepository.findById(m).orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다")));
    }

}