package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityMember;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotAdminOrHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final EntityManager em;

    @Transactional
    public void createActivity(ActivityCreateValues values, Member member) {
        Activity activity = new Activity(values.getTitle(), values.getDescription(),
                member,
                values.getStartDate(), values.getClassHour(), values.getIsSeminar(), values.getCapacity(), values.getHour(), false);
        activity.getTags().addAll(values.getTags().stream().map(t -> new ActivityTag(activity, t)).collect(Collectors.toList()));
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id, Member member) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다!"));
        if (!checkWhetherAdminOrHost(member.isAdmin(), member.getUUID(), activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activityRepository.delete(activity);
    }

    @Transactional
    public void updateActivity(Member member, Long id, ActivityUpdateValues values) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        if (!checkWhetherAdminOrHost(member.isAdmin(), member.getUUID(), activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activity.update(values);
    }

    @Transactional
    public void apply(Long id, String uuid) {
        Activity activity = activityRepository.findOneActivityWithMembers(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 없습니다"));
        if (!activity.getAvailable()) {
            throw new IllegalStateException("아직 신청할수 없습니다.");
        } else if (activity.getCapacity() <= activity.getMembers().size()) {
            throw new IllegalStateException("정원을 초과하였습니다");
        } else {
            Member member = memberRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("해당하는 회원이 없습니다"));
            ActivityMember activityMember = new ActivityMember(activity, member);
            activity.apply(activityMember);
        }
    }

    public List<Activity> findActivities() {
        return activityRepository.findActivitiesWithHostAndTags();
    }

    public Activity findOneActivity(Long id) {
        return activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
    }

    public List<ActivityMember> findActivityMembers(Long id) {
        return activityRepository.findOneActivityWithMembers(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다")).getMembers();
    }

    @Transactional
    public Map<ActivityMember, Boolean> findActivityMembersWithAttendance(Long id) {
        Session session = sessionRepository.findByIdWithAttendances(id).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        List<ActivityMember> members = activityRepository.findOneActivityWithMembers(session.getActivity().getId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"))
                .getMembers();
        Map<ActivityMember, Boolean> result = new HashMap<>();
        members.stream().forEach(m -> result.put(m, false));
        session.getAttendances().forEach(m -> result.put(m.getMemberID(), true));
        return result;
    }

    private boolean checkWhetherAdminOrHost(Boolean isAdmin, String MemberID, String activityHostID) {

        if ((MemberID.equals(activityHostID)) || (isAdmin)) {
            return true;
        } else {
            return false;
        }
    }

}
