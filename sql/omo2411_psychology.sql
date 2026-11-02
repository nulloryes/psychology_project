/*
 Navicat Premium Data Transfer

 Source Server         : localhost@3306
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : omo2411_psychology

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 31/10/2026 17:34:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_dify
-- ----------------------------
DROP TABLE IF EXISTS `ai_dify`;
CREATE TABLE `ai_dify`  (
  `ai_id` int NOT NULL AUTO_INCREMENT COMMENT 'AI管理id',
  `dify_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'dify工作流编号',
  `dify_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'dify链接',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int NULL DEFAULT 0 COMMENT '状态(0-未启用,1-删除,2-启用)',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ai_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_dify
-- ----------------------------
INSERT INTO `ai_dify` VALUES (1, 's0hrJaKVPQYenY5L', 'http://localhost', '省token版', 0, '2026-08-21 16:59:36', '2026-08-21 16:59:36');
INSERT INTO `ai_dify` VALUES (2, 'cXdhvczOeM6YihM9', 'http://localhost', '全血版', 2, '2026-08-21 16:59:36', '2026-08-21 16:59:36');
INSERT INTO `ai_dify` VALUES (3, 'test1111111', 'https://cloud.dify.ai/apps', '测试', 1, '2026-08-21 12:45:16', '2026-08-21 12:45:16');

-- ----------------------------
-- Table structure for consultant
-- ----------------------------
DROP TABLE IF EXISTS `consultant`;
CREATE TABLE `consultant`  (
  `consultant_id` int NOT NULL AUTO_INCREMENT COMMENT '咨询师id',
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位',
  `project` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业背景',
  `profile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `rate_score` int NULL DEFAULT NULL COMMENT '评分',
  `price` double NULL DEFAULT NULL COMMENT '价格',
  `admin_id` int NOT NULL COMMENT '后台id',
  `head_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`consultant_id`) USING BTREE,
  INDEX `admin_id`(`admin_id` ASC) USING BTREE,
  CONSTRAINT `consultant_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `hou_admin` (`admin_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of consultant
-- ----------------------------
INSERT INTO `consultant` VALUES (1, '火星星际医科大学', '主任医师', '神经心理学', '首席心理师，心理知识丰富，书写多项国际科研成果论文，2077年心理研究最高荣誉奖获得者', 5, 1000, 2, NULL, '2026-03-07 15:10:46', '2026-08-06 13:57:28');
INSERT INTO `consultant` VALUES (2, '中国医科大学', '副主任医师', '社会心理学', '最受好评的心理咨询师', 4, 190, 3, NULL, '2026-03-07 15:12:24', '2026-08-06 11:16:37');
INSERT INTO `consultant` VALUES (3, '罗德岛医药大学', '副主任医师', '创伤心理学', '一位非常非常有资历的心理教授', 4, 300, 8, NULL, '2026-03-20 22:09:52', '2026-08-06 11:16:38');
INSERT INTO `consultant` VALUES (6, '闽台国际医科大学', '医师', '人际心理学', '看看我吧', 3, 100, 9, NULL, '2026-03-24 20:05:02', '2026-08-06 11:16:57');

-- ----------------------------
-- Table structure for consultant_domain
-- ----------------------------
DROP TABLE IF EXISTS `consultant_domain`;
CREATE TABLE `consultant_domain`  (
  `consultant_domain_id` int NOT NULL AUTO_INCREMENT COMMENT '关联表id',
  `consultant_id` int NOT NULL COMMENT '咨询师id',
  `domain_id` int NOT NULL COMMENT '领域id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`consultant_domain_id`) USING BTREE,
  INDEX `consultant_id`(`consultant_id` ASC) USING BTREE,
  INDEX `domain_id`(`domain_id` ASC) USING BTREE,
  CONSTRAINT `consultant_domain_ibfk_1` FOREIGN KEY (`consultant_id`) REFERENCES `consultant` (`consultant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `consultant_domain_ibfk_2` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`domain_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of consultant_domain
-- ----------------------------
INSERT INTO `consultant_domain` VALUES (1, 1, 1, '2026-03-07 15:17:07', '2026-03-07 15:17:10');
INSERT INTO `consultant_domain` VALUES (2, 2, 2, '2026-03-07 15:17:18', '2026-03-07 15:17:21');
INSERT INTO `consultant_domain` VALUES (3, 2, 3, '2026-03-17 20:02:53', NULL);
INSERT INTO `consultant_domain` VALUES (4, 1, 4, '2026-03-17 20:03:17', NULL);
INSERT INTO `consultant_domain` VALUES (5, 1, 5, '2026-03-17 20:03:31', NULL);
INSERT INTO `consultant_domain` VALUES (6, 2, 4, '2026-03-17 20:03:51', NULL);
INSERT INTO `consultant_domain` VALUES (7, 3, 1, '2026-03-23 22:13:23', NULL);
INSERT INTO `consultant_domain` VALUES (14, 3, 2, '2026-03-23 23:25:10', NULL);
INSERT INTO `consultant_domain` VALUES (16, 6, 3, '2026-03-24 20:05:02', NULL);

-- ----------------------------
-- Table structure for domain
-- ----------------------------
DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain`  (
  `domain_id` int NOT NULL AUTO_INCREMENT COMMENT '领域id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领域名称',
  `status` int NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`domain_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domain
-- ----------------------------
INSERT INTO `domain` VALUES (1, '头疼', 0, '2026-03-07 15:16:03', '2026-03-17 20:01:40');
INSERT INTO `domain` VALUES (2, '失眠', 0, '2026-03-07 15:16:46', '2026-03-17 20:01:46');
INSERT INTO `domain` VALUES (3, '焦虑', 0, '2026-03-15 01:15:23', '2026-03-17 20:01:57');
INSERT INTO `domain` VALUES (4, '躯体化', 0, '2026-03-17 20:02:07', NULL);
INSERT INTO `domain` VALUES (5, '神经敏感', 0, '2026-03-17 20:02:21', NULL);

-- ----------------------------
-- Table structure for evaluation
-- ----------------------------
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE `evaluation`  (
  `evaluation_id` int NOT NULL AUTO_INCREMENT COMMENT '评估id',
  `user_id` int NOT NULL COMMENT '用户id',
  `domain_id` int NOT NULL COMMENT '领域id',
  `tip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报告结果',
  `money` double NULL DEFAULT NULL COMMENT '费用',
  `score` int NULL DEFAULT NULL COMMENT '分数',
  `status` int NULL DEFAULT 0 COMMENT '状态（0-未评论，1-已评论）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`evaluation_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `domain_id`(`domain_id` ASC) USING BTREE,
  CONSTRAINT `evaluation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluation_ibfk_3` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`domain_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of evaluation
-- ----------------------------
INSERT INTO `evaluation` VALUES (1, 2, 2, '您的心理状态良好，请继续保持积极的生活态度。', 50, 80, 0, '2026-03-08 09:10:26', '2026-03-18 18:29:34');
INSERT INTO `evaluation` VALUES (2, 3, 1, '您可能存在轻度的心理压力，建议关注自己的情绪变化。', 50, 90, 0, '2026-03-15 00:53:27', '2026-03-18 18:29:37');
INSERT INTO `evaluation` VALUES (5, 2, 1, '您的心理状态良好，请继续保持积极的生活态度。', 50, 95, 0, '2026-03-18 19:08:27', NULL);
INSERT INTO `evaluation` VALUES (6, 2, 1, '您的心理状态良好，请继续保持积极的生活态度。', 50, 95, 0, '2026-03-18 19:13:14', NULL);
INSERT INTO `evaluation` VALUES (7, 1, 2, '好好休息', 50, 88, 0, '2026-03-20 23:41:05', NULL);
INSERT INTO `evaluation` VALUES (8, 1, 2, '您可能存在轻度的心理压力，建议关注自己的情绪变化。', 50, 63, 0, '2026-04-08 23:42:54', NULL);
INSERT INTO `evaluation` VALUES (9, 3, 1, '您的心理压力较大，建议寻求专业帮助。', 50, 43, 0, '2026-04-09 14:15:33', NULL);
INSERT INTO `evaluation` VALUES (10, 3, 2, '您的心理压力较大，建议寻求专业帮助。', 50, 57, 0, '2026-04-09 14:15:43', NULL);
INSERT INTO `evaluation` VALUES (11, 3, 2, '您的心理状态良好，请继续保持积极的生活态度。', 50, 91, 0, '2026-04-09 14:55:32', NULL);

-- ----------------------------
-- Table structure for evaluation_report
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_report`;
CREATE TABLE `evaluation_report`  (
  `report_id` int NOT NULL AUTO_INCREMENT COMMENT '报告id',
  `evaluation_id` int NULL DEFAULT NULL COMMENT '咨询id',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询师建议',
  `consultant_id` int NULL DEFAULT NULL COMMENT '咨询师id',
  `status` int NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`report_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of evaluation_report
-- ----------------------------
INSERT INTO `evaluation_report` VALUES (1, 1, '无心理症状，你非常无敌健康', 1, 0, '2026-03-08 09:10:03', '2026-03-18 16:03:17');
INSERT INTO `evaluation_report` VALUES (2, 2, '心理症状在正常状态内，注意休息', 2, 0, '2026-03-08 09:19:22', '2026-03-18 16:03:20');

-- ----------------------------
-- Table structure for hou_admin
-- ----------------------------
DROP TABLE IF EXISTS `hou_admin`;
CREATE TABLE `hou_admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '后台人员id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `status` int NULL DEFAULT 1 COMMENT '状态（0-审核通过，1-未审核）',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除（1删除）',
  `role_id` int NOT NULL DEFAULT 2 COMMENT '角色id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`admin_id`) USING BTREE,
  INDEX `role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `hou_admin_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `hou_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hou_admin
-- ----------------------------
INSERT INTO `hou_admin` VALUES (1, 'admin@123', '123', '超级管理员', 0, 1180, 0, 1, '2026-03-05 13:38:51', '2026-04-09 14:55:56');
INSERT INTO `hou_admin` VALUES (2, 'doctor@123', '123', 'Doctor H', 0, 210, 0, 3, '2026-03-05 13:42:59', '2026-03-18 01:56:54');
INSERT INTO `hou_admin` VALUES (3, 'hahaha', '123', '晓丽', 0, 288, 0, 3, '2026-03-07 15:13:48', '2026-04-09 15:01:03');
INSERT INTO `hou_admin` VALUES (8, 'doctor', '111', 'Miss ff0', 0, 0, 0, 3, '2026-03-20 22:09:52', NULL);
INSERT INTO `hou_admin` VALUES (9, 'aaa', '222', '李昊游', 1, 0, 0, 2, '2026-03-24 00:09:38', '2026-04-09 16:07:04');

-- ----------------------------
-- Table structure for hou_log
-- ----------------------------
DROP TABLE IF EXISTS `hou_log`;
CREATE TABLE `hou_log`  (
  `log_id` int NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `type` int NULL DEFAULT NULL COMMENT '类型（1-新增用户，2-新增医师，3-新增咨询，4-新增答题）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hou_log
-- ----------------------------
INSERT INTO `hou_log` VALUES (1, 1, NULL, '2026-03-17 19:42:37');
INSERT INTO `hou_log` VALUES (2, 1, NULL, '2026-03-17 19:42:45');
INSERT INTO `hou_log` VALUES (3, 2, NULL, '2026-03-17 19:42:53');
INSERT INTO `hou_log` VALUES (4, 2, NULL, '2026-03-20 22:09:52');
INSERT INTO `hou_log` VALUES (5, 4, NULL, '2026-03-20 23:41:05');
INSERT INTO `hou_log` VALUES (6, 2, NULL, '2026-03-24 13:48:47');
INSERT INTO `hou_log` VALUES (7, 2, NULL, '2026-03-24 20:05:02');
INSERT INTO `hou_log` VALUES (8, 3, NULL, '2026-04-08 23:42:36');
INSERT INTO `hou_log` VALUES (9, 4, NULL, '2026-04-08 23:42:54');
INSERT INTO `hou_log` VALUES (10, 3, NULL, '2026-04-09 14:15:10');
INSERT INTO `hou_log` VALUES (11, 4, NULL, '2026-04-09 14:15:33');
INSERT INTO `hou_log` VALUES (12, 4, NULL, '2026-04-09 14:15:43');
INSERT INTO `hou_log` VALUES (13, 3, NULL, '2026-04-09 14:55:20');
INSERT INTO `hou_log` VALUES (14, 4, NULL, '2026-04-09 14:55:32');
INSERT INTO `hou_log` VALUES (15, 2, NULL, '2026-04-09 14:57:14');
INSERT INTO `hou_log` VALUES (16, 3, NULL, '2026-04-09 14:57:51');
INSERT INTO `hou_log` VALUES (17, 2, NULL, '2026-08-06 14:45:36');

-- ----------------------------
-- Table structure for hou_role
-- ----------------------------
DROP TABLE IF EXISTS `hou_role`;
CREATE TABLE `hou_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` int NULL DEFAULT 0 COMMENT '状态（1为禁用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hou_role
-- ----------------------------
INSERT INTO `hou_role` VALUES (1, '超级管理员', '可查看管理大部分信息', 0, '2026-03-05 13:37:12', '2026-03-24 22:52:31');
INSERT INTO `hou_role` VALUES (2, '普通管理员', '刚注册的管理员账户，无实用功能', 0, '2026-03-05 13:37:36', '2026-03-24 22:53:07');
INSERT INTO `hou_role` VALUES (3, '咨询师', '负责给用户做咨询，可查看自己的咨询管理', 0, '2026-03-05 13:37:54', '2026-03-24 22:54:22');
INSERT INTO `hou_role` VALUES (4, '111', '111', 1, '2026-03-25 01:04:42', '2026-03-25 01:16:48');
INSERT INTO `hou_role` VALUES (5, '测试员', '111', 0, '2026-04-07 20:36:31', NULL);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `up_id` int NULL DEFAULT NULL COMMENT '父级id',
  `order` int NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `status` int NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '人员管理', 0, 3, 'el-icon-notebook-2 menu-icon', 0, NULL, '2026-04-04 21:13:08');
INSERT INTO `menu` VALUES (2, '首页', 0, 1, 'el-icon-s-home menu-icon', 0, NULL, '2026-04-04 21:13:10');
INSERT INTO `menu` VALUES (3, '用户管理', 1, 1, 'el-icon-user menu-icon', 0, NULL, '2026-04-04 21:13:11');
INSERT INTO `menu` VALUES (4, '咨询师管理', 1, 2, 'el-icon-s-custom menu-icon', 0, NULL, '2026-04-04 21:13:12');
INSERT INTO `menu` VALUES (5, '咨询管理', 0, 4, 'el-icon-message menu-icon', 0, NULL, '2026-04-09 15:08:17');
INSERT INTO `menu` VALUES (6, '咨询查询', 5, 1, 'el-icon-document menu-icon', 0, NULL, '2026-04-04 21:13:16');
INSERT INTO `menu` VALUES (7, '咨询诊断', 5, 2, 'el-icon-reading menu-icon', 0, NULL, '2026-04-04 21:13:17');
INSERT INTO `menu` VALUES (8, '题库管理', 5, 4, 'el-icon-question menu-icon', 0, NULL, '2026-04-04 21:13:18');
INSERT INTO `menu` VALUES (9, '系统管理', 0, 5, 'el-icon-setting menu-icon', 0, NULL, '2026-04-04 21:13:19');
INSERT INTO `menu` VALUES (10, '菜单管理', 9, 1, 'el-icon-menu menu-icon', 0, NULL, '2026-04-04 21:13:20');
INSERT INTO `menu` VALUES (11, '权限配置', 1, 3, 'el-icon-lock menu-icon', 0, NULL, '2026-04-04 21:13:21');
INSERT INTO `menu` VALUES (12, '日志查询', 9, 3, 'el-icon-notebook-2 menu-icon', 0, NULL, '2026-04-04 21:13:22');
INSERT INTO `menu` VALUES (14, '个人主页', 0, 2, 'el-icon-user menu-icon', 0, NULL, '2026-04-09 15:08:46');
INSERT INTO `menu` VALUES (16, '预约时间设置', 5, 3, 'el-icon-date menu-icon', 0, NULL, '2026-04-04 21:13:26');
INSERT INTO `menu` VALUES (17, 'AI管理', 9, 2, 'el-icon-date menu-icon', 0, '2026-08-19 17:38:29', '2026-08-19 17:38:32');

-- ----------------------------
-- Table structure for money_flow
-- ----------------------------
DROP TABLE IF EXISTS `money_flow`;
CREATE TABLE `money_flow`  (
  `money_flow_id` int NOT NULL AUTO_INCREMENT COMMENT '流水id',
  `tip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事项',
  `user_id` int NOT NULL COMMENT '用户id',
  `consultant_id` int NULL DEFAULT NULL COMMENT '咨询师id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `money` double NULL DEFAULT NULL COMMENT '金额',
  `status` int NULL DEFAULT 0 COMMENT '是否支付（1为已支付）',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`money_flow_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `consultant_id`(`consultant_id` ASC) USING BTREE,
  CONSTRAINT `money_flow_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `money_flow_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `consultant` (`consultant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of money_flow
-- ----------------------------
INSERT INTO `money_flow` VALUES (1, '答题咨询', 2, NULL, '微信', 50, 1, '2026-03-14 17:41:41');
INSERT INTO `money_flow` VALUES (2, '心理咨询', 2, 2, '支付宝', 190, 1, '2026-03-14 16:58:31');
INSERT INTO `money_flow` VALUES (3, '心理咨询', 3, 1, '钱包', 800, 1, '2026-03-18 01:20:22');
INSERT INTO `money_flow` VALUES (4, '答题咨询', 3, NULL, '钱包', 50, 1, '2026-03-14 17:42:38');
INSERT INTO `money_flow` VALUES (5, '答题咨询', 3, NULL, '钱包', 50, 1, '2026-04-09 14:16:29');
INSERT INTO `money_flow` VALUES (6, '账户充值', 3, NULL, '支付宝', 50, 1, '2026-03-18 01:30:12');
INSERT INTO `money_flow` VALUES (7, '心理咨询', 3, 2, '钱包', 190, 1, '2026-03-18 01:56:54');
INSERT INTO `money_flow` VALUES (8, '心理咨询', 3, 1, NULL, 800, 0, '2026-03-18 01:25:04');
INSERT INTO `money_flow` VALUES (9, '充值钱包', 3, NULL, '微信', 100, 1, '2026-03-18 01:46:45');
INSERT INTO `money_flow` VALUES (10, '充值钱包', 3, NULL, '支付宝', 100, 0, '2026-03-18 01:53:06');
INSERT INTO `money_flow` VALUES (11, '账户充值', 2, NULL, '微信', 100, 1, '2026-03-18 18:32:03');
INSERT INTO `money_flow` VALUES (12, '账户充值', 2, NULL, '支付宝', 500, 1, '2026-03-18 18:32:15');
INSERT INTO `money_flow` VALUES (15, '答题咨询', 2, NULL, NULL, 50, NULL, '2026-03-18 19:08:27');
INSERT INTO `money_flow` VALUES (16, '答题咨询', 2, NULL, NULL, 50, NULL, '2026-03-18 19:13:14');
INSERT INTO `money_flow` VALUES (17, '答题咨询', 1, NULL, '钱包', 50, 1, '2026-04-08 23:43:20');
INSERT INTO `money_flow` VALUES (18, '心理咨询', 3, 1, NULL, 1000, NULL, '2026-03-25 15:59:06');
INSERT INTO `money_flow` VALUES (19, '答题咨询', 1, NULL, NULL, 50, NULL, '2026-04-08 23:42:54');
INSERT INTO `money_flow` VALUES (20, '账户充值', 1, NULL, '微信', 200, 1, '2026-04-08 23:43:29');
INSERT INTO `money_flow` VALUES (21, '答题咨询', 3, NULL, NULL, 50, NULL, '2026-04-09 14:15:33');
INSERT INTO `money_flow` VALUES (22, '答题咨询', 3, NULL, NULL, 50, NULL, '2026-04-09 14:15:43');
INSERT INTO `money_flow` VALUES (23, '账户充值', 3, NULL, '微信', 200, 1, '2026-04-09 14:16:37');
INSERT INTO `money_flow` VALUES (24, '答题咨询', 3, NULL, '钱包', 50, 1, '2026-04-09 14:55:56');
INSERT INTO `money_flow` VALUES (25, '账户充值', 3, NULL, '微信', 50, 1, '2026-04-09 14:56:02');
INSERT INTO `money_flow` VALUES (26, '心理咨询', 3, 2, '钱包', 190, 1, '2026-04-09 15:01:03');
INSERT INTO `money_flow` VALUES (27, '账户充值', 2, NULL, '微信', 50, 1, '2026-08-06 09:48:19');

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` int NOT NULL AUTO_INCREMENT COMMENT '题目id',
  `domain_id` int NOT NULL COMMENT '领域id',
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目',
  `AQ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'A选项',
  `AS` int NULL DEFAULT NULL COMMENT 'A分数',
  `BQ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'B选项',
  `BS` int NULL DEFAULT NULL COMMENT 'B分数',
  `CQ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'C选项',
  `CS` int NULL DEFAULT NULL COMMENT 'C分数',
  `DQ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'D选项',
  `DS` int NULL DEFAULT NULL COMMENT 'D分数',
  `status` int NULL DEFAULT 0 COMMENT '状态（1禁用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`question_id`) USING BTREE,
  INDEX `domain_id`(`domain_id` ASC) USING BTREE,
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`domain_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 2, '你是否感到疲惫', '总是', 11, '经常', 32, '偶尔', 71, '从不', 98, 0, '2026-03-08 09:31:09', '2026-03-25 15:15:53');
INSERT INTO `question` VALUES (2, 2, '你有胡思乱想过吗', '总是', 10, '经常', 55, '偶尔', 83, '从不', 91, 0, '2026-03-08 09:33:12', '2026-03-18 15:52:18');
INSERT INTO `question` VALUES (3, 1, '感到大脑疼痛', '总是', 10, '经常', 43, '偶尔', 68, '很少', 95, 0, '2026-03-15 00:16:06', '2026-03-18 15:52:25');
INSERT INTO `question` VALUES (4, 3, '你是否对以前喜欢的活动（如爱好、运动、社交）失去了兴趣或愉悦感？', '几乎每天', 15, '经常 ', 38, '偶尔 ', 68, '从不', 87, 0, '2026-03-25 15:19:04', NULL);
INSERT INTO `question` VALUES (5, 3, '你是否感到紧张、焦虑或急切，且难以控制这种担忧？', '从不', 85, '偶尔', 60, '经常', 35, '几乎每天', 5, 0, '2026-03-25 16:55:25', NULL);
INSERT INTO `question` VALUES (6, 3, '你是否因为过度担心各种事情（如工作、健康、家庭琐事）而难以静下心来？', '几乎每天', 25, '经常', 55, '偶尔', 75, '从不', 82, 0, '2026-03-25 16:56:35', NULL);
INSERT INTO `question` VALUES (7, 3, '你是否容易出现身体上的紧张反应（如心跳加速、出汗、手抖、肌肉紧绷或呼吸急促）', '几乎每天', 40, '经常', 56, '偶尔', 75, '从不', 90, 0, '2026-03-25 16:57:38', NULL);
INSERT INTO `question` VALUES (8, 3, '是否感到开心', '总是', 90, '经常', 65, '偶尔', 30, '从不', 10, 0, '2026-04-09 14:59:31', NULL);

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `reservation_id` int NOT NULL AUTO_INCREMENT COMMENT '预约id',
  `user_id` int NOT NULL COMMENT '用户id',
  `domain_id` int NOT NULL COMMENT '领域id',
  `consultant_id` int NOT NULL COMMENT '咨询师id',
  `status` int NULL DEFAULT 0 COMMENT '状态（0未审核，1通过，2完成，3取消）',
  `money` double NULL DEFAULT 0 COMMENT '费用',
  `order_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预约日期',
  `problem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题描述',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专家回答',
  `rate_score` int NULL DEFAULT NULL COMMENT '评价打分',
  `rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户评价',
  `rate_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评价时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`reservation_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `domain_id`(`domain_id` ASC) USING BTREE,
  INDEX `consultant_id`(`consultant_id` ASC) USING BTREE,
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`domain_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`consultant_id`) REFERENCES `consultant` (`consultant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES (1, 2, 1, 1, 2, 1000, '2026-03-12 08:00:00', '做梦成真了', '是个很罕见的现象，需要持续观察', 4, '很不错', '2026-03-16 22:30:55.164', '2026-03-07 15:58:30', '2026-08-06 10:22:11');
INSERT INTO `reservation` VALUES (2, 2, 2, 2, 2, 199, '2026-03-12 10:00:00', '睡觉睡不着了', '', 3, '真的非常棒', '2026-04-09 15:43:58.307', '2026-03-08 15:17:36', '2026-08-06 10:22:13');
INSERT INTO `reservation` VALUES (3, 3, 1, 1, 2, 1000, '2026-03-13 14:00:00', '梦到自己灵魂出鞘了', '今天这个测试发现你有点焦虑过度，不过请注意，灵魂出窍后不要到处乱走', NULL, NULL, NULL, '2026-03-08 15:59:22', '2026-08-06 10:22:54');
INSERT INTO `reservation` VALUES (4, 3, 2, 2, 2, 190, '2026-04-28 16:00:00', '有点睡不着觉', '多锻炼得有一个健康生活', 5, '每次都预约这个咨询师，真的很有效果', '2026-04-09 15:46:01.107', '2026-03-15 14:25:22', '2026-08-06 10:22:18');
INSERT INTO `reservation` VALUES (5, 2, 2, 2, 1, 190, '2026-03-20 08:00:00', '还是睡不着啊，难受', NULL, NULL, NULL, NULL, '2026-03-17 23:35:58', '2026-08-06 10:17:11');
INSERT INTO `reservation` VALUES (6, 2, 1, 2, 3, 190, '2026-03-26 08:00:00', NULL, NULL, NULL, NULL, NULL, '2026-03-17 23:41:36', '2026-03-19 01:09:37');
INSERT INTO `reservation` VALUES (7, 2, 5, 2, 3, 190, '2026-03-27 10:00:00', '睡觉梦到醒来见到外星人', NULL, NULL, NULL, NULL, '2026-03-19 01:11:37', '2026-03-19 23:46:07');
INSERT INTO `reservation` VALUES (8, 2, 3, 1, 2, 1000, '2026-03-27 10:00:00', '难受害怕压抑难受难受难受', NULL, NULL, '', '', '2026-03-19 22:59:41', '2026-08-06 13:57:16');
INSERT INTO `reservation` VALUES (9, 1, 1, 2, 1, 190, '2026-04-09 10:00:00', '头疼欲裂', NULL, NULL, NULL, NULL, '2026-04-08 23:42:36', '2026-08-06 14:45:35');
INSERT INTO `reservation` VALUES (10, 3, 1, 2, 3, 190, '2026-04-11 14:00:00', '头痛的厉害', '', NULL, NULL, NULL, '2026-04-09 14:15:10', '2026-04-09 15:40:22');
INSERT INTO `reservation` VALUES (11, 3, 1, 2, 2, 190, '2026-04-15 14:00:00', '痛痛痛', '多睡睡觉，多锻炼身体，有个好身体', 5, '真的很有帮助！', '2026-04-09 15:18:18.462', '2026-04-09 14:55:20', '2026-08-06 10:22:29');
INSERT INTO `reservation` VALUES (12, 4, 1, 2, 0, 190, '2026-08-12 14:00:00', '这个是测试数据', NULL, NULL, NULL, NULL, '2026-08-10 17:52:41', '2026-08-10 17:52:41');
INSERT INTO `reservation` VALUES (16, 7, 2, 2, 0, 190, '2026-08-13 14:00:00', '因长期压力导致入睡困难，并伴有情绪低落', NULL, NULL, NULL, NULL, '2026-08-12 10:06:04', '2026-08-12 10:06:04');
INSERT INTO `reservation` VALUES (17, 7, 4, 1, 0, 1000, '2026-08-14 08:00:00', '因无明显生理病因导致的手臂游动性疼痛', NULL, NULL, NULL, NULL, '2026-08-12 10:07:36', '2026-08-12 10:07:36');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `divide_id` int NOT NULL AUTO_INCREMENT COMMENT '分配id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `status` int NULL DEFAULT 0 COMMENT '状态（0-分配，1-删除-不分配）',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`divide_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (15, 2, 2, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (16, 14, 2, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (17, 2, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (18, 5, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (19, 6, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (20, 7, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (21, 8, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (22, 12, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (23, 14, 3, 0, NULL, NULL);
INSERT INTO `role_menu` VALUES (26, 16, 3, 0, '2026-03-25 01:04:02', NULL);
INSERT INTO `role_menu` VALUES (37, 2, 5, 0, '2026-04-07 20:57:23', NULL);
INSERT INTO `role_menu` VALUES (38, 14, 5, 0, '2026-04-07 20:57:23', NULL);
INSERT INTO `role_menu` VALUES (39, 9, 5, 0, '2026-04-07 20:57:23', NULL);
INSERT INTO `role_menu` VALUES (40, 10, 5, 0, '2026-04-07 20:57:23', NULL);
INSERT INTO `role_menu` VALUES (54, 2, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (55, 14, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (56, 1, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (57, 3, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (58, 4, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (59, 11, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (60, 5, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (61, 6, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (62, 8, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (63, 9, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (64, 10, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (65, 12, 1, 0, '2026-04-09 15:07:32', NULL);
INSERT INTO `role_menu` VALUES (66, 17, 1, 0, '2026-08-19 17:39:22', '2026-08-19 17:39:24');

-- ----------------------------
-- Table structure for scheduling
-- ----------------------------
DROP TABLE IF EXISTS `scheduling`;
CREATE TABLE `scheduling`  (
  `scheduling_id` int NOT NULL AUTO_INCREMENT COMMENT '排班id',
  `consultant_id` int NOT NULL COMMENT '咨询师id',
  `begin_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最晚时间',
  `status` int NULL DEFAULT 0 COMMENT '预约状态（1已失效）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`scheduling_id`) USING BTREE,
  INDEX `consultantId`(`consultant_id` ASC) USING BTREE,
  CONSTRAINT `scheduling_ibfk_1` FOREIGN KEY (`consultant_id`) REFERENCES `consultant` (`consultant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduling
-- ----------------------------
INSERT INTO `scheduling` VALUES (1, 1, '2026-03-20', '2026-03-28 ', 0, '2026-03-08 09:34:06', '2026-03-18 23:21:19');
INSERT INTO `scheduling` VALUES (2, 2, '2026-03-08', '2026-09-11', 0, '2026-03-08 09:53:13', '2026-03-17 01:11:39');
INSERT INTO `scheduling` VALUES (4, 1, '2026-03-08', '2026-04-01', 0, '2026-03-08 10:17:30', NULL);
INSERT INTO `scheduling` VALUES (5, 1, '2026-05-20', '2027-05-30', 0, '2026-03-15 01:19:33', NULL);
INSERT INTO `scheduling` VALUES (7, 1, '2026-04-01', '2026-04-03', 0, '2026-03-25 18:39:38', NULL);
INSERT INTO `scheduling` VALUES (8, 2, '2026-09-25', '2026-10-07', 0, '2026-04-09 14:58:33', NULL);
INSERT INTO `scheduling` VALUES (9, 1, '2026-08-11', '2026-08-20', 0, '2026-08-06 10:40:40', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `status` int NULL DEFAULT 0 COMMENT '状态',
  `is_delete` int NULL DEFAULT 0 COMMENT '是否删除（1删除）',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123', '123', '张三', '男', 21, NULL, '18888888888', '学校', 0, 0, 250, '2026-03-04 17:54:25', '2026-04-08 23:43:28');
INSERT INTO `user` VALUES (2, 'user@123', '123', '维什戴尔', '女', 18, 'https://img1.baidu.com/it/u=1579569017,2166571907&fm=253&app=138&f=JPEG?w=623&h=623', '16666666666', '巴别塔', 0, 0, 750, '2026-03-04 18:43:09', '2026-08-06 09:48:19');
INSERT INTO `user` VALUES (3, 'huanzhou', '123456', '幻周', '男', 19, NULL, '17777777777', '222', 0, 0, 290, '2026-03-04 21:29:29', '2026-04-09 15:01:03');
INSERT INTO `user` VALUES (4, 'ababab', '123456', '玛卡巴卡', '女', 20, NULL, '11111111111', NULL, 0, 0, 0, '2026-03-04 23:45:50', '2026-03-17 10:51:55');
INSERT INTO `user` VALUES (7, '1', '1', '嗨嗨', '女', 23, NULL, '18473682937', NULL, 0, 0, 0, '2026-03-17 10:51:12', NULL);

SET FOREIGN_KEY_CHECKS = 1;
