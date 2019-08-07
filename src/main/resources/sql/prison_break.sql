/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : prison_break

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 07/08/2019 21:40:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源id',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传后的文件名',
  `file_origin_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原始文件名',
  `file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传后的路径',
  `file_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',
  `md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径md5加密',
  `valid` tinyint(2) NOT NULL COMMENT '文件是否有效(true/1: 有效 ;false/0: 无效)',
  `size` bigint(50) NOT NULL COMMENT '文件大小，单位B（Byte）',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '是否删除(true/1 : 已删除 ; false/0: 未删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_info
-- ----------------------------
INSERT INTO `file_info` VALUES ('62c15ad029acda12920260aa9b1f5beb', NULL, 'FN-f1WDyzyAmDW9nnQY.png', '02.lifecycle.png', 'D:\\prisonbreak-upload\\file\\20190609\\115635', 'image/png', '00bbcaf2d94b691d031097519835284f', 1, 457586, 0, '2019-06-09 03:56:36', '2019-06-09 03:56:36', 0);
INSERT INTO `file_info` VALUES ('63cdb800302afa77205176bca71023bc', NULL, 'FN-g1mQGY0IVJN3Kmvi.txt', 'ES6中箭头函数的用法.txt', 'D:\\prisonbreak-upload\\file\\20190609\\140402', 'text/plain', 'e1e7f074ddba448fa30f08e6c539e147', 0, 2226, 1, '2019-06-09 06:04:03', '2019-06-09 06:04:03', 0);
INSERT INTO `file_info` VALUES ('8caf257f3ae0c7ec1edff71ee48b2c5f', NULL, 'FN-bUu4nu9GO6u9jaF0.jpg', '03.vscode中vue插件.jpg', 'D:\\prisonbreak-upload\\file\\20190609\\115635', 'image/jpeg', '40342a552ce7ad7968e8837a631e452d', 1, 71656, 1, '2019-06-09 03:56:36', '2019-06-09 03:56:36', 0);
INSERT INTO `file_info` VALUES ('e8bf766e09cd8a79c6e4da4cb04b50a3', NULL, 'FN-fAsTM5Qa7mC7uzNs.png', '01.MVC和MVVM的关系图解.png', 'D:\\prisonbreak-upload\\file\\20190609\\115635', 'image/png', '15da72471447da43b2feb5e3ad5c72b3', 1, 76420, 0, '2019-06-09 03:56:35', '2019-06-09 03:56:35', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '别名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '是否删除(0:未删除 1:已删除)',
  `update_version` int(11) NULL DEFAULT 0 COMMENT '乐观锁',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '贾宝玉', '多情公子', '123456', 1, 1, '2019-05-16 06:18:14', '2019-07-16 06:39:19', NULL, 0, 0, 'baoyu@gmail.com', '19999999999');
