package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.Project;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.repository.MemberRepository;
import poolc.poolc.repository.ProjectRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    void 신규등록() {
        Project project = new Project();
        project.setName("예시 프로젝트");
        project.setDescription("레포 테스트용 플젝");
        project.setGenre("1");
        project.setDuration("1");
        project.setBody("11");
        project.setThumbnailURL("!111");
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        ProjectMember projectMember = new ProjectMember();
        ProjectMember projectMember2 = new ProjectMember();
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        memberRepository.save(member);

        projectMember.setMember(member);
        projectMember2.setMember(member);
        projectMember.setProject(project);
        projectMember2.setProject(project);
        project.getMembers().add(projectMember);
        project.getMembers().add(projectMember2);

        projectRepository.save(project);
        ProjectMember projectMember3 = new ProjectMember();
        projectMember3.setMember(member);
        projectMember3.setProject(project);
        project.getMembers().add(projectMember3);

    }

    @Test
    @Rollback(false)
    @Transactional
    void 삭제() {
        Project project = new Project();
        project.setName("예시 프로젝트");
        project.setDescription("레포 테스트용 플젝");
        project.setGenre("1");
        project.setDuration("1");
        project.setBody("11");
        project.setThumbnailURL("!111");
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        memberRepository.save(member);
        ProjectMember projectMember = new ProjectMember();
        ProjectMember projectMember2 = new ProjectMember();
        projectMember.setMember(member);
        projectMember.setProject(project);
        project.getMembers().add(projectMember);
        projectRepository.save(project);
        em.flush();
        em.clear();
//        memberRepository.delete(memberRepository.findOne(member.getUUID()));
        Project project1 = projectRepository.findOne(project.getId());
        em.remove(project1);
    }
}