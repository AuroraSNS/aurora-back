create sequence hibernate_sequence start with 1 increment by 1;

create table friend
(
    me_id  bigint not null,
    you_id bigint not null,
    primary key (me_id, you_id)
);

create table hello_entity
(
    hello_id bigint not null AUTO_INCREMENT,
    text1    varchar(255),
    text2    varchar(255),
    primary key (hello_id)
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
    status     varchar(255),
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
    provider    varchar(255),
    provider_id varchar(255),
    role        varchar(255),
    primary key (user_id)
);

alter table friend add constraint FKm8stwgqp91633qd94lj42tsj5 foreign key (me_id) references user (user_id);
alter table friend add constraint FKgp9n7fvh2clwhl8nrgl652ntq foreign key (you_id) references user (user_id);
alter table image add constraint FKe2l07hc93u2bbjnl80meu3rn4 foreign key (post_id) references post (post_id);
alter table notification add constraint FK2yifcahfjv13yy7xj33xa606y foreign key (recipient) references user (user_id);
alter table notification add constraint FKbtvkivatoh9yitle5unojiqt4 foreign key (writer_id) references user (user_id);
alter table post add constraint FKck6542xnt1axiyqjlceoc7khj foreign key (writer) references user (user_id);