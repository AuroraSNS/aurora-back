package com.center.aurora.service.notification;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.domain.user.friend.FriendStatus;
import com.center.aurora.exception.NotExistNotificationType;
import com.center.aurora.repository.NotificationRepository;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.notification.dto.*;
import com.center.aurora.service.notification.dto.NotificationSendDto.*;
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

    private final FriendRepository friendRepository;

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
                Friend friend1 = Friend.builder().id(new FriendId(from.getId(), to.getId())).me(from).you(to).status(FriendStatus.ONGOING).build();
                Friend friend2 = Friend.builder().id(new FriendId(to.getId(), from.getId())).me(to).you(from).status(FriendStatus.ONGOING).build();
                friendRepository.save(friend1);
                friendRepository.save(friend2);
                ret = new FriendRequestNotificationDto(notificationRecvDto);
                break;
            case FRIEND_ACCEPT:
                ret = new FriendAcceptNotificationDto(notificationRecvDto);
                break;
            case CHATTING:
                ret = new ChatNotificationDto(notificationRecvDto);
                break;
            default:
                log.error("????????? ?????? ?????? ?????? ??????");
                throw new NotExistNotificationType("???????????? ?????? ?????? ???????????????!!");
        }
        notificationRepository.save(notification);
        return ret;
    }
    /**
     * ?????? ?????? ?????? ?????? ?????? ?????? ??????
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
     * ??????  ????????? ?????? ?????? ?????? ????????? ?????? ????????? ?????? ??????
     * */
    @Transactional
    public void updateAllChattingNotificationStatus(Long roomId){
        List<Notification> notifications = notificationRepository.findAllByTargetIdAndTypeAndStatus(roomId, NotificationType.CHATTING, NotificationStatus.NOT_READ);
        for (Notification notification : notifications) {
            notification.read();
        }
    }
    /**
     * ?????? ?????? ?????? ????????? ??????
     * */
    @Transactional
    public void updateNotificationStatus(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.read();
    }

    /**
     * ???????????? ?????? ??????, ?????? ?????? ????????? ??????
     * */
    @Transactional(readOnly = true)
    public List<FriendRequestListDto> getAllFriendRequestNotification(Long userId){
        List<Notification> res = notificationRepository.findAllByRecipientIdAndTypeAndStatus(userId, NotificationType.FRIEND_REQUEST, NotificationStatus.NOT_READ);
        return res.stream()
                .map(FriendRequestListDto::new)
                .sorted((a,b) -> b.getTimeStamp().compareTo(a.getTimeStamp()))
                .collect(Collectors.toList());
    }
    /**
     * ?????? ?????? ?????? ??????
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
