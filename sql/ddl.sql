create sequence hibernate_sequence start with 1 increment by 1;

create table comment
(
    comment_id bigint not null AUTO_INCREMENT,
    content TEXT not null,
    post_id bigint,
    writer bigint,
    primary key (comment_id)
);

create table friend
(
    user_id   bigint not null,
    friend_id bigint not null,
    status    varchar(255),
    primary key (user_id, friend_id)
);

create table image
(
    image_id bigint NOT NULL AUTO_INCREMENT,
    image    varchar(255),
    post_id  bigint,
    primary key (image_id)
);


create table notification
(
    id         bigint NOT NULL AUTO_INCREMENT,
    created_at timestamp,
    message    varchar(255),
    status     varchar(255),
    target_id  bigint,
    type       varchar(255),
    recipient  bigint,
    writer_id  bigint,
    primary key (id)
);

create table post
(
    post_id    bigint NOT NULL AUTO_INCREMENT,
    content    TEXT not null,
    created_at timestamp,
    mood       varchar(255),
    updated_at timestamp,
    writer     bigint,
    primary key (post_id)
);

create table user
(
    user_id     bigint NOT NULL AUTO_INCREMENT,
    bio         varchar(255),
    email       varchar(255),
    image       varchar(255),
    name        varchar(255),
    password    varchar(255),
    provider    varchar(255),
    provider_id varchar(255),
    role        varchar(255),
    primary key (user_id)
);

create table chat_room
(
    room_id              bigint NOT NULL AUTO_INCREMENT,
    participant1_user_id bigint,
    participant2_user_id bigint,
    primary key (room_id)
);

create table message
(
    message_id bigint NOT NULL AUTO_INCREMENT,
    message    varchar(255),
    time_stamp timestamp,
    room_id    bigint,
    user_id    bigint,
    primary key (message_id)
);

create table likes (
    post_id bigint not null,
    writer bigint not null,
    primary key (post_id, writer)
);

ALTER TABLE likes ADD CONSTRAINT DeleteLikesCascade FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE;
ALTER TABLE post ADD CONSTRAINT DeletePostCascade FOREIGN KEY (writer) REFERENCES user(user_id) ON DELETE CASCADE;
ALTER TABLE image ADD CONSTRAINT DeleteImageCascade FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE;
ALTER TABLE comment ADD CONSTRAINT DeleteCommentCascade FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE;

alter table likes add constraint FKc7o2t2k4ndpg29hj446mml6ol foreign key (writer) references user(user_id);
alter table likes add constraint FKowd6f4s7x9f3w50pvlo6x3b41 foreign key (post_id) references post(post_id);
alter table comment add constraint FKaqdy2fu25ym7qpn6aajqf7mb2 foreign key (writer) references user(user_id);
alter table comment add constraint FKs1slvnkuemjsq2kj4h3vhx7i1 foreign key (post_id) references post(post_id);
alter table friend add constraint FK3uu8s7yyof1qmenthngm24hry foreign key (user_id) references user (user_id);
alter table friend add constraint FKf9qk6e95h3da7o1u83ihtwrqa foreign key (friend_id) references user (user_id);
alter table image add constraint FKe2l07hc93u2bbjnl80meu3rn4 foreign key (post_id) references post (post_id);
alter table notification add constraint FK2yifcahfjv13yy7xj33xa606y foreign key (recipient) references user (user_id);
alter table notification add constraint FKbtvkivatoh9yitle5unojiqt4 foreign key (writer_id) references user (user_id);
alter table post add constraint FKck6542xnt1axiyqjlceoc7khj foreign key (writer) references user (user_id);

alter table chat_room add constraint FKgcssea4yrfkpr8yt6ke3tvsv foreign key (participant1_user_id) references user(user_id) on delete cascade;
alter table chat_room add constraint FK23jtigsqgksqupo5xfh985m42 foreign key (participant2_user_id) references user(user_id) on delete cascade;
alter table message add constraint FKq97urb0l1mxmmjl54tmlya11f foreign key (room_id) references chat_room(room_id);
alter table message add constraint FKb3y6etti1cfougkdr0qiiemgv foreign key (user_id) references user(user_id);