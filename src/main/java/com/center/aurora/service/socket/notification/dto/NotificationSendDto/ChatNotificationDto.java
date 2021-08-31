package com.center.aurora.service.socket.notification.dto.NotificationSendDto;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.domain.user.User;
import com.center.aurora.service.socket.notification.dto.NotificationRecvDto;


public class ChatNotificationDto extends NotificationSendDto{

    public ChatNotificationDto(NotificationRecvDto recvDto) {
        super(NotificationType.CHATTING.name());
    }
}
