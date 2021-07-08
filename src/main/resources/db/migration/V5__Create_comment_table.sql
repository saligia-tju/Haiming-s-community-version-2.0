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

comment on column COMMENT.PARENT_ID is '父类id';

comment on column COMMENT.TYPE is '父类类型，区别一级回复还是二级回复';

comment on column COMMENT.COMMENTATOR is '评论人id';

comment on column COMMENT.GMT_CREATE is '创建时间';

comment on column COMMENT.GMT_MODIFIED is '更新时间';

comment on column COMMENT.CONTENT is '评论内容';

