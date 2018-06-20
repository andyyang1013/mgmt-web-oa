/**初始化sql*/

USE `oa`;

/*Table structure for table `t_oauth` */

DROP TABLE IF EXISTS `t_oauth`;

CREATE TABLE `t_oauth` (
  `id`                 bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `user_info_id`       bigint(20)  DEFAULT NULL
  COMMENT '用户基础信息id',
  `oauth_type`         varchar(20) DEFAULT NULL
  COMMENT 'OAuth认证类型',
  `oauth_id`           varchar(64) DEFAULT NULL
  COMMENT 'OAuth认证id',
  `oauth_access_token` varchar(20) DEFAULT NULL
  COMMENT 'OAuth认证token',
  `oauth_expires`      bigint(20)  DEFAULT NULL
  COMMENT 'OAuth认证过期时间，单位毫秒',
  `create_time`        datetime    DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`         bigint(20)  DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`        datetime    DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`         bigint(20)  DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_oauth_access_token` (`oauth_access_token`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='OAuth2.0认证表，包括微信、qq，新浪微博等';

/*Table structure for table `t_subsidiary_info` */

DROP TABLE IF EXISTS `t_subsidiary_info`;

CREATE TABLE `t_subsidiary_info` (
  `id`              bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `subsidiary_name` varchar(255) DEFAULT NULL
  COMMENT '子公司名称',
  `subsidiary_code` varchar(20)  DEFAULT NULL
  COMMENT '子公司标识，可以为公司名的简写，如乐盈电竞为dj',
  `api_key`         varchar(64)  DEFAULT NULL
  COMMENT 'apiKey',
  `api_secret`      varchar(64)  DEFAULT NULL
  COMMENT 'apiSecret',
  `state`           tinyint(2)   DEFAULT '0'
  COMMENT '子公司状态',
  `del_flag`        tinyint(1)   DEFAULT '0'
  COMMENT '删除状态，0 未删除 1 已删除 默认 0',
  `create_time`     datetime     DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`      bigint(20)   DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`     datetime     DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`      bigint(20)   DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_api_key` (`api_key`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='子公司信息管理';

/*Table structure for table `t_sys_permission` */

DROP TABLE IF EXISTS `t_sys_permission`;

CREATE TABLE `t_sys_permission` (
  `id`            bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `disabled`      tinyint(1)   DEFAULT '0'
  COMMENT '状态：0 正常 1 禁用，默认 0',
  `resource_code` varchar(100) DEFAULT NULL
  COMMENT '资源编码',
  `resource_name` varchar(255) DEFAULT NULL
  COMMENT '资源名称',
  `parent_id`     bigint(20)   DEFAULT NULL
  COMMENT '资源父ID',
  `resource_type` tinyint(2)   DEFAULT NULL
  COMMENT '资源类型 1菜单 2按钮',
  `url`           varchar(255) DEFAULT NULL
  COMMENT '资源url，类似/login',
  `create_time`   datetime     DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`    bigint(20)   DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`   datetime     DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`    bigint(20)   DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='系统权限资源表';

/*Table structure for table `t_sys_role` */

DROP TABLE IF EXISTS `t_sys_role`;

CREATE TABLE `t_sys_role` (
  `id`          bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `disabled`    tinyint(1)    DEFAULT '0'
  COMMENT '角色状态：0 正常 1 禁用，默认0',
  `system_type` tinyint(1)    DEFAULT '0'
  COMMENT '0非超级管理员 1超级管理员',
  `role_code`   varchar(50)   DEFAULT NULL
  COMMENT '角色编码',
  `role_name`   varchar(255)  DEFAULT NULL
  COMMENT '角色名称',
  `description` varchar(2000) DEFAULT NULL
  COMMENT '角色描述',
  `create_time` datetime      DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`  bigint(20)    DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time` datetime      DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`  bigint(20)    DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='系统角色表';

/*Table structure for table `t_sys_role_permission` */

DROP TABLE IF EXISTS `t_sys_role_permission`;

CREATE TABLE `t_sys_role_permission` (
  `id`            bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `role_id`       bigint(20)          NOT NULL
  COMMENT '角色ID',
  `permission_id` bigint(20)          NOT NULL
  COMMENT '权限ID',
  `create_time`   datetime   DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`    bigint(20) DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`   datetime   DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`    bigint(20) DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_permission_id` (`permission_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='角色和资源关系表';

/*Table structure for table `t_sys_user` */

DROP TABLE IF EXISTS `t_sys_user`;

CREATE TABLE `t_sys_user` (
  `id`              bigint(20)   NOT NULL
  COMMENT '主键ID',
  `account`         varchar(255) NOT NULL
  COMMENT '登录账号',
  `pwd`             varchar(255) NOT NULL
  COMMENT '登录密码',
  `salt`            varchar(255)          DEFAULT NULL
  COMMENT '加密的盐',
  `disabled`        tinyint(1)   NOT NULL DEFAULT '0'
  COMMENT '账号状态：0正常，1禁用',
  `locked`          tinyint(1)            DEFAULT '0'
  COMMENT '是否锁定：0正常，1锁定',
  `system_type`     tinyint(1)            DEFAULT '0'
  COMMENT '0非超级管理员 1超级管理员',
  `description`     varchar(1000)         DEFAULT NULL
  COMMENT '备注',
  `last_login_time` datetime              DEFAULT NULL
  COMMENT '最近登录时间',
  `create_time`     datetime              DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`      bigint(20)            DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`     datetime              DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`      bigint(20)            DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_account` (`account`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='系统用户表';

/*Table structure for table `t_sys_user_role` */

DROP TABLE IF EXISTS `t_sys_user_role`;

CREATE TABLE `t_sys_user_role` (
  `id`          bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `user_id`     bigint(20)          NOT NULL
  COMMENT '用户ID',
  `role_id`     bigint(20)          NOT NULL
  COMMENT '角色ID',
  `create_time` datetime   DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`  bigint(20) DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time` datetime   DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`  bigint(20) DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='用户和角色关系表';

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id`                 bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `user_info_id`       bigint(20)   DEFAULT NULL
  COMMENT '用户基础信息id',
  `account`            varchar(50)  DEFAULT NULL
  COMMENT '用户名/账号',
  `email`              varchar(50)  DEFAULT NULL
  COMMENT '邮箱',
  `phone`              varchar(20)  DEFAULT NULL
  COMMENT '手机号',
  `pwd`                varchar(128) DEFAULT NULL
  COMMENT '密码\r\n益彩(128)：PBKDF2\r\n一比分：MD5\r\n电竞：sha1+md5',
  `salt`               varchar(50)  DEFAULT NULL
  COMMENT '加密盐，只对新账号有效',
  `encrypt_type`       varchar(50)  DEFAULT NULL
  COMMENT '加密类型',
  `subsidiary_code`    varchar(50)  DEFAULT NULL
  COMMENT '子公司标识，注册来源',
  `subsidiary_user_id` varchar(50)  DEFAULT NULL
  COMMENT '子公司用户id，新注册的和主键保持一致',
  `reg_time`           datetime(3)  DEFAULT NULL
  COMMENT '注册时间',
  `last_login_time`    datetime(3)  DEFAULT NULL
  COMMENT '最近登录时间',
  `del_flag`           tinyint(1)   DEFAULT '0'
  COMMENT '删除状态，0 未删除 1 已删除 默认 0',
  `create_time`        datetime(3)  DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`         bigint(20)   DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`        datetime(3)  DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`         bigint(20)   DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_info_id` (`user_info_id`),
  KEY `idx_account` (`account`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='账户认证表';

/*Table structure for table `t_user_bind_relation` */

DROP TABLE IF EXISTS `t_user_bind_relation`;

CREATE TABLE `t_user_bind_relation` (
  `id`            bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `user_id`       bigint(20)          NOT NULL
  COMMENT '当前用户id',
  `bind_user_ids` varchar(200)        NOT NULL
  COMMENT '绑定的用户id集合',
  `del_flag`      tinyint(1)  DEFAULT '0'
  COMMENT '删除状态，0 未删除 1 已删除 默认 0',
  `create_time`   datetime(3) DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`    bigint(20)  DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`   datetime(3) DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`    bigint(20)  DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_user_id` (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='用户账号绑定关系表';

/*Table structure for table `t_user_history_import_control` */

DROP TABLE IF EXISTS `t_user_history_import_control`;

CREATE TABLE `t_user_history_import_control` (
  `id`           bigint(20) unsigned NOT NULL
  COMMENT '主键ID，由于是底层，可以作为业务条件传入',
  `import_time`  datetime   DEFAULT CURRENT_TIMESTAMP
  COMMENT '导入时间',
  `import_num`   bigint(20) DEFAULT NULL
  COMMENT '导入记录条数',
  `state`        tinyint(2) DEFAULT '1'
  COMMENT '导入状态，0失败 1成功',
  `consume_time` bigint(20) DEFAULT NULL
  COMMENT '导入耗时，单位毫秒',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='用户历史数据导入控制表';

/*Table structure for table `t_user_info` */

DROP TABLE IF EXISTS `t_user_info`;

CREATE TABLE `t_user_info` (
  `id`              bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `nick_name`       varchar(50) CHARACTER SET utf8mb4
  COLLATE utf8mb4_bin            DEFAULT NULL
  COMMENT '用户昵称',
  `sex`             tinyint(1)   DEFAULT '0'
  COMMENT '性别,0 保密1 男 2 女 默认为0',
  `image`           varchar(255) DEFAULT NULL
  COMMENT '头像url地址',
  `true_name`       varchar(50)  DEFAULT NULL
  COMMENT '真实姓名',
  `id_number`       varchar(20)  DEFAULT NULL
  COMMENT '身份证号',
  `id_number_state` tinyint(1)   DEFAULT '0'
  COMMENT '身份证号验证状态,0 未验证 1已验证 默认 0',
  `create_time`     datetime(3)  DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`      bigint(20)   DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`     datetime(3)  DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`      bigint(20)   DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='账户扩展信息表';

/*Table structure for table `t_user_modify_record` */

DROP TABLE IF EXISTS `t_user_modify_record`;

CREATE TABLE `t_user_modify_record` (
  `id`               bigint(20) unsigned NOT NULL
  COMMENT '主键ID',
  `user_id`          bigint(20)    DEFAULT NULL
  COMMENT '修改用户id',
  `resource_content` varchar(1000) DEFAULT NULL
  COMMENT '原对象内容',
  `modify_content`   varchar(1000) DEFAULT NULL
  COMMENT '修改后的内容',
  `subsidiary_code`  varchar(20)   DEFAULT NULL
  COMMENT '子公司标识',
  `create_time`      datetime(3)   DEFAULT NULL
  COMMENT '创建时间',
  `create_uid`       bigint(20)    DEFAULT NULL
  COMMENT '创建用户Id',
  `update_time`      datetime(3)   DEFAULT NULL
  COMMENT '更新时间',
  `update_uid`       bigint(20)    DEFAULT NULL
  COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='用户信息修改记录表';

/*Table structure for table `t_user_temp` */

DROP TABLE IF EXISTS `t_user_temp`;

CREATE TABLE `t_user_temp` (
  `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT
  COMMENT '主键ID',
  `account`            varchar(200)                 DEFAULT NULL
  COMMENT '用户名/账号',
  `email`              varchar(200)                 DEFAULT NULL
  COMMENT '邮箱',
  `phone`              varchar(20)                  DEFAULT NULL
  COMMENT '手机号',
  `pwd`                varchar(128)                 DEFAULT NULL
  COMMENT '密码',
  `salt`               varchar(100)                 DEFAULT NULL
  COMMENT '加密盐，只对新账号有效',
  `subsidiary_code`    varchar(50)                  DEFAULT NULL
  COMMENT '子公司标识',
  `subsidiary_user_id` varchar(50)                  DEFAULT NULL
  COMMENT '子公司用户id，新注册的和主键保持一致',
  `reg_time`           datetime(3)                  DEFAULT NULL
  COMMENT '注册时间',
  `last_login_time`    datetime(3)                  DEFAULT NULL
  COMMENT '最近登录时间',
  `nick_name`          varchar(200) CHARACTER SET utf8mb4
  COLLATE utf8mb4_bin                               DEFAULT NULL
  COMMENT '用户昵称',
  `sex`                tinyint(1)                   DEFAULT '0'
  COMMENT '性别,0 保密1 男 2 女 默认为0',
  `image`              varchar(255)                 DEFAULT NULL
  COMMENT '头像url地址',
  `true_name`          varchar(200)                 DEFAULT NULL
  COMMENT '真实姓名',
  `id_number`          varchar(20)                  DEFAULT NULL
  COMMENT '身份证号',
  `id_number_state`    tinyint(1)                   DEFAULT '0'
  COMMENT '身份证验证状态',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 976930
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC
  COMMENT ='用户信息临时表，为了接收历史数据';


insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (969468898627825888, '华体星空', 'ht', 'e1a2c53d16666ece99cd63f669350e5e', '2a370730be48489b9633c362ba5ea756', 1, 0,
  '2018-03-05 14:55:23', 968051929257107458, '2018-03-05 14:55:26', 968051929257107458);
insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (969468898627825889, '一比分', 'ybf', 'e1a2c53d16666ece99cd63f669350e52', '2a370730be48489b9633c362ba5ea752', 1, 0,
  '2018-03-05 14:55:23', 968051929257107458, '2018-03-19 16:38:59', 968051929257107458);
insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (969468898627825890, '乐盈电竞', 'dj', 'e1a2c53d16666ece99cd63f669350e23', '2a370730be48489b9633c262ba5ea752', 1, 0,
  '2018-03-05 14:55:23', 968051929257107458, '2018-03-19 16:53:27', 968051929257107458);
insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (969468898627825891, '益彩网络', 'yc', 'e1a2c53d166daece99cd63f669350e23', '2a370730be48bc9b9633c262ba5ea752', 1, 0,
  '2018-03-05 14:55:23', 968051929257107458, '2018-03-19 16:45:21', 968051929257107458);
insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (969468898627825892, '法义网络', 'fy', 'e1a1c43d166daece99cd63f669350e23', '3a370730be48bc9bd633c262ba5ea752', 0, 0,
  '2018-03-05 14:55:23', 968051929257107458, '2018-03-05 14:55:26', 968051929257107458);
insert into `t_subsidiary_info` (`id`, `subsidiary_name`, `subsidiary_code`, `api_key`, `api_secret`, `state`, `del_flag`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (969468898627825893, '柒壹思诺', 'qysn', 'e1a1c43d166daecebccd63f669350e23', '3a370730be48369bd633c262ba5ea752', 0, 0,
    '2018-03-05 14:55:23', 968051929257107458, '2018-03-05 14:55:26', 968051929257107458);


insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (1, 0, 'user:list', '会员账号', -1, 1, '/user/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (2, 0, 'subsidiary:list', '系统配置', -1, 1, '/subsidiary/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (3, 0, 'sysUser:list', '用户管理', -1, 1, '/sysUsr/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (4, 0, 'sysRole:list', '用户组管理', -1, 1, '/sysRole/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (5, 0, 'user:export', '会员列表导出', 1, 2, '/user/export', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (6, 0, 'user:update', '会员修改', 1, 2, '/user/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (7, 0, 'subsidiary:insert', '新增系统', 2, 2, '/subsidiary/insert', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (8, 0, 'subsidiary:update', '修改系统', 2, 2, '/subsidiary/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (9, 0, 'subsidiary:delete', '删除系统', 2, 2, '/subsidiary/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (10, 0, 'sysRole:insert', '新增用户组', 4, 2, '/sysRole/insert', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (11, 0, 'sysRole:update', '修改用户组', 4, 2, '/sysRole/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (12, 0, 'sysRole:delete', '删除用户组', 4, 2, '/sysRole/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (13, 0, 'sysRole:modifyType', '禁启用户组', 4, 2, '/sysRole/modifyType', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00',
    1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (14, 0, 'sysUser:insert', '新增用户', 3, 2, '/sysUser/insert', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (15, 0, 'sysUser:modifyType', '禁启用户', 3, 2, '/sysUser/modifyType', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00',
    1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (16, 0, 'sysUser:update', '修改用户', 3, 2, '/sysUser/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (17, 0, 'sysUser:updatePass', '修改用户密码', 3, 2, '/sysUser/updatePass', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00',
    1);
insert into `t_sys_permission` (`id`, `disabled`, `resource_code`, `resource_name`, `parent_id`, `resource_type`, `url`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (18, 0, 'sysUser:delete', '删除用户', 3, 2, '/sysUser/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1);

/*Data for the table `t_sys_role` */

insert into `t_sys_role` (`id`, `disabled`, `system_type`, `role_code`, `role_name`, `description`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970587013654847490, 0, 1, '5c4b70ca8df548c5af197e1bd770b2c5', '超级管理员', '超级管理员角色', '2018-03-05 17:09:11',
        968051929257107458, '2018-03-06 10:46:45', 968051929257107458);

/*Data for the table `t_sys_role_permission` */

insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467393, 970587013654847490, 1, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467394, 970587013654847490, 2, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467395, 970587013654847490, 3, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467396, 970587013654847490, 4, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467397, 970587013654847490, 5, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467398, 970587013654847490, 6, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467399, 970587013654847490, 7, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467400, 970587013654847490, 8, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467401, 970587013654847490, 9, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467402, 970587013654847490, 10, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467403, 970587013654847490, 11, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467404, 970587013654847490, 12, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467405, 970587013654847490, 13, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467406, 970587013654847490, 14, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467407, 970587013654847490, 15, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467408, 970587013654847490, 16, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467409, 970587013654847490, 17, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);
insert into `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970853156529467410, 970587013654847490, 18, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45',
        970587013654847490);

/*Data for the table `t_sys_user` */

insert into `t_sys_user` (`id`, `account`, `pwd`, `salt`, `disabled`, `locked`, `system_type`, `description`, `last_login_time`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values
  (968051929257107458, 'admin', '856fbf86e6910a074b7d4077a5fb924e33512afb', 'NRw3TkUowVu48HPyZ45tYg==', 0, 0, 1, '12',
                       '2018-03-26 14:31:06', '2018-02-26 17:15:40', 968051929257107458, '2018-03-26 14:31:06',
   968051929257107458);

/*Data for the table `t_sys_user_role` */

insert into `t_sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_uid`, `update_time`, `update_uid`)
values (970927794508816385, 968051929257107458, 970587013654847490, '2018-02-26 17:15:40', 968051929257107458,
        '2018-03-06 15:43:20', 968051929257107458);