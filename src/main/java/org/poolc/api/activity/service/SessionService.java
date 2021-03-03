package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.AttendanceValues;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.activity.vo.SessionUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final EntityManager em;
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @Transactional
    public Session createSession(Member member, SessionCreateValues sessionCreateValues) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(sessionCreateValues.getActivityID()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        if (!checkUserIsHost(activity.getHost().getUUID(), member.getUUID())) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 입력할수 없습니다");
        }
        Session session = new Session(activity, sessionCreateValues.getDescription(), sessionCreateValues.getDate(), sessionCreateValues.getSessionNumber());
        sessionRepository.save(session);
        return session;
    }


    public List<Session> findSessionsByActivityID(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        return sessionRepository.findByActivity(activity);
    }

    public Session findOneSessionByID(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회차입니다"));
    }

    @Transactional
    public void updateSession(Long id, SessionUpdateValues values) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회차입니다"));
        if (!checkUserIsHost(session.getActivity().getHost().getUUID(), values.getUuid())) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 업데이트 할수 없습니다");
        }
        session.update(values);
    }

    @Transactional
    public void attend(String uuid, AttendanceValues values) {
        Session session = sessionRepository.findById(values.getSessionID())
                .orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        Activity activity = session.getActivity();
        if (!checkUserIsHost(uuid, activity.getHost().getUUID())) {
            throw new NotHostException("호스트가 아니면 출석체크를 할수 없습니다");
        }
        checkMembersExist(values.getMemberLoginIDs());
        session.clear();
        session.attend(values.getMemberLoginIDs());

    }

    private void checkMembersExist(List<String> memberLoginIDs) {
        memberLoginIDs.forEach(memberService::findMemberbyLoginID);
    }

    private boolean checkUserIsHost(String uuid, String userID) {
        if (!uuid.equals(userID)) {
            return false;
        } else {
            return true;
        }
    }
}
