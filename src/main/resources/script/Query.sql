create database soccer_date;
--------------------------------------------------------------------------

create table public.app_user
(
    id            serial
        primary key,
    first_name    varchar(50)  not null,
    last_name     varchar(50)  not null,
    email         varchar(250) not null
        unique,
    password      varchar(250) not null,
    profile_image varchar(250) default 'defaultProfile.png'::character varying,
    is_verify     boolean      default false,
    skill         varchar(50),
    address       varchar(250) default 'Phnom Penh'::character varying,
    contact       varchar(50),
    description   varchar(250),
    create_at     timestamp    default now()
);


create table public.role
(
    id   serial
        primary key,
    name varchar(100)
);

create table public.user_detail
(
    id      serial
        primary key,
    user_id integer not null
        constraint fk_user
            references public.app_user
            on update cascade on delete cascade,
    role_id integer not null
        constraint fk_role
            references public.role
            on update cascade on delete cascade
);



create table public.verify
(
    user_id     integer    not null
        constraint fk_user
            references public.app_user
            on update cascade on delete cascade,
    verify      varchar(6) not null
        unique,
    time_create time default now()
);


create table team
(
    id          serial4 primary key,
    name        varchar(50) not null,
    logo        varchar(250),
    description varchar(150)
);

create table role_play
(
    id        serial4 primary key,
    role_name varchar(100)
);


create table team_member
(
    team_id       int4 not null,
    player_id     int4 not null,
    player_number int4,
    role_id       int4,
    is_owner      bool default false,
    constraint fk_team foreign key (team_id) references team (id) on delete cascade on update cascade,
    constraint fk_user foreign key (player_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_role foreign key (role_id) references role_play (id) on delete cascade on update cascade
);


create table venue
(
    id            serial4 primary key,
    location_name varchar(250)
);
create table match
(
    id          serial4 primary key,
    match_title varchar(250) not null,
    time        timestamp    not null,
    location_id int4         not null,
    home_id     int4         not null,
    away_id     int4,
    score_home  int4,
    score_away  int4,
    create_at   timestamp default now(),
    constraint fk_home foreign key (home_id) references team (id) on delete cascade on update cascade,
    constraint fk_away foreign key (away_id) references team (id) on delete cascade on update cascade,
    constraint fk_location foreign key (location_id) references venue (id) on delete cascade on update cascade
);
create table request_match
(
    id       serial4 primary key,
    team_id  int4 not null,
    match_id int4 not null,
    status   varchar(10) default 'pending',
    constraint fk_team foreign key (team_id) references team (id) on delete cascade on update cascade,
    constraint fk_match foreign key (match_id) references match (id) on delete cascade on update cascade
);
create table request_team
(
    id        serial4 primary key,
    team_id   int4 not null,
    player_id int4 not null,
    status    varchar(10) default 'pending',
    constraint fk_team foreign key (team_id) references team (id) on delete cascade on update cascade,
    constraint fk_player foreign key (player_id) references app_user (id) on delete cascade on update cascade
);
create table invite_player
(
    id         serial4 primary key,
    team_id    int4 not null,
    player_id  int4 not null,
    inviter_id int4 not null,
    constraint fk_team foreign key (team_id) references team (id) on delete cascade on update cascade,
    constraint fk_player foreign key (player_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_inviter foreign key (inviter_id) references app_user (id) on delete cascade on update cascade
);



create table match_attendee
(
    id        serial4 primary key,
    team_id   int4 not null,
    player_id int4 not null,
    match_id  int4 not null,
    role_id   int4,
    constraint fk_team foreign key (team_id) references team (id) on delete cascade on update cascade,
    constraint fk_player foreign key (player_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_match foreign key (match_id) references match (id) on delete cascade on update cascade,
    constraint fk_role foreign key (role_id) references role_play (id) on delete cascade on update cascade
);

create table match_comment
(
    id        serial4 primary key,
    text      varchar(250),
    match_id  int4 not null,
    team_member_id int4 not null,
    is_public boolean default true,
    parent_id int4 ,
    create_at timestamp default now(),
    constraint fk_match foreign key (match_id) references match (id) on delete cascade on update cascade,
    constraint fk_team_member foreign key (team_member_id) references team_member (id) on delete cascade on update cascade,
    constraint fk_match_comment foreign key (parent_id) references match_comment on delete cascade
);


create table match_like
(
    id        serial4 primary key,
    user_id   int4 not null,
    match_id   int4 not null,
    create_at timestamp default now(),
    constraint fk_user foreign key (user_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_match foreign key (match_id) references match (id) on delete cascade on update cascade
);


create table post
(
    id        serial4 primary key,
    title     varchar(200),
    article   varchar,
    image     varchar(250),
    user_id   int4 not null,
    create_at timestamp default now(),
    constraint fk_user foreign key (user_id) references app_user (id) on delete cascade on update cascade
);
create table comment
(
    id        serial4 primary key,
    comment   varchar,
    user_id   int4 not null,
    post_id   int4 not null,
    create_at timestamp default now(),
    constraint fk_user foreign key (user_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_post foreign key (post_id) references post (id) on delete cascade on update cascade
);

create table post_like
(
    id        serial4 primary key,
    user_id   int4 not null,
    post_id   int4 not null,
    create_at timestamp default now(),
    constraint fk_user foreign key (user_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_post foreign key (post_id) references post (id) on delete cascade on update cascade
);
create table reply_comment
(
    id         serial4 primary key,
    r_comment  varchar,
    user_id    int4 not null,
    comment_id int4 not null,
    create_at  timestamp default now(),
    constraint fk_user foreign key (user_id) references app_user (id) on delete cascade on update cascade,
    constraint fk_comment foreign key (comment_id) references comment (id) on delete cascade on update cascade
);




create table public.team_member
(
    id            serial primary key ,
    team_id       integer not null
        constraint fk_team
            references public.team
            on update cascade on delete cascade,
    player_id     integer not null
        constraint fk_user
            references public.app_user
            on update cascade on delete cascade,
    player_number integer default 0,
    role_id       integer default 5
        constraint fk_role
            references public.role_play
            on update cascade on delete cascade,
    is_owner      boolean default false

);

alter table public.team_member
    owner to soccer_date;


create table venue
(
    id            serial4 primary key,
    location_name varchar(250)
);




create table match
(
    id          serial4 primary key,
    match_title varchar(250) not null,
    time        timestamp    not null,
    location_id int4         not null,
    pitch       varchar(250),
    home_id     int4         not null,
    away_id     int4,
    score_home  int4,
    score_away  int4 default null,
    create_at   timestamp default now(),
    constraint fk_home foreign key (home_id) references team (id) on delete cascade on update cascade,
    constraint fk_away foreign key (away_id) references team (id) on delete cascade on update cascade,
    constraint fk_location foreign key (location_id) references venue (id) on delete cascade on update cascade
);


