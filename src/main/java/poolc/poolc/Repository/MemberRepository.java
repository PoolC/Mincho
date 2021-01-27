package poolc.poolc.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(String UUID){
        return em.find(Member.class, UUID);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findByStudentID(String studentID){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", studentID)
                .getResultList();
    }
}
