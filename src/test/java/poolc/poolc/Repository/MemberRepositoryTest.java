package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() {
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        //when
        memberRepository.save(member);

        //given
        Optional<Member> findMember = memberRepository.findById(member.getUUID());
        member.equals(findMember);
    }


    @Test
    public void 모든회원조회() {
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member1 = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        Member member2 = new Member("2", "ppp", "anfr2520@gmail.com", "010-1111-2222", "ppp",
                "ComputerScience", "2016147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 ppp이다", false, a.getBytes(), a.getBytes(), projectMembers);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        List<Member> members = memberRepository.findAll();
        assertEquals(2L, members.size());
    }

    @Test
    public void loginID회원찾기() {
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "00-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        //when
        memberRepository.save(member);

        //then
        em.flush();
        em.clear();
        Member findMember = memberRepository.findByLoginID(member.getLoginID());
        member.equals(findMember);
    }


    @Test
    public void 이메일로회원찾기() {
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        //when
        memberRepository.save(member);

        //then
        em.flush();
        em.clear();
        Member findMember = memberRepository.findByEmail(member.getEmail());
        member.equals(findMember);
    }

    @Test
    public void 학번으로회원찾기() {
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        //when
        memberRepository.save(member);

        //then
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByEmail(member.getEmail());
        member.equals(findMember);
    }

    @Test
    public void 회원가입_후_탈퇴() {
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        memberRepository.save(member);
        memberRepository.delete(member);

        Optional<Member> findMember = memberRepository.findById(member.getUUID());
        assertTrue(findMember.isEmpty());
    }
}