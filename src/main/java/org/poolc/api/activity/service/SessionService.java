package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.activity.vo.SessionUpdateValues;
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

    private boolean checkUserIsHost(String uuid, String userID) {
        if (!uuid.equals(userID)) {
            return false;
        } else {
            return true;
        }
    }

}
