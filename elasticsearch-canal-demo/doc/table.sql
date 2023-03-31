# 如果存在 es_demo 数据库则删除
DROP DATABASE IF EXISTS `es_demo`;

# 创建新数据库
CREATE DATABASE `es_demo`;

# 使用数据库
USE `es_demo`;

# 创建一张用户表，注意自增主键id是从1000开始
CREATE TABLE `user`(
                       `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
                       `username` VARCHAR(24) NOT NULL COMMENT '用户名',
                       `icon` VARCHAR(255) NOT NULL COMMENT '头像url',
                       PRIMARY KEY(`id`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

# 创建一张博客表，注意自增主键id是从1000开始
CREATE TABLE `blog`(
                       `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
                       `user_id` INT NOT NULL COMMENT '用户id（雪花算法生成）',
                       `title` VARCHAR(255) NOT NULL COMMENT '标题',
                       `tags` VARCHAR(64) NOT NULL COMMENT '标签',
                       `introduce` VARCHAR(512) NOT NULL COMMENT '介绍',
                       `content` TEXT NOT NULL COMMENT '文章内容',
                       `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                       PRIMARY KEY(`id`),
                       KEY `idx_user_create`(`user_id`,`create_time` DESC)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='博客信息表';