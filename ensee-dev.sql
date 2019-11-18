/*
Navicat MySQL Data Transfer

Source Server         : mysql5.6
Source Server Version : 50643
Source Host           : 127.0.0.1:3306
Source Database       : ensee-dev

Target Server Type    : MYSQL
Target Server Version : 50643
File Encoding         : 65001

Date: 2019-11-18 21:18:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bgm
-- ----------------------------
DROP TABLE IF EXISTS `bgm`;
CREATE TABLE `bgm` (
  `id` varchar(64) NOT NULL,
  `author` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bgm
-- ----------------------------
INSERT INTO `bgm` VALUES ('1', '炫神', '我疯狂的炫', '\\bgm\\炫！疯狂的炫.mp3');
INSERT INTO `bgm` VALUES ('180530DXKK4YYGTC', 'Ayasa绚沙', '告白之夜', '\\bgm\\告白之夜.mp3');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` varchar(20) NOT NULL,
  `father_comment_id` varchar(20) DEFAULT NULL,
  `to_user_id` varchar(20) DEFAULT NULL,
  `video_id` varchar(20) NOT NULL COMMENT '视频id',
  `from_user_id` varchar(20) NOT NULL COMMENT '留言者，评论的用户id',
  `comment` text NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评论表';

-- ----------------------------
-- Records of comments
-- ----------------------------

-- ----------------------------
-- Table structure for search_records
-- ----------------------------
DROP TABLE IF EXISTS `search_records`;
CREATE TABLE `search_records` (
  `id` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL COMMENT '搜索的内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频搜索的记录表';

-- ----------------------------
-- Records of search_records
-- ----------------------------
INSERT INTO `search_records` VALUES ('1', '李赣');
INSERT INTO `search_records` VALUES ('2', '李赣');
INSERT INTO `search_records` VALUES ('3', '李赣');
INSERT INTO `search_records` VALUES ('34fc839a-36f6-4a8b-8815-ca53492ac224', '带篮子');
INSERT INTO `search_records` VALUES ('4', '李赣');
INSERT INTO `search_records` VALUES ('5', '带篮子');
INSERT INTO `search_records` VALUES ('6', '带篮子');
INSERT INTO `search_records` VALUES ('7', 'AE86');
INSERT INTO `search_records` VALUES ('8', 'AE86');
INSERT INTO `search_records` VALUES ('81d0d589-dc34-46fd-b3cb-81bf491c7508', 'AE86');
INSERT INTO `search_records` VALUES ('9', 'AE86');
INSERT INTO `search_records` VALUES ('9ca649ff-0d3c-4a06-aa34-285759bfce76', 'AE86');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) DEFAULT NULL COMMENT '我的头像，如果没有默认给一张',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `fans_counts` int(11) DEFAULT '0' COMMENT '我的粉丝数量',
  `follow_counts` int(11) DEFAULT '0' COMMENT '我关注的人总数',
  `receive_like_counts` int(11) DEFAULT '0' COMMENT '我接受到的赞美/收藏 的数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('191114F38G8HC8SW', 'admin', '3Eg+gKegvZ73HYz5c2c5JA==', '/191114F38G8HC8SW/face/tmp_368829a908f28db526291c1cc7a3669d.jpg', 'admin', '6', '2', '14');
INSERT INTO `users` VALUES ('191117BTT31CN3F8', 'test', 'CY9rzUYh03PK3k6DJie09g==', '/191117BTT31CN3F8/face/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.erjG5OdAnvlh3b5065daed48d449e1b2ed9b0dd58fd4.JPG', 'test', '0', '4', '1');

-- ----------------------------
-- Table structure for users_fans
-- ----------------------------
DROP TABLE IF EXISTS `users_fans`;
CREATE TABLE `users_fans` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `fan_id` varchar(64) NOT NULL COMMENT '粉丝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户粉丝关联关系表';

-- ----------------------------
-- Records of users_fans
-- ----------------------------

-- ----------------------------
-- Table structure for users_like_videos
-- ----------------------------
DROP TABLE IF EXISTS `users_like_videos`;
CREATE TABLE `users_like_videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `video_id` varchar(64) NOT NULL COMMENT '视频',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_video_rel` (`user_id`,`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户喜欢的/赞过的视频';

-- ----------------------------
-- Records of users_like_videos
-- ----------------------------
INSERT INTO `users_like_videos` VALUES ('123123', '191114F38G8HC8SW', '191115164870620184576');
INSERT INTO `users_like_videos` VALUES ('191117122485099986944', '191114F38G8HC8SW', '191116106144676708352');
INSERT INTO `users_like_videos` VALUES ('191117154743710154752', '191114F38G8HC8SW', '191116106164582875136');
INSERT INTO `users_like_videos` VALUES ('1', '191114F38G8HC8SW', '191116106173661446144');
INSERT INTO `users_like_videos` VALUES ('191117154685770039296', '191114F38G8HC8SW', '191116106180085022720');
INSERT INTO `users_like_videos` VALUES ('191117154647029350400', '191114F38G8HC8SW', '191116106207060688896');
INSERT INTO `users_like_videos` VALUES ('191117154631728529408', '191114F38G8HC8SW', '191116106214251823104');
INSERT INTO `users_like_videos` VALUES ('191117154617421758464', '191114F38G8HC8SW', '191116106220702662656');
INSERT INTO `users_like_videos` VALUES ('191117154607202336768', '191114F38G8HC8SW', '191116106333462331392');
INSERT INTO `users_like_videos` VALUES ('191117154597926633472', '191114F38G8HC8SW', '191116106348771540992');
INSERT INTO `users_like_videos` VALUES ('191117154586843185152', '191114F38G8HC8SW', '191117136132025122816');

-- ----------------------------
-- Table structure for users_report
-- ----------------------------
DROP TABLE IF EXISTS `users_report`;
CREATE TABLE `users_report` (
  `id` varchar(64) NOT NULL,
  `deal_user_id` varchar(64) NOT NULL COMMENT '被举报用户id',
  `deal_video_id` varchar(64) NOT NULL,
  `title` varchar(128) NOT NULL COMMENT '类型标题，让用户选择，详情见 枚举',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `userid` varchar(64) NOT NULL COMMENT '举报人的id',
  `create_date` datetime NOT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报用户表';

-- ----------------------------
-- Records of users_report
-- ----------------------------
INSERT INTO `users_report` VALUES ('180521FZ501ABDYW', '180425CFA4RB6T0H', '180510CD0A6K3SRP', '引人不适', '不合时宜的视频', '180518CKMAAM5TXP', '2018-05-21 20:58:35');
INSERT INTO `users_report` VALUES ('180521FZK44ZRWX4', '180425CFA4RB6T0H', '180510CD0A6K3SRP', '引人不适', '不合时宜的视频', '180518CKMAAM5TXP', '2018-05-21 20:59:53');
INSERT INTO `users_report` VALUES ('180521FZR1TYRTXP', '180425CFA4RB6T0H', '180510CD0A6K3SRP', '辱骂谩骂', 't4t43', '180518CKMAAM5TXP', '2018-05-21 21:00:18');

-- ----------------------------
-- Table structure for videos
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '发布者id',
  `audio_id` varchar(64) DEFAULT NULL COMMENT '用户使用音频的信息',
  `video_desc` varchar(128) DEFAULT NULL COMMENT '视频描述',
  `video_path` varchar(255) NOT NULL COMMENT '视频存放的路径',
  `video_seconds` float(6,2) DEFAULT NULL COMMENT '视频秒数',
  `video_width` int(6) DEFAULT NULL COMMENT '视频宽度',
  `video_height` int(6) DEFAULT NULL COMMENT '视频高度',
  `cover_path` varchar(255) DEFAULT NULL COMMENT '视频封面图',
  `like_counts` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢/赞美的数量',
  `status` int(1) NOT NULL COMMENT '视频状态：\r\n1、发布成功\r\n2、禁止播放，管理员操作',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

-- ----------------------------
-- Records of videos
-- ----------------------------
INSERT INTO `videos` VALUES ('191115164870620184576', '191114F38G8HC8SW', '180530DXKK4YYGTC', '李赣', '/191114F38G8HC8SW/video/tmp_d65d0c183570cd4fd7bbb0ab7e632d4e.mp452a0783f-b625-4b46-ab48-71473b5f18e5.mp4', '8.00', '720', '544', '/191114F38G8HC8SW/video/7ec197f4-f260-43a4-93fc-70ad1300565b.jpg', '0', '1', '2019-11-15 21:50:16');
INSERT INTO `videos` VALUES ('191116106144676708352', '191114F38G8HC8SW', '', '带篮子', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.JVlQibnYJpCac7c3dfc6a80ed412ce6691633e59b759.MP4', '8.57', '368', '640', '/191114F38G8HC8SW/video/917b4077-39ab-4363-9c01-c0c5f9a7b8fd.jpg', '3', '1', '2019-11-16 14:03:34');
INSERT INTO `videos` VALUES ('191116106164582875136', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.63XLaLiuTYTB13d27948523a50481e018ef77f86ce9a.MP4', '9.51', '540', '960', '/191114F38G8HC8SW/video/60e7b9fa-0d16-4dfa-a5c6-dfdb3b54b616.jpg', '1', '1', '2019-11-16 14:03:43');
INSERT INTO `videos` VALUES ('191116106173661446144', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.9EWl2jHCQU940cf6ed86e2a22ea43a4374f436e57acd.MP4', '3.04', '540', '960', '/191114F38G8HC8SW/video/d71e6a4a-1496-496c-8860-abc3f1a42397.jpg', '0', '1', '2019-11-16 14:03:48');
INSERT INTO `videos` VALUES ('191116106180085022720', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.uYusQ0jNNbyaca0569e9242a28e8e5a47765da5b4757.MP4', '3.10', '540', '960', '/191114F38G8HC8SW/video/a24f5383-0c72-4248-90b7-9f1076c32585.jpg', '1', '1', '2019-11-16 14:03:51');
INSERT INTO `videos` VALUES ('191116106187674615808', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.0I6Hik9ToOyt5643a67d6bbe2a75ce09b073fb0bb4aa.MP4', '4.60', '720', '1280', '/191114F38G8HC8SW/video/891589a4-34d0-4e6e-b4aa-9cd9da006911.jpg', '0', '1', '2019-11-16 14:03:54');
INSERT INTO `videos` VALUES ('191116106195218071552', '191114F38G8HC8SW', '', 'AE86', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.0JjSkJ91CIQ3b4fa6fd038da74a9a5fb1a6b68df48af.MP4', '9.70', '960', '544', '/191114F38G8HC8SW/video/887dac55-4f63-499e-a5c5-fc80cbb8aa1a.jpg', '0', '1', '2019-11-16 14:03:58');
INSERT INTO `videos` VALUES ('191116106207060688896', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.JANej1MELAyT6f83fa992fc2330cf7b3ce5ca6407d3f.MP4', '9.00', '404', '720', '/191114F38G8HC8SW/video/fd1d26ea-2506-4d82-92b1-c870f1771c64.jpg', '1', '1', '2019-11-16 14:04:03');
INSERT INTO `videos` VALUES ('191116106214251823104', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.7KMCQwxaFb8k5797bccac5b4efe63ace2115717e1b00.MP4', '5.40', '404', '720', '/191114F38G8HC8SW/video/d2aaa763-8414-4055-a228-cd1b325929c1.jpg', '1', '1', '2019-11-16 14:04:07');
INSERT INTO `videos` VALUES ('191116106220702662656', '191114F38G8HC8SW', '', '', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.QsmTJ9jVNB6c6bdac83cc0f58aeebaff265c19a46021.MP4', '10.19', '852', '480', '/191114F38G8HC8SW/video/9d67a6dc-de72-42ed-ba01-d5b2ce3c8770.jpg', '1', '1', '2019-11-16 14:04:10');
INSERT INTO `videos` VALUES ('191116106333462331392', '191114F38G8HC8SW', '180530DXKK4YYGTC', '5675', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.mRJyCmTNRo9s5643a67d6bbe2a75ce09b073fb0bb4aa.MP44cc5e351-e999-4e9c-b040-7a3aa205f659.mp4', '4.60', '720', '1280', '/191114F38G8HC8SW/video/16baa425-d224-4346-aa14-2298ea141637.jpg', '1', '1', '2019-11-16 14:05:04');
INSERT INTO `videos` VALUES ('191116106348771540992', '191114F38G8HC8SW', '180530DXKK4YYGTC', 'AE86', '/191114F38G8HC8SW/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.Ya4A5BrnPZdub4fa6fd038da74a9a5fb1a6b68df48af.MP4c8b4a842-b76d-4624-91d5-a2e02102502d.mp4', '9.70', '960', '544', '/191114F38G8HC8SW/video/436dcf97-b122-43c3-8201-55aaf7041d5c.jpg', '1', '1', '2019-11-16 14:05:11');
INSERT INTO `videos` VALUES ('191117136132025122816', '191117BTT31CN3F8', '1', '', '/191117BTT31CN3F8/video/wxf05e200306a3f147.o6zAJsz9V3l2IQf3u4MJAtqnhf1Y.NDv2eRJcmXiv5797bccac5b4efe63ace2115717e1b00.MP4e40ff45c-8ccd-49fb-9572-414d90a0da86.mp4', '5.40', '404', '720', '/191117BTT31CN3F8/video/924ecf37-fa5b-4ef5-93be-fc8b248dd933.jpg', '1', '1', '2019-11-17 18:01:53');
