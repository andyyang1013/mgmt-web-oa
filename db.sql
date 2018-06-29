-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.22-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 oa 的数据库结构
CREATE DATABASE IF NOT EXISTS `oa` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `oa`;

-- 导出  表 oa.t_sys_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_permission` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `disabled` tinyint(1) DEFAULT '0' COMMENT '状态：0 正常 1 禁用，默认 0',
  `resource_code` varchar(100) DEFAULT NULL COMMENT '资源编码',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '资源父ID',
  `resource_type` tinyint(2) DEFAULT NULL COMMENT '资源类型 1菜单 2按钮',
  `url` varchar(255) DEFAULT NULL COMMENT '资源url，类似/login',
  `path` varchar(255) DEFAULT NULL COMMENT '路由地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新用户id',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `redirect` varchar(255) DEFAULT NULL COMMENT '主路由跳转地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `menu_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统权限资源表';

-- 正在导出表  oa.t_sys_permission 的数据：~0 rows (大约)
DELETE FROM `t_sys_permission`;
/*!40000 ALTER TABLE `t_sys_permission` DISABLE KEYS */;
INSERT INTO `t_sys_permission` (`id`, `disabled`, `resource_code`, `name`, `parent_id`, `resource_type`, `url`, `path`, `create_time`, `create_uid`, `update_time`, `update_uid`, `component`, `redirect`, `icon`, `menu_id`) VALUES
	(1, 0, 'user:list', '会员列表', 19, 1, '/user/list', '/user/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'user/user', NULL, '', NULL),
	(2, 0, 'subsidiary:list', '系统配置', 20, 1, '/sys/subsidiary/list', '/subsidiary/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'sys/subsidiary', NULL, '', NULL),
	(3, 0, 'sysUser:list', '用户管理', 20, 1, '/sys/sysUsr/list', '/sysUsr/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'sys/sysUser', NULL, '', NULL),
	(4, 0, 'sysRole:list', '用户组管理', 20, 1, '/sys/sysRole/list', '/sysRole/list', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'sys/sysRole', NULL, '', NULL),
	(5, 0, 'user:export', '会员列表导出', 1, 2, '/user/export', '/user/export', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(6, 0, 'user:update', '会员修改', 1, 2, '/user/update', '/user/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(7, 0, 'subsidiary:insert', '新增系统', 2, 2, '/subsidiary/insert', '/subsidiary/insert', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(8, 0, 'subsidiary:update', '修改系统', 2, 2, '/subsidiary/update', '/subsidiary/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(9, 0, 'subsidiary:delete', '删除系统', 2, 2, '/subsidiary/delete', '/subsidiary/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(10, 0, 'sysRole:insert', '新增用户组', 4, 2, '/sysRole/insert', '/sysRole/insert', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(11, 0, 'sysRole:update', '修改用户组', 4, 2, '/sysRole/update', '/sysRole/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(12, 0, 'sysRole:delete', '删除用户组', 4, 2, '/sysRole/delete', '/sysRole/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(13, 0, 'sysRole:modifyType', '禁启用户组', 4, 2, '/sysRole/modifyType', '/sysRole/modifyType', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(14, 0, 'sysUser:update', '用户修改', 3, 2, '/sysUser/update', '/sysUser/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, '', '', NULL, NULL),
	(15, 0, 'sysUser:modifyType', '禁启用户', 3, 2, '/sysUser/modifyType', '/sysUser/modifyType', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(16, 0, 'sysUser:update', '用户详情', 3, 2, '/sysUser/detial', '/sysUser/detial', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'sys/sysUserDetial', NULL, '', NULL),
	(17, 0, 'sysUser:updatePass', '修改用户密码', 3, 2, '/sysUser/updatePass', '/sysUser/updatePass', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(18, 0, 'sysUser:delete', '删除用户', 3, 2, '/sysUser/delete', '/sysUser/delete', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, NULL, NULL, 'home', NULL),
	(19, 0, 'user:*', '会员管理', -1, 1, '/user', '/user', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'layout/Layout', '', 'customer', NULL),
	(20, 0, 'sys:*', '系统管理', -1, 1, '/sys', '/sys', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, 'layout/Layout', '', 'sys', NULL),
	(21, 0, NULL, '首页', -1, 1, '/', '/', '2018-06-26 17:52:51', 1, '2018-06-26 17:52:56', 1, 'dashboard/index', 'dashboard', 'home', NULL),
	(22, 0, 'sysUser:insert', '新增用户', 3, 2, '/sysUser/update', '/sysUser/update', '2018-02-26 00:00:00', 1, '2018-02-26 00:00:00', 1, '', NULL, '', NULL);
/*!40000 ALTER TABLE `t_sys_permission` ENABLE KEYS */;

-- 导出  表 oa.t_sys_role 结构
CREATE TABLE IF NOT EXISTS `t_sys_role` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `disabled` tinyint(1) DEFAULT '0' COMMENT '角色状态：0 正常 1 禁用，默认0',
  `system_type` tinyint(1) DEFAULT '0' COMMENT '0非超级管理员 1超级管理员',
  `role_code` varchar(50) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(2000) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- 正在导出表  oa.t_sys_role 的数据：~0 rows (大约)
DELETE FROM `t_sys_role`;
/*!40000 ALTER TABLE `t_sys_role` DISABLE KEYS */;
INSERT INTO `t_sys_role` (`id`, `disabled`, `system_type`, `role_code`, `role_name`, `description`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES
	(970587013654847490, 0, 1, '5c4b70ca8df548c5af197e1bd770b2c5', '超级管理员', '超级管理员角色', '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 968051929257107458),
	(1012511731653693442, 0, 0, '41957bcc3d014da79e9393fe011eff2b', 'test', 'test', '2018-06-29 09:43:03', 968051929257107459, '2018-06-29 09:43:03', 968051929257107459);
/*!40000 ALTER TABLE `t_sys_role` ENABLE KEYS */;

-- 导出  表 oa.t_sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_permission` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色和资源关系表';

-- 正在导出表  oa.t_sys_role_permission 的数据：~0 rows (大约)
DELETE FROM `t_sys_role_permission`;
/*!40000 ALTER TABLE `t_sys_role_permission` DISABLE KEYS */;
INSERT INTO `t_sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES
	(970853156529467393, 970587013654847490, 1, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467394, 970587013654847490, 2, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467395, 970587013654847490, 3, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467396, 970587013654847490, 4, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467397, 970587013654847490, 5, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467398, 970587013654847490, 6, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467399, 970587013654847490, 7, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467400, 970587013654847490, 8, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467401, 970587013654847490, 9, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467402, 970587013654847490, 10, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467403, 970587013654847490, 11, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467404, 970587013654847490, 12, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467405, 970587013654847490, 13, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467406, 970587013654847490, 14, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467407, 970587013654847490, 15, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467408, 970587013654847490, 16, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467409, 970587013654847490, 17, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467410, 970587013654847490, 18, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467411, 970587013654847490, 19, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467412, 970587013654847490, 20, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(970853156529467413, 970587013654847490, 22, '2018-03-05 17:09:11', 968051929257107458, '2018-03-06 10:46:45', 970587013654847490),
	(1012511731708219394, 1012511731653693442, 14, '2018-06-29 09:43:03', 968051929257107459, '2018-06-29 09:43:03', 1012511731653693442),
	(1012511731708219395, 1012511731653693442, 16, '2018-06-29 09:43:03', 968051929257107459, '2018-06-29 09:43:03', 1012511731653693442),
	(1012511731708219396, 1012511731653693442, 22, '2018-06-29 09:43:03', 968051929257107459, '2018-06-29 09:43:03', 1012511731653693442);
/*!40000 ALTER TABLE `t_sys_role_permission` ENABLE KEYS */;

-- 导出  表 oa.t_sys_user 结构
CREATE TABLE IF NOT EXISTS `t_sys_user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `account` varchar(255) NOT NULL COMMENT '登录账号',
  `pwd` varchar(255) NOT NULL COMMENT '登录密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '加密的盐',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '账号状态：0正常，1禁用',
  `locked` tinyint(1) DEFAULT '0' COMMENT '是否锁定：0正常，1锁定',
  `system_type` tinyint(1) DEFAULT '0' COMMENT '0非超级管理员 1超级管理员',
  `description` varchar(1000) DEFAULT NULL COMMENT '备注',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- 正在导出表  oa.t_sys_user 的数据：~0 rows (大约)
DELETE FROM `t_sys_user`;
/*!40000 ALTER TABLE `t_sys_user` DISABLE KEYS */;
INSERT INTO `t_sys_user` (`id`, `account`, `pwd`, `salt`, `disabled`, `locked`, `system_type`, `description`, `last_login_time`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES
	(968051929257107458, 'admin', '856fbf86e6910a074b7d4077a5fb924e33512afb', 'NRw3TkUowVu48HPyZ45tYg==', 1, 0, 1, '22211', '2018-06-26 19:20:02', '2018-02-26 17:15:40', 968051929257107458, '2018-06-28 15:14:28', 968051929257107459),
	(968051929257107459, 'zxx', 'b65d573a238b45b367ecacb70aa7d70106b44cb5', 'tVzqaS9XX8Vu9C8q/YVx8Q==', 0, 0, 1, '12', '2018-06-29 17:31:21', '2018-02-26 17:15:40', 968051929257107458, '2018-06-29 17:31:21', 968051929257107459),
	(1012230854424023041, 'test', 'e0ca49e5d9385fef2db0d037cf556be0214faafd', '7MdtzB6+FwuTQoN9wWpEMg==', 0, 0, 0, 'test', '2018-06-28 15:06:55', '2018-06-28 15:06:56', 968051929257107459, '2018-06-28 15:06:56', 968051929257107459);
/*!40000 ALTER TABLE `t_sys_user` ENABLE KEYS */;

-- 导出  表 oa.t_sys_user_role 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_role` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户和角色关系表';

-- 正在导出表  oa.t_sys_user_role 的数据：~0 rows (大约)
DELETE FROM `t_sys_user_role`;
/*!40000 ALTER TABLE `t_sys_user_role` DISABLE KEYS */;
INSERT INTO `t_sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES
	(970927794508816386, 968051929257107459, 970587013654847490, '2018-02-26 17:15:40', 968051929257107458, '2018-03-06 15:43:20', 968051929257107458),
	(1012230854918950914, 1012230854424023041, 970587013654847490, '2018-06-28 15:06:56', 968051929257107459, '2018-06-28 15:06:56', 968051929257107459),
	(1012232745405009921, 968051929257107458, 970587013654847490, '2018-02-26 17:15:40', 968051929257107459, '2018-06-28 15:14:28', 968051929257107459);
/*!40000 ALTER TABLE `t_sys_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
