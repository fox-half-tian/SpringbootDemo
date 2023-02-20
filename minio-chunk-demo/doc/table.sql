create database minio_demo;

CREATE TABLE service_media_file
(
    `id`               BIGINT UNSIGNED PRIMARY KEY COMMENT '主键id（雪花算法）',
    `file_name`        VARCHAR(255)    NOT NULL COMMENT '文件名称',
    `file_type`        CHAR(2)         NOT NULL COMMENT '文件类型：文本，图片，音频，视频，其它',
    `file_format`      VARCHAR(128)    NOT NULL COMMENT '文件格式',
    `tag`              VARCHAR(32)     NOT NULL COMMENT '标签',
    `bucket`           VARCHAR(32)     NOT NULL COMMENT '存储桶',
    `file_path`        VARCHAR(512)    NOT NULL COMMENT '文件存储路径',
    `file_md5`         CHAR(32)        NOT NULL UNIQUE COMMENT '文件的md5值',
    `file_byte_size`   BIGINT UNSIGNED NOT NULL COMMENT '文件的字节大小',
    `file_format_size` VARCHAR(24)     NOT NULL COMMENT '文件的格式大小',
    `user_id`          BIGINT          NOT NULL COMMENT '上传人id',
    `create_time`      DATETIME        NOT NULL COMMENT '创建时间（上传时间）',
    `update_time`      DATETIME        NOT NULL COMMENT '修改时间'
) ENGINE = INNODB
  CHARACTER SET = utf8mb4 COMMENT '第三方服务-媒资文件表';