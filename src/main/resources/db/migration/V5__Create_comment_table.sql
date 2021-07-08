create table COMMENT
(
    ID           BIGINT auto_increment primary key ,
    PARENT_ID    BIGINT not null,
    TYPE         INT    not null,
    COMMENTATOR  INT   not null,
    GMT_CREATE   BIGINT not null,
    GMT_MODIFIED BIGINT not null,
    LIKE_COUNT   BIGINT,
    CONTENT      VARCHAR(255)
);

