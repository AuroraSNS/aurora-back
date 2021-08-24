package com.center.aurora.service.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UserUpdateDto {

    public MultipartFile image;
    public String name;
    public String bio;
    public Boolean isImageChanged;

    @Builder
    public UserUpdateDto(MultipartFile image, String name, String bio, Boolean isImageChanged) {
        this.image = image;
        this.name = name;
        this.bio = bio;
        this.isImageChanged = isImageChanged;
    }

    @Override
    public String toString() {
        return "UserUpdateDto{" +
                "name='" + name + '\'' +
                ", image=" + ((image == null)? null : image) +
                ", bio='" + bio + '\'' +
                ", isImageChanged=" + isImageChanged +
                '}';
    }
}
