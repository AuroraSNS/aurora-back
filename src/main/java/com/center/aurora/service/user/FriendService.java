package com.center.aurora.service.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.domain.user.friend.FriendStatus;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FriendService {

    private final UserRepository userRepository;

    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public List<FriendListDto> findAllFriends(Long myId){
        User me = userRepository.findById(myId).get();
        return me.getFriends().stream()
                .filter(x -> x.getStatus().equals(FriendStatus.FRIEND))
                .map(Friend::getYou)
                .map(FriendListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriend(Long myId, Long friendId){
        User me = userRepository.findById(myId).get();
        User friend = userRepository.findById(friendId).get();

        addFriendLogic(me, friend);
        addFriendLogic(friend, me);
    }

    public void addFriendLogic(User user1, User user2){
        Long myId = user1.getId(), friendId = user2.getId();
        FriendId id = new FriendId(myId, friendId);

        Optional<Friend> relation = friendRepository.findById(id);
        if(relation.isPresent()){
            relation.get().changeStatusToFriend();
        }else{
            Friend newFriend = Friend.builder().id(id).me(user1).you(user2).status(FriendStatus.FRIEND).build();
            friendRepository.save(newFriend);
        }
    }

    @Transactional
    public void deleteFriend(Long myId, Long friendId){
        Friend relation1 = friendRepository.findById(new FriendId(myId, friendId)).get();
        Friend relation2 = friendRepository.findById(new FriendId(friendId, myId)).get();

        friendRepository.delete(relation1);
        friendRepository.delete(relation2);
    }
}
