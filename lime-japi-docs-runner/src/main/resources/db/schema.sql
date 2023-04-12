
CREATE TABLE IF NOT EXISTS `api_docs_controller_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `controller_id` varchar(255) NOT NULL COMMENT 'controller唯一标识',
  `controller_full_name` varchar(255) NULL DEFAULT NULL COMMENT 'controller类全名',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT 'controller名称注释',
  `base_uri_list` varchar(255) NULL DEFAULT NULL COMMENT '请求前缀',
  `interface_data_list` longtext NULL COMMENT '接口方法集',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `api_docs_parse_log`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
    `parse_timestamp` bigint(20) NULL DEFAULT NULL COMMENT '生成时间',
    `log_msg` text NULL DEFAULT NULL COMMENT '日志消息',
    `create_timestamp` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
);