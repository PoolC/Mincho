package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        assertEquals(member,memberRepository.findOne("1"));
    }


    @Test
    public void 모든회원조회() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member1 = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );
        Member member2 = new Member("2", "ppp", "anfr2520@gmail.com", "010-1111-2222", "ppp",
                "ComputerScience", "2016147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 ppp이다", false, a.getBytes(), a.getBytes(), projectMembers );
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> members = memberRepository.findAll();
        assertEquals(2L, members.size() );
    }

    @Test
    @Rollback(value = false)
    public void UUID회원찾기() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "00-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        em.flush();
        em.clear();
        Member findMember = memberRepository.findOne("1");

        member.equals(findMember);
    }


    @Test
    public void 이메일로회원찾기() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        em.flush();
        em.clear();
        Member findMember = memberRepository.findByEmail("jasotn12@naver.com");
        member.equals(findMember);
    }

    @Test
    public void 회원가입_후_탈퇴() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );
        memberRepository.save(member);
        memberRepository.delete(member);

        Member findMember = memberRepository.findOne("1");
        assertNull(findMember);
    }
}
