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
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final EntityManager em;
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @Transactional
    public Session createSession(Member member, SessionCreateValues sessionCreateValues) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(sessionCreateValues.getActivityID()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        if (!checkUserIsHost(activity.getHost().getUUID(), member.getUUID())) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 입력할수 없습니다");
        }
        Session session = new Session(activity, sessionCreateValues);
        sessionRepository.save(session);
        return session;
    }


    public List<Session> findSessionsByActivityID(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        List<Session> sessionsByActivity = sessionRepository.findByActivity(activity);
        sessionsByActivity.sort(Comparator.comparing(Session::getSessionNumber));
        return sessionsByActivity;
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
        checkMembersExistInActivity(values.getMemberLoginIDs(), activity);
        session.clear();
        session.attend(values.getMemberLoginIDs());

    }

    public List<Map.Entry<Member, Boolean>> findActivityMembersWithAttendanceBySessionId(Long sessionId) {
        Session session = sessionRepository.findByIdWithAttendances(sessionId).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        List<Member> members = memberRepository.findAllMembersByLoginIDList(session.getActivity().getMemberLoginIDs());

        //TODO: Refactor가 필요한 거 같다. 좀 더 세련되게 코드를 짤 수 있을 거 같다.
        Map<Member, Boolean> membersAttendanceMap = new HashMap<>();
        members.stream().forEach(member -> membersAttendanceMap.put(member, false));
        session.getAttendedMemberLoginIDs().forEach(memberLoginId ->
                membersAttendanceMap.put(findByMemberLoginId(memberLoginId), true));
        ArrayList<Map.Entry<Member, Boolean>> memberAttendanceList = new ArrayList<>(membersAttendanceMap.entrySet());
        memberAttendanceList.sort(new Comparator<Map.Entry<Member, Boolean>>() {
            @Override
            public int compare(Map.Entry<Member, Boolean> o1, Map.Entry<Member, Boolean> o2) {
                return o1.getKey().getName().compareTo(o2.getKey().getName());
            }
        });
        return memberAttendanceList;
    }

    private void checkMembersExistInActivity(List<String> memberLoginIDs, Activity activity) {
        memberLoginIDs.forEach((s) -> {
            if (!activity.getMemberLoginIDs().contains(s)) {
                throw new NoSuchElementException("해당하는 회원이 이 활동에 수강신청하지 않았습니다");
            }
        });
    }

    private void checkMembersExist(List<String> memberLoginIDs) {
        memberLoginIDs.forEach(memberService::getMemberByLoginID);
    }

    private boolean checkUserIsHost(String uuid, String userID) {
        if (!uuid.equals(userID)) {
            return false;
        } else {
            return true;
        }
    }

    private Member findByMemberLoginId(String memberLoginId) {
        return memberRepository.findByLoginID(memberLoginId).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
    }
}
