package com.center.aurora.controller;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.notification.NotificationService;
import com.center.aurora.service.notification.dto.FriendRequestListDto;
import com.center.aurora.service.notification.dto.NotificationCountDto;
import com.center.aurora.service.notification.dto.NotificationListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/count")
    public NotificationCountDto getAllNotifications(@CurrentUser UserPrincipal user){
        return notificationService.getAllNotificationCount(user.getId());
    }

    @PatchMapping("/{type}/{id}")
    public void readNotification(@PathVariable String type, @PathVariable Long id){
        if (type.equals(NotificationType.CHATTING.name())){
            notificationService.updateAllChattingNotificationStatus(id);
        }else{
            notificationService.updateNotificationStatus(id);
        }
    }

    @GetMapping("/friend")
    public List<FriendRequestListDto> getAllFriendRequest(@CurrentUser UserPrincipal user){
        return notificationService.getAllFriendRequestNotification(user.getId());
    }

    @GetMapping("")
    public List<NotificationListDto> getAllNormalRequest(@CurrentUser UserPrincipal user){
        return notificationService.getAllNormalNotification(user.getId());
    }


}
