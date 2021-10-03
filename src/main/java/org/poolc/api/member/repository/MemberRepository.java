package org.poolc.api.member.repository;

import org.poolc.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUUID(String UUID);

    Optional<Member> findByLoginID(String loginID);

    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByPasswordResetToken(String passwordResetToken);

    List<Member> findByName(String name);

    @Query("select m from Member m where m.loginID in (:loginIDs)")
    List<Member> findAllMembersByLoginIDList(@Param("loginIDs") List<String> loginIDs);

    boolean existsByLoginIDOrEmailOrPhoneNumberOrStudentID(String loginID, String email, String phoneNumber, String studentID);
}