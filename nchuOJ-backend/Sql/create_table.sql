# 数据库初始化

-- 创建库
create database if not exists my_db;

-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;


-- 题目表
create table if not exists question
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    answer     text                               null comment '答案',
    submitNum  int      default 0                 not null comment '提交数',
    acceptedNum int    default 0                 not null comment '正确数',
    judgeCase  text                               null comment '判题用例 json数组',
    judgeConfig text                               null comment '判题配置 json数组',
    userId     bigint                             not null comment '创建用户 id',
    status   int      default 0                 not null comment '状态 1 公开/0 不公开',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '问题' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                        not null comment '提交代码语言类型',
    code       text                               not null comment '提交代码',
    judgeInfo text                              not null comment '判题信息(json对象)',
    status  int      default 0                 not null comment '判题状态',
    questionId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';
-- 考试题目关联表
create table if not exists exam_question
(
    id         bigint auto_increment comment 'id' primary key,
    examId     bigint                             not null comment '考试 id',
    questionId     bigint                             not null comment '题目 id',
    score       int      default 0                 not null comment '题目分数',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_examId (examId),
    index idx_questionId (questionId)
) comment '考试题目关联表';


-- 考试表
create table if not exists exam
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       not null comment '标题',
    description text                               not null comment '描述',
    startTime  datetime                             not null comment '开始时间',
    endTime    datetime                             not null comment '结束时间',
    status     int      default 0                     not null comment '状态 1 进行中/0 未开始/2 已结束',
    examQuestionIds varchar(1024)                      not null comment '考试题目关联列表（json 数组）',
    userId     bigint                              not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP  not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP  not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                  not null comment '是否删除',
    index idx_userId (userId)
) comment '考试' collate = utf8mb4_unicode_ci;

-- 考试成绩表
create table if not exists exam_result
(
    id         bigint auto_increment comment 'id' primary key,
    examId     bigint                             not null comment '考试 id',
    userId     bigint                             not null comment '用户 id',
    score      int      default 0                     not null comment '分数',
    createTime datetime default CURRENT_TIMESTAMP  not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP  not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                  not null comment '是否删除',
    index idx_examId (examId),
    index idx_userId (userId)
) comment '考试结果' collate = utf8mb4_unicode_ci;


-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
