package poolc.poolc.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Project;
import poolc.poolc.service.ProjectService;

import javax.persistence.EntityManager;


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

}
