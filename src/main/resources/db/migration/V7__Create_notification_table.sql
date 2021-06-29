CREATE table notification(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notifier int not null,
    receiver int not null,
    outer_id bigint not null,
    type int not null,
    gmt_create BIGINT not null,
    status int default 0 not null
);