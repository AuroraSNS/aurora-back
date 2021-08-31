package com.center.aurora.service.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import com.center.aurora.service.user.dto.UserUpdateDto;
import com.center.aurora.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

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
    public List<FriendListDto> findFriendsByName(String name){
        List<User> friends = userRepository.findFriendsByName(name);

        return friends.stream()
                .map(x-> FriendListDto.entityToDto(x))
                .sorted(Comparator.comparing(FriendListDto::getName))
                .collect(Collectors.toList());
    }

    private String fileUpload(MultipartFile file) throws IOException {
        return s3Uploader.upload(file, "aurora");
    }

    private void fileDelete(String url){
        s3Uploader.deleteFile(url,"aurora");
    }
}
