package org.poolc.api.project.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.domain.ProjectMember;
import org.poolc.api.project.repository.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("projectTest")
@RequiredArgsConstructor
public class ProjectDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final ProjectRepository projectRepository;

    @Override
    public void run(String... args) {
        Member member = new Member(UUID.randomUUID().toString(),
                "MEMBER_ID",
                passwordHashProvider.encodePassword("MEMBER_PASSWORD"),
                "example@email.com",
                "examplePhoneNumber",
                "MEMBER_NAME",
                "exampleDepartment",
                "exampleStudentID",
                true,
                true,
                null,
                null,
                null,
                null,
                false,
                null);
        Member member2 = new Member(UUID.randomUUID().toString(),
                "MEMBER_ID2",
                passwordHashProvider.encodePassword("MEMBER_PASSWORD2"),
                "example@email.com2",
                "examplePhoneNumber2",
                "MEMBER_NAME2",
                "exampleDepartment2",
                "exampleStudentID2",
                true,
                false,
                null,
                null,
                null,
                null,
                false,
                null);
        memberRepository.save(member);
        memberRepository.save(member2);
        Project project = new Project("첫 플젝",
                "풀씨 프로젝트",
                "게임",
                "3개월",
                "http://naver.com",
                "ds");
        ProjectMember projectMember = new ProjectMember(project, member);
        ProjectMember projectMember2 = new ProjectMember(project, member2);
        List<ProjectMember> projectMemberList = new ArrayList<>();
        projectMemberList.add(projectMember);
        projectMemberList.add(projectMember2);
        project.setMembers(projectMemberList);
        projectRepository.save(project);
        projectRepository.save(new Project("두번째 플젝",
                "풀씨 프로젝트",
                "게임",
                "3개월",
                "http://naver.com",
                "ds"));
        memberRepository.save(
                new Member(UUID.randomUUID().toString(),
                        "MEMBER_ID3",
                        passwordHashProvider.encodePassword("MEMBER_PASSWORD3"),
                        "example@email.com3",
                        "examplePhoneNumber3",
                        "MEMBER_NAME",
                        "exampleDepartment",
                        "exampleStudentID3",
                        true,
                        false,
                        null,
                        null,
                        null,
                        null,
                        false,
                        null));
    }

}
