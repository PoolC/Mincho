package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotAdminOrHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.common.domain.YearSemester;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;
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
        Activity activity = new Activity(member, values);
        activity.getTags().addAll(values.getTags().stream().map(t -> new ActivityTag(activity, t)).collect(Collectors.toList()));
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id, Member member) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당 활동이 존재하지 않습니다!"));
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
    public void openActivity(Long id) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        checkActivityIsClosed(activity);
        activity.open();
    }

    @Transactional
    public void closeActivity(Long id) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        checkActivityIsOpen(activity);
        activity.close();
    }

    @Transactional
    public Activity apply(Long id, Member user) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 없습니다"));
        if (!activity.getAvailable()) {
            throw new IllegalStateException("아직 신청할수 없습니다.");
        } else if (!activity.checkMemberContain(user.getLoginID()) && activity.getCapacity() <= activity.getMemberLoginIDs().size()) {
            throw new IllegalStateException("정원을 초과하였습니다");
        } else {
            activity.toggleApply(user.getLoginID());
        }
        return activity;
    }

    public List<Activity> findActivities() {
        List<Activity> activities = activityRepository.findActivitiesWithHostAndTags();
        activities.sort(Comparator.comparing(Activity::getStartDate).reversed());
        return activities;
    }

    public List<Activity> findActivitiesInSemester(String value) {
        YearSemester yearSemester = YearSemester.getYearSemesterFromString(value);
        LocalDate startDate = yearSemester.getFirstDateFromYearSemester();
        LocalDate endDate = yearSemester.getLastDateFromYearSemester();
        List<Activity> activities = activityRepository.findActivitiesWithHostAndTagsInSemester(startDate, endDate);
        activities.sort(Comparator.comparing(Activity::getTitle));
        return activities;
    }


    public Activity findOneActivity(Long id) {
        return activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
    }

    public List<Member> findActivityMembersByActivityId(Long activityId) {
        List<Member> activityMembers = memberRepository.findAllMembersByLoginIDList(activityRepository.findById(activityId).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다")).getMemberLoginIDs());
        activityMembers.sort(Comparator.comparing(Member::getName));
        return activityMembers;
    }

    public List<Activity> findActivitiesByHost(Member host) {
        return activityRepository.findActivitiesByHost(host);
    }

    public List<Activity> findActivitiesByMemberLoginId(String loginId) {
        return activityRepository.findActivitiesByActivityMembers(loginId);
    }

    @Transactional
    public Map<Member, Boolean> findActivityMembersWithAttendance(Long id) {
        Session session = sessionRepository.findByIdWithAttendances(id).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        List<Member> members = memberRepository.findAllMembersByLoginIDList(session.getActivity().getMemberLoginIDs());
        Map<Member, Boolean> result = new HashMap<>();
        members.stream().forEach(m -> result.put(m, false));
        session.getAttendedMemberLoginIDs().forEach(m -> result.put(memberRepository.findByLoginID(m).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다")), true));
        return result;
    }


    public List<YearSemester> findActivityYears() {
        return activityRepository.findAll().stream().map(a -> YearSemester.of(a.getStartDate())).distinct().collect(Collectors.toList());
    }

    private void checkActivityIsClosed(Activity activity) {
        if (activity.getAvailable()) {
            throw new IllegalStateException("이미 신청가능한 활동입니다");
        }
    }

    private void checkActivityIsOpen(Activity activity) {
        if (!activity.getAvailable()) {
            throw new IllegalStateException("이미 닫혀있는 활동입니다");
        }
    }

    private boolean checkWhetherAdminOrHost(Boolean isAdmin, String MemberID, String activityHostID) {

        if ((MemberID.equals(activityHostID)) || (isAdmin)) {
            return true;
        } else {
            return false;
        }
    }
}