INSERT INTO `sys_user` VALUES (2, '林黛玉', '颦儿', '123456', 1, 1, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 1, 'lindaiyu@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (3, '柳湘莲1', '冷二郎', '123456', 1, NULL, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (4, '柳湘莲2', '冷二郎', '123456', 2, NULL, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (5, '柳湘莲3', '冷二郎', '123456', 3, 3, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (6, '柳湘莲4', '冷二郎', '123456', 4, 4, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (7, '柳湘莲5', '冷二郎', '123456', 5, 5, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (8, '柳湘莲6', '冷二郎', '123456', 6, 6, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (9, '柳湘莲7', '冷二郎', '123456', 7, 7, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (10, '柳湘莲8', '冷二郎', '123456', 8, 8, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (11, '柳湘莲9', '冷二郎', '123456', 9, 9, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (12, '柳湘莲10', '冷二郎', '123456', 10, 10, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (13, '柳湘莲11', '冷二郎', '123456', 11, 11, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (14, '柳湘莲12', '冷二郎', '123456', 12, 12, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (15, '柳湘莲13', '冷二郎', '123456', 13, 13, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (16, '柳湘莲14', '冷二郎', '123456', 14, 14, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (17, '柳湘莲15', '冷二郎', '123456', 15, 15, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (18, '柳湘莲16', '冷二郎', '123456', 16, 16, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (19, '柳湘莲17', '冷二郎', '123456', 17, 17, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (20, '柳湘莲18', '冷二郎', '123456', 18, 18, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (21, '柳湘莲19', '冷二郎', '123456', 19, 19, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (22, '柳湘莲20', '冷二郎', '123456', 20, 20, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (23, '柳湘莲21', '冷二郎', '123456', 21, 21, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (24, '柳湘莲22', '冷二郎', '123456', 22, 22, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (25, '柳湘莲23', '冷二郎', '123456', 23, 23, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (26, '柳湘莲24', '冷二郎', '123456', 24, 24, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (27, '柳湘莲25', '冷二郎', '123456', 25, 25, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (28, '柳湘莲26', '冷二郎', '123456', 26, 26, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (29, '柳湘莲27', '冷二郎', '123456', 27, 27, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (30, '柳湘莲28', '冷二郎', '123456', 28, 28, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (31, '柳湘莲29', '冷二郎', '123456', 29, 29, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (32, '柳湘莲30', '冷二郎', '123456', 30, 30, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (33, '柳湘莲31', '冷二郎', '123456', 31, 31, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (34, '柳湘莲32', '冷二郎', '123456', 32, 32, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (35, '柳湘莲33', '冷二郎', '123456', 33, 33, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (36, '柳湘莲34', '冷二郎', '123456', 34, 34, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (37, '柳湘莲35', '冷二郎', '123456', 35, 35, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (38, '柳湘莲36', '冷二郎', '123456', 36, 36, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (39, '柳湘莲37', '冷二郎', '123456', 37, 37, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (40, '柳湘莲38', '冷二郎', '123456', 38, 38, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (41, '柳湘莲39', '冷二郎', '123456', 39, 39, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (42, '柳湘莲40', '冷二郎', '123456', 40, 40, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (43, '柳湘莲41', '冷二郎', '123456', 41, 41, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (44, '柳湘莲42', '冷二郎', '123456', 42, 42, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (45, '柳湘莲43', '冷二郎', '123456', 43, 43, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (46, '柳湘莲44', '冷二郎', '123456', 44, 44, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (47, '柳湘莲45', '冷二郎', '123456', 45, 45, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (48, '柳湘莲46', '冷二郎', '123456', 46, 46, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (49, '柳湘莲47', '冷二郎', '123456', 47, 47, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (50, '柳湘莲48', '冷二郎', '123456', 48, 48, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (51, '柳湘莲49', '冷二郎', '123456', 49, 49, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (52, '柳湘莲50', '冷二郎', '123456', 50, 50, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (53, '柳湘莲51', '冷二郎', '123456', 51, 51, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (54, '柳湘莲52', '冷二郎', '123456', 52, 52, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (55, '柳湘莲53', '冷二郎', '123456', 53, 53, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (56, '柳湘莲54', '冷二郎', '123456', 54, 54, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (57, '柳湘莲55', '冷二郎', '123456', 55, 55, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (58, '柳湘莲56', '冷二郎', '123456', 56, 56, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (59, '柳湘莲57', '冷二郎', '123456', 57, 57, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (60, '柳湘莲58', '冷二郎', '123456', 58, 58, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (61, '柳湘莲59', '冷二郎', '123456', 59, 59, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (62, '柳湘莲60', '冷二郎', '123456', 60, 60, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (63, '柳湘莲61', '冷二郎', '123456', 61, 61, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (64, '柳湘莲62', '冷二郎', '123456', 62, 62, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (65, '柳湘莲63', '冷二郎', '123456', 63, 63, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (66, '柳湘莲64', '冷二郎', '123456', 64, 64, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (67, '柳湘莲65', '冷二郎', '123456', 65, 65, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (68, '柳湘莲66', '冷二郎', '123456', 66, 66, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (69, '柳湘莲67', '冷二郎', '123456', 67, 67, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (70, '柳湘莲68', '冷二郎', '123456', 68, 68, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (71, '柳湘莲69', '冷二郎', '123456', 69, 69, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (72, '柳湘莲70', '冷二郎', '123456', 70, 70, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (73, '柳湘莲71', '冷二郎', '123456', 71, 71, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (74, '柳湘莲72', '冷二郎', '123456', 72, 72, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (75, '柳湘莲73', '冷二郎', '123456', 73, 73, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (76, '柳湘莲74', '冷二郎', '123456', 74, 74, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (77, '柳湘莲75', '冷二郎', '123456', 75, 75, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (78, '柳湘莲76', '冷二郎', '123456', 76, 76, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (79, '柳湘莲77', '冷二郎', '123456', 77, 77, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (80, '柳湘莲78', '冷二郎', '123456', 78, 78, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (81, '柳湘莲79', '冷二郎', '123456', 79, 79, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (82, '柳湘莲80', '冷二郎', '123456', 80, 80, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (83, '柳湘莲81', '冷二郎', '123456', 81, 81, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (84, '柳湘莲82', '冷二郎', '123456', 82, 82, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (85, '柳湘莲83', '冷二郎', '123456', 83, 83, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (86, '柳湘莲84', '冷二郎', '123456', 84, 84, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (87, '柳湘莲85', '冷二郎', '123456', 85, 85, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (88, '柳湘莲86', '冷二郎', '123456', 86, 86, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (89, '柳湘莲87', '冷二郎', '123456', 87, 87, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (90, '柳湘莲88', '冷二郎', '123456', 88, 88, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (91, '柳湘莲89', '冷二郎', '123456', 89, 89, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (92, '柳湘莲90', '冷二郎', '123456', 90, 90, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (93, '柳湘莲91', '冷二郎', '123456', 91, 91, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (94, '柳湘莲92', '冷二郎', '123456', 92, 92, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (95, '柳湘莲93', '冷二郎', '123456', 93, 93, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (96, '柳湘莲94', '冷二郎', '123456', 94, 94, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (97, '柳湘莲95', '冷二郎', '123456', 95, 95, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (98, '柳湘莲96', '冷二郎', '123456', 96, 96, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (99, '柳湘莲97', '冷二郎', '123456', 97, 97, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (100, '柳湘莲98', '冷二郎', '123456', 98, 98, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (101, '柳湘莲99', '冷二郎', '123456', 99, 99, '2019-05-16 06:28:37', '2019-07-16 06:39:19', NULL, 0, 0, 'lengerlang@gmail.com', '18888888888');
INSERT INTO `sys_user` VALUES (102, '贾宝玉', '冷二郎', '123456', 0, 0, '2019-05-22 02:42:23', '2019-07-16 06:39:19', NULL, 0, 0, '123@qq.com', '18888888888');
INSERT INTO `sys_user` VALUES (103, '张三', '冷二郎', '123', 0, 0, '2019-05-22 02:42:23', '2019-07-16 06:39:19', NULL, 0, 0, '123@qq.com', '18888888888');
INSERT INTO `sys_user` VALUES (105, '张三', '冷二郎', NULL, NULL, NULL, '2019-05-22 02:53:29', '2019-07-16 06:39:19', NULL, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for timed_task
-- ----------------------------
DROP TABLE IF EXISTS `timed_task`;
CREATE TABLE `timed_task`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行时间表达式',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除(0:未删除 1:已删除)',
  `update_version` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of timed_task
-- ----------------------------
INSERT INTO `timed_task` VALUES (1, '任务1', '0/5 * * * * ?', NULL, NULL, 0, 1);
INSERT INTO `timed_task` VALUES (2, '任务2', '0/1 * * * * ?', NULL, NULL, 0, 1);
INSERT INTO `timed_task` VALUES (3, '任务3', '0/10 * * * * ?', NULL, NULL, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
