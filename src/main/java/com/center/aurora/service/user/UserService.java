package com.center.aurora.service.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.domain.user.friend.FriendStatus;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.RandomUserListDto;
import com.center.aurora.service.user.dto.UserDto;
import com.center.aurora.service.user.dto.UserListDto;
import com.center.aurora.service.user.dto.UserUpdateDto;
import com.center.aurora.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final FriendRepository friendRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    public void userUpdate(Long id, UserUpdateDto updateDto) throws IOException {
        User me = userRepository.findById(id).get();

        String changeName = updateDto.getName();
        String changeBio = updateDto.getBio();
        MultipartFile image = updateDto.getImage();
        if(image == null){
            if(updateDto.getIsImageChanged()){
                fileDelete(me.getImage());
                me.update(changeName, changeBio, User.DEFAULT_IMAGE_URL);
            }else{
                me.update(changeName, changeBio, me.getImage());
            }
        }else{
            String imageUrl = fileUpload(image);
            me.update(changeName, changeBio, imageUrl);
        }
    }

    @Transactional(readOnly = true)
    public List<UserListDto> findUsersByName(String name){
        List<User> users = userRepository.findUserByName(name);

        return users.stream()
                .map(UserListDto::new)
                .sorted(Comparator.comparing(UserListDto::getName))
                .collect(Collectors.toList());
    }

    private String fileUpload(MultipartFile file) throws IOException {
        return s3Uploader.upload(file, "aurora");
    }

    private void fileDelete(String url){
        s3Uploader.deleteFile(url,"aurora");
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long myId, Long targetId){
        User user = userRepository.findById(targetId).get();

        String status = FriendStatus.NOT_FRIEND.name();
        Optional<Friend> friend = friendRepository.findById(new FriendId(myId, targetId));
        if(friend.isPresent()){
            status = friend.get().getStatus().name();
        }
        return new UserDto(user, status);
    }

    @Transactional(readOnly = true)
    public List<RandomUserListDto> getRandomUsers(){
        List<User> users = userRepository.findAllRandom();
        return users.stream().map(RandomUserListDto::new).collect(Collectors.toList());
    }
}
