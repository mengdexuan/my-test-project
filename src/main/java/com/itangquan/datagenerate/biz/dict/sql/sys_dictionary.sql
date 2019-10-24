/*
Navicat MySQL Data Transfer

Source Server         : 192.168.4.192(江苏稳定版)
Source Server Version : 50726
Source Host           : 192.168.4.192:3306
Source Database       : pbank

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-10-24 15:22:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `dict_key` varchar(255) NOT NULL COMMENT '字典key',
  `dict_value` varchar(2000) NOT NULL COMMENT '字典值',
  `dict_note` varchar(500) DEFAULT NULL COMMENT '备注',
  `enable` smallint(1) DEFAULT NULL COMMENT '是否可用：0：不可用  1：可用',
  `order_no` int(11) DEFAULT NULL COMMENT '排序字段',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `m_key_unique` (`dict_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';
