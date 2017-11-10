/*
Navicat MySQL Data Transfer

Source Server         : 本地MySQL
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : benefitgo

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-11-09 19:58:52
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for after_sale
-- ----------------------------
DROP TABLE IF EXISTS `after_sale`;
CREATE TABLE `after_sale` (
  `id`             BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`     DATETIME             NOT NULL,
  `gmt_modified`   DATETIME             NOT NULL,
  `title`          VARCHAR(255)
                   CHARACTER SET utf8   NOT NULL
  COMMENT '售后记录标题',
  `description`    VARCHAR(255)         NOT NULL
  COMMENT '售后理由描述',
  `service_status` INT(255) UNSIGNED    NOT NULL DEFAULT '1'
  COMMENT '售后服务的受理状态\r\n1.申请已提交\r\n2.处理超时\r\n3.用户取消\r\n4.处理完成',
  `orderid`        BIGINT(255) UNSIGNED NOT NULL
  COMMENT '订单id',
  `handle_person`  BIGINT(255) UNSIGNED          DEFAULT NULL
  COMMENT '处理人id',
  `remark`         VARCHAR(255)                  DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `orderid` (`orderid`),
  KEY `FK_64dvo5tfn24kafuoj358gilld` (`handle_person`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id`            BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`    DATETIME             NOT NULL,
  `gmt_modified`  DATETIME             NOT NULL,
  `title`         VARCHAR(255)
                  CHARACTER SET utf8   NOT NULL DEFAULT '新公告'
  COMMENT '公告标题',
  `publisher`     BIGINT(255) UNSIGNED NOT NULL
  COMMENT '发布人id',
  `publisherNick` VARCHAR(255)
                  CHARACTER SET utf8            DEFAULT NULL
  COMMENT '发布人昵称',
  `content`       VARCHAR(255)         NOT NULL DEFAULT '公告内容'
  COMMENT '公告内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `publisher` (`publisher`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for evaluation
-- ----------------------------
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE `evaluation` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `userid`       BIGINT(255) UNSIGNED NOT NULL,
  `goodsid`      BIGINT(255) UNSIGNED NOT NULL,
  `score`        INT(20) UNSIGNED     NOT NULL DEFAULT '0'
  COMMENT '评价等级0到5',
  `content`      VARCHAR(255)         NOT NULL DEFAULT ''
  COMMENT '评价内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userid` (`userid`) USING BTREE,
  KEY `goodsid` (`goodsid`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for express_delivery
-- ----------------------------
DROP TABLE IF EXISTS `express_delivery`;
CREATE TABLE `express_delivery` (
  `id`           BIGINT(255) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME                NOT NULL,
  `gmt_modified` DATETIME                NOT NULL,
  `name`         VARCHAR(255)            NOT NULL
  COMMENT '快递名称',
  `price`        DECIMAL(10, 2) UNSIGNED NOT NULL DEFAULT '0.00'
  COMMENT '快递费',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `userid`       BIGINT(255) UNSIGNED NOT NULL
  COMMENT '用户id',
  `goodsid`      BIGINT(255) UNSIGNED NOT NULL
  COMMENT '商品id',
  `isDeleted`    BIT(1)               NOT NULL DEFAULT b'0'
  COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userid` (`userid`) USING BTREE,
  KEY `FK_fxs1efwvl3ctass58t6uea2fh` (`goodsid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id`                BIGINT(255) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `gmt_create`        DATETIME                NOT NULL,
  `gmt_modified`      DATETIME                NOT NULL,
  `name`              VARCHAR(255)            NOT NULL DEFAULT ''
  COMMENT '商品名称',
  `originalprice`     DECIMAL(65, 2) UNSIGNED NOT NULL DEFAULT '0.00'
  COMMENT '原价',
  `preferentialprice` DECIMAL(65, 2) UNSIGNED NOT NULL DEFAULT '0.00'
  COMMENT '团购价',
  `total_inventory`   BIGINT(255) UNSIGNED             DEFAULT '0'
  COMMENT '总库存量',
  `surplus_inventory` BIGINT(255) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '剩余库存',
  `shopid`            BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '所属店铺id',
  `picture`           VARCHAR(255)                     DEFAULT NULL
  COMMENT '商品图片地址',
  `description`       TEXT                    NOT NULL
  COMMENT '商品描述HTML',
  `category`          BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '商品类别',
  `isonshelves`       BIT(1)                  NOT NULL DEFAULT b'0'
  COMMENT '1上架0下架',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for goods_picture
-- ----------------------------
DROP TABLE IF EXISTS `goods_picture`;
CREATE TABLE `goods_picture` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `save_url`     VARCHAR(255)         NOT NULL
  COMMENT '图片存储路径',
  `goodsid`      BIGINT(255) UNSIGNED NOT NULL
  COMMENT '商品id',
  `isdeleted`    BIT(1)               NOT NULL DEFAULT b'0'
  COMMENT '1是0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `goodsid` (`goodsid`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for group_purchase
-- ----------------------------
DROP TABLE IF EXISTS `group_purchase`;
CREATE TABLE `group_purchase` (
  `id`                 BIGINT(20) UNSIGNED  NOT NULL AUTO_INCREMENT,
  `gmt_create`         DATETIME             NOT NULL,
  `gmt_modified`       DATETIME             NOT NULL,
  `userid`             BIGINT(255) UNSIGNED NOT NULL
  COMMENT '商家id',
  `goodsid`            BIGINT(255) UNSIGNED NOT NULL
  COMMENT '团购商品id',
  `express_deliveryid` BIGINT(255) UNSIGNED NOT NULL
  COMMENT '快递表id',
  `min_participants`   BIGINT(255) UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '最少参与人数',
  `max_participants`   BIGINT(255) UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '最大参与人数',
  `start_time`         DATETIME             NOT NULL
  COMMENT '团购开始时间',
  `end_time`           DATETIME             NOT NULL
  COMMENT '团购截止时间',
  `iseffective`        BIT(1)               NOT NULL DEFAULT b'1'
  COMMENT '当前团购活动是否有效1有0无',
  `description`        TEXT COMMENT '描述',
  `number_part`        INT(255) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '团购参与人数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `goodsid` (`goodsid`) USING BTREE,
  KEY `userid` (`userid`) USING BTREE,
  KEY `express_deliveryid` (`express_deliveryid`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `senderid`     BIGINT(255) UNSIGNED NOT NULL
  COMMENT '发送人id',
  `senderNicke`  VARCHAR(255)
                 CHARACTER SET utf8   NOT NULL
  COMMENT '发送人昵称',
  `receiveid`    BIGINT(255) UNSIGNED NOT NULL
  COMMENT '接收人id',
  `content`      VARCHAR(255)         NOT NULL
  COMMENT '消息内容',
  `type`         INT(255) UNSIGNED    NOT NULL DEFAULT '1'
  COMMENT '消息类型1个人消息0系统消息2全部消息',
  `isread`       BIT(1)               NOT NULL DEFAULT b'0'
  COMMENT '已读1 未读0',
  `isdeleted`    BIT(1)               NOT NULL DEFAULT b'0'
  COMMENT '1已删除0未删除，默认0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `senderid` (`senderid`),
  KEY `receiveid` (`receiveid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id`           BIGINT(255) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME                NOT NULL,
  `gmt_modified` DATETIME                NOT NULL,
  `userid`       BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '用户id',
  `goodsid`      BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '商品id',
  `expressid`    BIGINT(255) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '快递方式id',
  `addressid`    BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '地址ID',
  `price`        DECIMAL(63, 2) UNSIGNED NOT NULL DEFAULT '0.00'
  COMMENT '商品单价',
  `pay_method`   VARCHAR(255)
                 CHARACTER SET utf8      NOT NULL DEFAULT 'ACCOUNT'
  COMMENT '订单支付方式',
  `status`       VARCHAR(255)
                 CHARACTER SET utf8      NOT NULL DEFAULT 'ORDER_UNPAY'
  COMMENT '订单状态',
  `remark`       VARCHAR(255)            NOT NULL
  COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userid` (`userid`),
  KEY `goodsid` (`goodsid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 53
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for order_status
-- ----------------------------
DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME(6)          NOT NULL,
  `gmt_modified` DATETIME(6)          NOT NULL,
  `flag`         VARCHAR(255)
                 CHARACTER SET utf8   NOT NULL
  COMMENT '订单状态标识符',
  `description`  VARCHAR(255)
                 CHARACTER SET utf8            DEFAULT NULL
  COMMENT '状态含义描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for participate_group
-- ----------------------------
DROP TABLE IF EXISTS `participate_group`;
CREATE TABLE `participate_group` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `userid`       BIGINT(255) UNSIGNED NOT NULL
  COMMENT '参团的用户id',
  `groupid`      BIGINT(255) UNSIGNED NOT NULL
  COMMENT '团购活动id',
  `iseffective`  BIT(1)               NOT NULL DEFAULT b'1'
  COMMENT '当前记录是否有效1有0无',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userid` (`userid`),
  KEY `groupid` (`groupid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 61
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for receiving_address
-- ----------------------------
DROP TABLE IF EXISTS `receiving_address`;
CREATE TABLE `receiving_address` (
  `id`         BIGINT(20) UNSIGNED  NOT NULL AUTO_INCREMENT,
  `userId`     BIGINT(255) UNSIGNED NOT NULL,
  `name`       VARCHAR(25)
               CHARACTER SET utf8   NOT NULL
  COMMENT '收货人姓名',
  `tel`        VARCHAR(25)
               CHARACTER SET utf8   NOT NULL
  COMMENT '收货人联系方式',
  `address`    VARCHAR(255)         NOT NULL DEFAULT ''
  COMMENT '具体收货地址',
  `gmt_create` DATETIME             NOT NULL
  COMMENT '记录创建时间',
  `gmt_modify` DATETIME                      DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userId` (`userId`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME            NOT NULL,
  `gmt_modified` DATETIME            NOT NULL,
  `name`         VARCHAR(25)         NOT NULL DEFAULT '',
  `flag`         VARCHAR(20)
                 CHARACTER SET utf8  NOT NULL DEFAULT ''
  COMMENT '类型标识符CUSTOMER 会员 ADMIN 管理员 MERCHANT商家',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for saletype
-- ----------------------------
DROP TABLE IF EXISTS `saletype`;
CREATE TABLE `saletype` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME             NOT NULL,
  `name`         VARCHAR(255)         NOT NULL,
  `description`  VARCHAR(255)                  DEFAULT NULL
  COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id`           BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userId`       BIGINT(255) UNSIGNED NOT NULL
  COMMENT '商家id',
  `gmt_create`   DATETIME             NOT NULL,
  `gmt_modified` DATETIME                      DEFAULT NULL,
  `name`         VARCHAR(255)         NOT NULL DEFAULT '新店铺',
  `addresss`     VARCHAR(255)                  DEFAULT NULL
  COMMENT '店铺所在地',
  `serviceTel`   VARCHAR(50)          NOT NULL DEFAULT ''
  COMMENT '服务电话',
  `saletype`     BIGINT(255) UNSIGNED          DEFAULT NULL
  COMMENT '销售类型',
  `isVerify`     TINYINT(1) UNSIGNED  NOT NULL DEFAULT '0'
  COMMENT '是否通过审核0否1是2等待审核',
  `license_code` VARCHAR(255)
                 CHARACTER SET utf8   NOT NULL DEFAULT ''
  COMMENT '营业执照注册号',
  `license_pic`  VARCHAR(255)
                 CHARACTER SET utf8   NOT NULL DEFAULT ''
  COMMENT '营业执照图片路径',
  `logo`         VARCHAR(255)                  DEFAULT NULL
  COMMENT '店铺LOGO地址',
  `description`  VARCHAR(255)                  DEFAULT NULL
  COMMENT '店铺描述',
  `islocked`     BIT(1)               NOT NULL DEFAULT b'0'
  COMMENT '店铺是否禁用0否1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userId` (`userId`),
  KEY `isVerify` (`isVerify`),
  KEY `FK_pni211m78iyf0rm1870mn9hdf` (`saletype`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`             BIGINT(255) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `account`        VARCHAR(25)
                   CHARACTER SET utf8      NOT NULL DEFAULT ''
  COMMENT '账号',
  `password`       VARCHAR(255)
                   CHARACTER SET utf8      NOT NULL,
  `relname`        VARCHAR(20)
                   CHARACTER SET utf8               DEFAULT NULL
  COMMENT '真实姓名',
  `nickName`       VARCHAR(50)
                   CHARACTER SET utf8      NOT NULL DEFAULT '新用户'
  COMMENT '昵称',
  `email`          VARCHAR(255)
                   CHARACTER SET utf8      NOT NULL
  COMMENT '邮箱',
  `telephone`      VARCHAR(20)
                   CHARACTER SET utf8      NOT NULL,
  `headportrait`   VARCHAR(255)
                   CHARACTER SET utf8               DEFAULT NULL
  COMMENT '用户头像路径',
  `sex`            BIT(1)                  NOT NULL DEFAULT b'1'
  COMMENT '1男0女',
  `birthday`       DATE                             DEFAULT NULL
  COMMENT '生日',
  `idCard`         VARCHAR(255)
                   CHARACTER SET utf8               DEFAULT ''
  COMMENT '身份证号',
  `role`           VARCHAR(20)
                   CHARACTER SET utf8      NOT NULL DEFAULT 'CUSTOMER'
  COMMENT '用户角色\r\nCUSTOMER 会员\r\nMERCHANT 商家\r\nADMIN 管理员',
  `islocked`       BIT(1)                  NOT NULL DEFAULT b'0'
  COMMENT '0正常1锁定',
  `integral`       BIGINT(255) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '积分',
  `balance`        DECIMAL(65, 2) UNSIGNED NOT NULL DEFAULT '0.00'
  COMMENT '余额',
  `gmt_create`     DATETIME                NOT NULL
  COMMENT '创建时间',
  `gmt_modified`   DATETIME                NOT NULL
  COMMENT '记录修改时间',
  `checkcode`      VARCHAR(255)
                   CHARACTER SET utf8      NOT NULL
  COMMENT '校验码,在用户注册时由系统自动生成',
  `isVerification` BIT(1)                  NOT NULL DEFAULT b'0'
  COMMENT '用户账号是否认证1是0否',
  `isLogin`        BIT(1)                  NOT NULL DEFAULT b'0'
  COMMENT '用户登录状态0未登录 1已登录',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING HASH,
  UNIQUE KEY `account` (`account`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for vouchers
-- ----------------------------
DROP TABLE IF EXISTS `vouchers`;
CREATE TABLE `vouchers` (
  `id`           BIGINT(255) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `gmt_create`   DATETIME                NOT NULL,
  `gmt_modified` DATETIME                NOT NULL,
  `userid`       BIGINT(255) UNSIGNED    NOT NULL
  COMMENT '持有人Id',
  `seriacod`     VARCHAR(255)
                 CHARACTER SET utf8      NOT NULL
  COMMENT '团购券序列码',
  `indate`       DATETIME(6)             NOT NULL
  COMMENT '有效期',
  `price`        DECIMAL(10, 2) UNSIGNED NOT NULL
  COMMENT '优惠金额',
  `limituse`     DECIMAL(10, 2) UNSIGNED NOT NULL
  COMMENT '限制使用金额,订单金额不足该值时无法使用',
  `status`       VARCHAR(20)
                 CHARACTER SET utf8      NOT NULL DEFAULT '1'
  COMMENT '1有效0逾期2无效',
  `isused`       BIT(1)                  NOT NULL DEFAULT b'0'
  COMMENT '是否被使用1 是0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userid` (`userid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8mb4;
