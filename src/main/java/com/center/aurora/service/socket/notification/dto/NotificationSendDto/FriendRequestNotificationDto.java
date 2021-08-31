package com.center.aurora.service.socket.notification.dto.NotificationSendDto;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.domain.user.User;
import com.center.aurora.service.socket.notification.dto.NotificationRecvDto;

public class FriendRequestNotificationDto extends NotificationSendDto{
    public FriendRequestNotificationDto(NotificationRecvDto recvDto) {
        super(NotificationType.FRIEND_REQUEST.name());
    }
}
