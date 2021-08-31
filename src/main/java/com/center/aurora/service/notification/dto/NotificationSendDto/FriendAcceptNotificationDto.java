package com.center.aurora.service.notification.dto.NotificationSendDto;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.service.notification.dto.NotificationRecvDto;

public class FriendAcceptNotificationDto extends NotificationSendDto{
    public FriendAcceptNotificationDto(NotificationRecvDto recvDto) {
        super(NotificationType.FRIEND_ACCEPT.name());
    }
}
