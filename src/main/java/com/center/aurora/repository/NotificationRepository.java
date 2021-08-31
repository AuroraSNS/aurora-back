package com.center.aurora.repository;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.notification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public Long countByRecipientIdAndStatusAndType(Long recipientId, NotificationStatus status, NotificationType type);

    public List<Notification> findAllByTargetIdAndTypeAndStatus(Long targetId, NotificationType type, NotificationStatus status);

    public List<Notification> findAllByRecipientIdAndType(Long recipientId, NotificationType type);

    @Query("select n from Notification n where n.recipient.id = :id and n.type = 'POST' or n.type = 'FRIEND_ACCEPT'")
    public List<Notification> findAllNormalNotification(@Param("id") Long userId);
}
