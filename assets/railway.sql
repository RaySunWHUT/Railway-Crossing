# Host: localhost  (Version 5.7.27)
# Date: 2020-12-30 23:22:45
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "baluster"
#

DROP TABLE IF EXISTS `baluster`;
CREATE TABLE `baluster` (
  `balusterId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '栏木机编号',
  `balusterName` varchar(255) DEFAULT NULL COMMENT '栏木机名称',
  `balusterStatus` tinyint(2) unsigned zerofill NOT NULL COMMENT '栏木机状态',
  `crossingId` int(10) NOT NULL COMMENT '道口id',
  PRIMARY KEY (`balusterId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "baluster"
#

INSERT INTO `baluster` VALUES (1,'A1',01,1),(2,'A2',01,1),(3,'A3',01,1),(4,'A4',01,1),(5,'A5',01,1),(6,'A6',01,1),(7,'A7',01,1),(8,'A8',01,1),(9,'B1',00,2),(10,'B2',00,2),(11,'B3',00,2),(12,'B4',00,2),(13,'B5',00,2),(14,'B6',00,2),(15,'B7',00,2),(16,'C1',00,3),(17,'C2',00,3);

#
# Structure for table "crossing"
#

DROP TABLE IF EXISTS `crossing`;
CREATE TABLE `crossing` (
  `crossingId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '路口编号',
  `crossingName` varchar(255) DEFAULT NULL COMMENT '路口名称',
  `crossingGPS` varchar(255) DEFAULT NULL COMMENT '路口GPS',
  `crossingMode` tinyint(1) unsigned zerofill NOT NULL COMMENT '路口状态(自动0，手动1)',
  PRIMARY KEY (`crossingId`) USING BTREE,
  KEY `crossingId` (`crossingId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "crossing"
#

INSERT INTO `crossing` VALUES (1,'凌志西',NULL,0),(2,'石头村',NULL,0),(3,'石头油库',NULL,0);

#
# Structure for table "instruction"
#

DROP TABLE IF EXISTS `instruction`;
CREATE TABLE `instruction` (
  `InstructionId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '指令编号',
  `userId` int(10) NOT NULL COMMENT '用户编号',
  `crossingId` int(10) NOT NULL COMMENT '道口编号',
  `locomotiveId` varchar(25) DEFAULT NULL COMMENT '机车编号',
  `instructionContent` varchar(255) DEFAULT NULL COMMENT '指令内容',
  `instructionTime` datetime DEFAULT NULL COMMENT '指令发布时间',
  `passTime` int(11) DEFAULT NULL COMMENT '机车通过时间（秒）',
  `valid` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '是否有效，0为无效，1为有效',
  PRIMARY KEY (`InstructionId`) USING BTREE,
  KEY `crossingId` (`crossingId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "instruction"
#

INSERT INTO `instruction` VALUES (1,1,1,'1','允许通过','2020-12-15 21:57:44',10,1);

#
# Structure for table "locomotive"
#

DROP TABLE IF EXISTS `locomotive`;
CREATE TABLE `locomotive` (
  `locomotiveId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '机车编号',
  `locomotiveName` varchar(255) DEFAULT NULL COMMENT '机车名称',
  `trackId` varchar(10) DEFAULT NULL COMMENT '股道编码',
  `responseStatus` tinyint(1) unsigned DEFAULT NULL COMMENT '机车应答状态(0为不通过，1为通过)',
  `locomotiveGPS` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`locomotiveId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "locomotive"
#

INSERT INTO `locomotive` VALUES (1,'东风0313','E3A',0,0);

#
# Structure for table "railwayblinker"
#

DROP TABLE IF EXISTS `railwayblinker`;
CREATE TABLE `railwayblinker` (
  `railwayBlinkerId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '铁路同行信号灯编码',
  `railwayBlinkerName` varchar(255) DEFAULT NULL COMMENT '铁路同行信号灯名称',
  `railwayBlinkerStatus` tinyint(2) unsigned zerofill DEFAULT NULL COMMENT '铁路同行信号灯状态',
  `crossingId` int(10) DEFAULT NULL COMMENT '道口编号',
  PRIMARY KEY (`railwayBlinkerId`) USING BTREE,
  KEY `crossingId` (`crossingId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "railwayblinker"
#

INSERT INTO `railwayblinker` VALUES (1,'遮断信号灯1',01,1),(2,'遮断信号灯2',01,1),(3,'遮断信号灯1',00,2),(4,'遮断信号灯2',00,2),(5,'遮断信号灯1',00,3),(6,'遮断信号灯2',00,3);

#
# Structure for table "roadblinker"
#

DROP TABLE IF EXISTS `roadblinker`;
CREATE TABLE `roadblinker` (
  `roadBlinkerId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '公路信号灯编码',
  `roadBlinkerName` varchar(255) DEFAULT NULL COMMENT '公路信号灯名称',
  `roadBlinkerStatus` tinyint(2) unsigned zerofill DEFAULT NULL COMMENT '公路信号灯状态',
  `crossingId` int(10) DEFAULT NULL COMMENT '道口编号',
  PRIMARY KEY (`roadBlinkerId`) USING BTREE,
  KEY `crossingId` (`crossingId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "roadblinker"
#

INSERT INTO `roadblinker` VALUES (1,'信号灯1',00,1),(2,'信号灯2',00,1),(3,'信号灯1',01,2),(4,'信号灯2',01,2),(5,'信号灯1',00,3),(6,'信号灯2',00,3);

#
# Structure for table "role"
#

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "role"
#

INSERT INTO `role` VALUES (1,'admin'),(2,'superAdmin');

#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` int(10) NOT NULL DEFAULT '0' COMMENT '用户id',
  `userAccount` varchar(25) NOT NULL COMMENT '用户账户',
  `userName` varchar(25) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) NOT NULL COMMENT '盐值',
  `roleId` int(11) NOT NULL DEFAULT '0',
  `lastLoginTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`userId`,`userAccount`) USING BTREE,
  KEY `userId` (`userId`) USING BTREE,
  KEY `roleId` (`roleId`),
  CONSTRAINT `roleId` FOREIGN KEY (`roleId`) REFERENCES `role` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "user"
#

INSERT INTO `user` VALUES (0,'275518','Sun','69787342046C20606FB92644C4DABE52','88F8BC99-9B91-4533-AD4B-AAF259EA976C',1,'2020-12-30 15:35:41'),(1,'271880',NULL,'6B2E362E4B344941937D461553C7A578','39322103-CD73-4D50-8CEB-C3E6746931EF',1,'0000-00-00 00:00:00'),(5,'271881',NULL,'9D4A0F970CBE0429DECAB7468D5ADEDA','20D49FFE-EFC7-4C27-A5AF-69D02B0CD9D0',1,'0000-00-00 00:00:00');

#
# Structure for table "warning"
#

DROP TABLE IF EXISTS `warning`;
CREATE TABLE `warning` (
  `warningId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '警报id',
  `warningContent` varchar(255) DEFAULT NULL COMMENT '警报内容',
  `crossingId` int(10) unsigned zerofill DEFAULT NULL COMMENT '道口id(若为0则为RFID错误）',
  PRIMARY KEY (`warningId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

#
# Data for table "warning"
#

