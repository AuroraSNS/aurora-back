package com.center.aurora.service.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FriendService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FriendListDto> findAllFriends(Long myId){
        User me = userRepository.findById(myId).get();
        Set<User> friends = me.getFriends();

        return friends.stream()
                .map(x-> FriendListDto.entityToDto(x))
                .sorted(Comparator.comparing(FriendListDto::getName))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendListDto> findFriendsByName(String name){
        List<User> friends = userRepository.findFriendsByName(name);

        return friends.stream()
                .map(x-> FriendListDto.entityToDto(x))
                .sorted(Comparator.comparing(FriendListDto::getName))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriend(Long myId, Long friendId){
        User me = userRepository.findById(myId).get();
        User friend = userRepository.findById(friendId).get();

        me.addFriends(friend);
    }

    @Transactional
    public void deleteFriend(Long myId, Long friendId){
        User me = userRepository.findById(myId).get();
        User friend = userRepository.findById(friendId).get();

        me.deleteFriend(friend);
    }
}
