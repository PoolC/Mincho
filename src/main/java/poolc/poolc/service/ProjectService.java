package poolc.poolc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Project;
import poolc.poolc.repository.MemberRepository;
import poolc.poolc.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final MemberRepository memberRepository;

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> findProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectWithMember(Long projectId) {
        return Optional.ofNullable(projectRepository.findOneWithMembers(projectId));
    }
}
