package com.center.aurora.service.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FriendService {

    private final UserRepository userRepository;

    private final FriendRepository friendRepository;


    @Transactional(readOnly = true)
    public List<FriendListDto> findAllFriends(Long myId){
        List<Friend> myFriends = friendRepository.findAllByMe_Id(myId);
        return myFriends.stream()
                .map(x-> FriendListDto.entityToDto(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriend(Long myId, Long friendId){
        User me = userRepository.findById(myId).get();
        User you = userRepository.findById(friendId).get();

        FriendId friendId1 = new FriendId(me.getId(), you.getId());
        Friend newRelation1 = Friend.builder().me(me).you(you).id(friendId1).build();
        friendRepository.save(newRelation1);

        FriendId friendId2 = new FriendId(you.getId(), me.getId());
        Friend newRelation2 = Friend.builder().me(you).you(me).id(friendId2).build();
        friendRepository.save(newRelation2);
    }

    @Transactional
    public void deleteFriend(Long myId, Long friendId){
        Friend friend1 = friendRepository.findByMe_IdAndYou_Id(myId, friendId);
        Friend friend2 = friendRepository.findByMe_IdAndYou_Id(friendId, myId);
        friendRepository.delete(friend1);
        friendRepository.delete(friend2);
    }
}
