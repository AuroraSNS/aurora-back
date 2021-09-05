package com.center.aurora.domain.user;

import com.center.aurora.domain.chat.ChatRoom;
import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    public static final String DEFAULT_IMAGE_URL = "https://aurora-image-bucket.s3.ap-northeast-2.amazonaws.com/aurora/defaultProfile.png";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String email;

    private String image;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column
    private String providerId;

    private String password;

    @OneToMany(mappedBy = "me", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Friend> friends = new HashSet<>();

    @OneToMany(mappedBy = "you", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Friend> friendsOf = new HashSet<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    List<Post> posts = new ArrayList<>();

    public User update(String name) {
        this.name = name;
        return this;
    }

    public void update(String name, String bio, String image){
        this.name = name;
        this.bio = bio;
        this.image = (image.equals(""))? User.DEFAULT_IMAGE_URL : image;
    }

    public Set<User> getFriendsSet(){
        return friends.stream()
                .filter(x -> x.getStatus().equals(FriendStatus.FRIEND))
                .map(Friend::getYou)
                .collect(Collectors.toSet());
    }


    @Builder
    public User(String name, String email, String image, String bio, Role role, AuthProvider provider, String providerId, String password) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.bio = bio;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.password = password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getImage(), user.getImage()) && Objects.equals(getBio(), user.getBio()) && getRole() == user.getRole() && getProvider() == user.getProvider() && Objects.equals(getProviderId(), user.getProviderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getImage(), getBio(), getRole(), getProvider(), getProviderId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", bio='" + bio + '\'' +
                ", role=" + role +
                ", provider=" + provider +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
