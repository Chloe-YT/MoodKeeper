/*
Navicat MySQL Data Transfer

Source Server         : moodkeeper_db
Source Server Version : 50734
Source Host           : localhost:3306
Source Database       : moodkeeper_db

Target Server Type    : MYSQL
Target Server Version : 50734
File Encoding         : 65001

Date: 2021-05-11 16:57:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for diary
-- ----------------------------
DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `mood_id` int(3) unsigned NOT NULL DEFAULT '1',
  `weather_id` int(3) unsigned NOT NULL DEFAULT '1',
  `category_id` int(3) unsigned NOT NULL DEFAULT '1',
  `diary_name` varchar(255) NOT NULL,
  `diary_content` varchar(255) NOT NULL,
  `diary_date` datetime NOT NULL,
  `state` int(3) unsigned NOT NULL,
  `anchor` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of diary
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '姓名',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '性别 1-男，2-女',
  `age` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '年龄',
  `telephone` varchar(11) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '手机号',
  `register_mode` varchar(8) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '注册方式',
  `third_part_id` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '第三方id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `KEY_UNIQUE_PHONE` (`telephone`) USING BTREE COMMENT '手机号唯一'
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('29', 'zyt', '2', '21', '13107936123', 'byphone', '', '2021-05-11 16:55:36', '2021-05-11 16:55:36');
INSERT INTO `user_info` VALUES ('30', 'zzz', '2', '20', '13107936111', 'byphone', '', '2021-05-11 16:56:10', '2021-05-11 16:56:10');

-- ----------------------------
-- Table structure for user_password
-- ----------------------------
DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `encrypt_password` varchar(255) NOT NULL DEFAULT '' COMMENT '加密后的密码',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '关联用户id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_password
-- ----------------------------
INSERT INTO `user_password` VALUES ('29', '4QrcOUm6Wau+VuBX8g+IPg==', '29', '2021-05-11 16:55:36', '2021-05-11 16:55:36');
INSERT INTO `user_password` VALUES ('30', '4QrcOUm6Wau+VuBX8g+IPg==', '30', '2021-05-11 16:56:10', '2021-05-11 16:56:10');
