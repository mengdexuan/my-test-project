/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : my_test_project

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2019-07-07 10:41:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `dict_key` varchar(255) NOT NULL COMMENT '字典key',
  `dict_value` varchar(2000) NOT NULL COMMENT '字典值',
  `dict_note` varchar(500) DEFAULT NULL COMMENT '备注',
  `enable` smallint(1) DEFAULT NULL COMMENT '是否可用：0：不可用  1：可用',
  `order_no` int(11) DEFAULT NULL COMMENT '排序字段',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `m_key_unique` (`dict_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';
