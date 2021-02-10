package poolc.poolc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poolc.poolc.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);

    Member findByLoginID(String LoginID);

    Member findByStudentID(String StudentID);

}