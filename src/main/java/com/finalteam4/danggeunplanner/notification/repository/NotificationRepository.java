package com.finalteam4.danggeunplanner.notification.repository;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.notification.entity.Notification;
import com.finalteam4.danggeunplanner.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMember_Id(Long memberId);
    Boolean existsByIsReadAndMember(Boolean isRead, Member member);
    @Query("select count(n) from Notification n " +
            "where n.member.id = :memberId and " +
            "n.isRead = false")
    Long countUnReadNotification(@Param("memberId") Long memberId);
    Boolean existsByMemberAndGroupIdAndNotificationType(Member member, Long groupId, NotificationType notificationType);
    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.isRead = true where n.member.id = :memberId")
    void updateAllIsRead(@Param("memberId") Long memberId);
}
