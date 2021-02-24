package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Attendance;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.dto.AttendanceRequest;
import org.poolc.api.activity.exception.NotHostException;
import org.poolc.api.activity.repository.ActivityMemberRepository;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.activity.vo.SessionUpdateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final EntityManager em;
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMemberRepository activityMemberRepository;

    @Transactional

    public void createSession(String userID, SessionCreateValues sessionCreateValues) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(sessionCreateValues.getActivityID()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        if (!checkUserIsHost(activity.getHost().getUUID(), userID)) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 입력할수 없습니다");
        }
        sessionRepository.save(new Session(activity, sessionCreateValues.getDescription(), sessionCreateValues.getDate(), sessionCreateValues.getSessionNumber()));
    }


    public List<Session> findSessionsByActivityID(String userID, Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        if (!checkUserIsHost(activity.getHost().getUUID(), userID)) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 조회할수 없습니다");
        }

        return sessionRepository.findByActivity(activity);
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
    public void attend(String uuid, AttendanceRequest requestBody) {
        Session session = sessionRepository.findById(requestBody.getSessionID())
                .orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        Activity activity = session.getActivity();
        if (!checkUserIsHost(uuid, activity.getHost().getUUID())) {
            throw new NotHostException("호스트가 아니면 출석체크를 할수 없습니다");
        }
        session.attend(requestBody.getMembers().stream()
                .map(s -> activityMemberRepository.findById(s))
                .map(m -> new Attendance(session, m.orElseThrow(() -> new NoSuchElementException("해당하는 회원이 존재하지 않습니다"))))
                .collect(Collectors.toList()));

    }

    private boolean checkUserIsHost(String uuid, String userID) {
        if (!uuid.equals(userID)) {
            return false;
        } else {
            return true;
        }
    }
}
