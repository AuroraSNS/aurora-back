package com.center.aurora.service.socket.notification.dto;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRecvDto {
    private String type;
    private Long from; //알림 보낸 유저 id
    private Long to; // 알림 받는 유저 id
    private Long targetId; // postid 등 알람의 주체가되는 타겟의 아이디
    private String message;

    public Notification toEntity(User from, User to) throws IllegalArgumentException{
        return Notification.builder()
                .writer(from)
                .recipient(to)
                .type(NotificationType.valueOf(this.type))
                .targetId(this.targetId)
                .message(this.message)
                .status(NotificationStatus.NOT_READ)
                .build();
    }

    @Override
    public String toString() {
        return "NotificationRecvDto{" +
                "type='" + type + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", targetId=" + targetId +
                ", message='" + message + '\'' +
                '}';
    }
}
