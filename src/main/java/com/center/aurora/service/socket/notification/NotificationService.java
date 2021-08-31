package com.center.aurora.service.socket.notification;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.domain.user.User;
import com.center.aurora.exception.NotExistNotificationType;
import com.center.aurora.repository.socket.NotificationRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.socket.notification.dto.*;
import com.center.aurora.service.socket.notification.dto.NotificationSendDto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationSendDto saveNotification(NotificationRecvDto notificationRecvDto){
        User from = userRepository.findById(notificationRecvDto.getFrom()).get();
        User to = userRepository.findById(notificationRecvDto.getTo()).get();

        Notification notification = notificationRecvDto.toEntity(from, to);

        NotificationSendDto ret;
        switch (notification.getType()){
            case POST:
                ret = new PostNotificationDto(notificationRecvDto);
                break;
            case FRIEND_REQUEST:
                ret = new FriendRequestNotificationDto(notificationRecvDto);
                break;
            case FRIEND_ACCEPT:
                ret = new FriendAcceptNotificationDto(notificationRecvDto);
                break;
            case CHATTING:
                ret = new ChatNotificationDto(notificationRecvDto);
                break;
            default:
                log.error("잘못된 알림 타입 전달 받음");
                throw new NotExistNotificationType("존재하지 않는 알림 타입입니다!!");
        }
        notificationRepository.save(notification);
        return ret;
    }
    /**
     * 읽지 않은 알림 종류 별로 개수 반환
     * */
    @Transactional(readOnly = true)
    public NotificationCountDto getAllNotificationCount(Long userId){
        Map<String, Long> ret = new HashMap<>();
        for (NotificationType type : NotificationType.values()) {
            Long cnt = notificationRepository.countByRecipientIdAndStatusAndType(userId, NotificationStatus.NOT_READ, type);
            ret.put(type.name(), cnt);
        }
        return new NotificationCountDto(ret);
    }
    /**
     * 특정  채팅방 읽지 않은 채팅 메시지 읽음 상태로 전부 변경
     * */
    @Transactional
    public void updateAllChattingNotificationStatus(Long roomId){
        List<Notification> notifications = notificationRepository.findAllByTargetIdAndTypeAndStatus(roomId, NotificationType.CHATTING, NotificationStatus.NOT_READ);
        for (Notification notification : notifications) {
            notification.read();
        }
    }
    /**
     * 특정 알림 읽음 상태로 변경
     * */
    @Transactional
    public void updateNotificationStatus(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.read();
    }

    /**
     * 친구요청 알림 반환
     * */
    @Transactional(readOnly = true)
    public List<FriendRequestListDto> getAllFriendRequestNotification(Long userId){
        List<Notification> res = notificationRepository.findAllByRecipientIdAndType(userId, NotificationType.FRIEND_REQUEST);
        return res.stream()
                .map(FriendRequestListDto::new)
                .sorted((a,b) -> b.getTimeStamp().compareTo(a.getTimeStamp()))
                .collect(Collectors.toList());
    }
    /**
     * 일반 알림 전부 조회
     * */
    @Transactional(readOnly = true)
    public List<NotificationListDto> getAllNormalNotification(Long userId){
        List<Notification> res = notificationRepository.findAllNormalNotification(userId);
        return res.stream()
                .map(NotificationListDto::new)
                .sorted((a,b) -> b.getTimeStamp().compareTo(a.getTimeStamp()))
                .collect(Collectors.toList());
    }

}
