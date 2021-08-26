package com.center.aurora.service.chat;

import com.center.aurora.domain.chat.ChatRoom;
import com.center.aurora.domain.chat.Message;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.chat.ChatRoomRepository;
import com.center.aurora.repository.chat.MessageRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.chat.dto.ChatRoomDto;
import com.center.aurora.service.chat.dto.ChatRoomListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomListDto> findAllByUserId(Long id){
        List<ChatRoom> rooms = chatRoomRepository.findAllMyChatRoom(id);
        return rooms.stream()
            .map(x -> {
                Message lastMessage = null;
                if(x.getMessages().size() > 0) lastMessage = x.getMessages().get(x.getMessages().size()-1);
                return new ChatRoomListDto(id, x, lastMessage);
            })
            .sorted(Comparator.comparing(ChatRoomListDto::getLastTimeStamp))
            .collect(Collectors.toList());

    }

    @Transactional
    public void createChatRoom(Long myId, Long anotherUserId) {
        User me = userRepository.findById(myId).get();
        User you = userRepository.findById(anotherUserId).get();

        ChatRoom newChatRoom = ChatRoom.builder()
                .participant1(me)
                .participant2(you)
                .build();

        chatRoomRepository.save(newChatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoomDto findRoomById(Long roomId, Long meId) throws IllegalAccessException {
        Boolean isExist = chatRoomRepository.existsChatRoomById(roomId);
        if(!isExist){
            throw new IllegalAccessException();
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        return new ChatRoomDto(chatRoom);
    }

    @Transactional
    public void deleteRoomById(Long roomId) throws IllegalAccessException {
        Boolean isExist = chatRoomRepository.existsChatRoomById(roomId);
        if(!isExist){
            throw new IllegalAccessException();
        }
        chatRoomRepository.deleteById(roomId);
    }
}
