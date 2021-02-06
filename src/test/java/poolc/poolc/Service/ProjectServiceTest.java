package poolc.poolc.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.Project;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.service.ProjectService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    ProjectService projectService;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void 프로젝트등록() {
        Project project = new Project();
        project.setName("새 프로젝트");
        project.setGenre("게임");
        project.setDuration("2020~2021");
        project.setThumbnailURL("썸네일");
        project.setBody("안녕");
        project.setDescription("조아");
        projectService.saveProject(project);
    }

    @Test
    public void 프로젝트삭제() {
        Project project = new Project();
        project.setName("새 프로젝트");
        project.setGenre("게임");
        project.setDuration("2020~2021");
        project.setThumbnailURL("썸네일");
        project.setBody("안녕");
        project.setDescription("조아");
        projectService.saveProject(project);
        em.flush();
        em.clear();
        projectService.deleteProject(project.getId());
        assertEquals(Optional.empty(), projectService.findProjectWithMember(project.getId()));
    }

    @Test
    public void 없는프로젝트삭제() {
        assertThrows(IllegalStateException.class, () -> {
            projectService.deleteProject(1l);
        });
    }

    @Test
    @Rollback(false)
    public void 프로젝트전부조회() {
        Project project = new Project();
        project.setName("새 프로젝트");
        project.setGenre("게임");
        project.setDuration("2020~2021");
        project.setThumbnailURL("썸네일");
        project.setBody("안녕");
        project.setDescription("조아");
        projectService.saveProject(project);
        Project project2 = new Project();
        project2.setName("새 프로젝트2");
        project2.setGenre("게임");
        project2.setDuration("2020~2021");
        project2.setThumbnailURL("썸네일");
        project2.setBody("안녕");
        project2.setDescription("조아");
        projectService.saveProject(project2);
        em.flush();
        em.clear();
        List<Project> projects = projectService.findProjects();
        assertEquals(2, projects.size());
    }

    @Test
    @Rollback(false)
    public void 프로젝트하나조회() {
        Project project = new Project();
        project.setName("새 프로젝트");
        project.setGenre("게임");
        project.setDuration("2020~2021");
        project.setThumbnailURL("썸네일");
        project.setBody("안녕");
        project.setDescription("조아");
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        em.persist(member);
        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(member);
        projectMember.setProject(project);
        project.addMember(projectMember);
        member.getProjects().add(projectMember);
        projectService.saveProject(project);
        em.flush();
        em.clear();
        Optional<Project> projectWithMember = projectService.findProjectWithMember(project.getId());
        assertEquals(project.getMembers().get(0), projectWithMember.get().getMembers().get(0));


    }


}
