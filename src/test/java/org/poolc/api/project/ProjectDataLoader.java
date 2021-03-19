package org.poolc.api.project;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.repository.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("projectTest")
@RequiredArgsConstructor
public class ProjectDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void run(String... args) {
        projectRepository.save(new Project("첫 플젝",
                "풀씨 프로젝트",
                "게임",
                "3개월",
                "http://naver.com",
                "ds"));

        projectRepository.save(new Project("두번째 플젝",
                "풀씨 프로젝트",
                "게임",
                "3개월",
                "http://naver.com",
                "ds"));
    }

}
