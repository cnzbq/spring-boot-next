-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`      VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password_hash` VARCHAR(60)  NOT NULL COMMENT '密码',
    `email`         VARCHAR(191) NOT NULL COMMENT '邮箱',
    `avatar_url`    VARCHAR(50)  NOT NULL COMMENT '头像',
    `activated`     BOOLEAN      NOT NULL DEFAULT false COMMENT '是否激活',
    `created_by`    VARCHAR(50)  NOT NULL COMMENT '创建人',
    `created_at`    DATETIME     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
);

-- 初始化用户
INSERT INTO `sys_user`
VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator',
        'https://via.placeholder.com/250x250', true, 'admin', CURRENT_TIMESTAMP());
INSERT INTO `sys_user`
VALUES (2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User',
        'https://via.placeholder.com/250x250', true, 'admin', CURRENT_TIMESTAMP());

-- 权限表
CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`   BIGINT(20)  NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL COMMENT '权限',
    PRIMARY KEY (`name`)
);

-- 初始化权限
INSERT INTO `sys_role`
VALUES (1, 'ROLE_ADMIN');
INSERT INTO `sys_role`
VALUES (2, 'ROLE_USER');

-- 用户权限表
CREATE TABLE IF NOT EXISTS `sys_user_role`
(
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '权限ID'
);

-- 初始化用户权限
INSERT INTO `sys_user_role`
VALUES (1, 1);
INSERT INTO `sys_user_role`
VALUES (1, 2);
INSERT INTO `sys_user_role`
VALUES (2, 2);

