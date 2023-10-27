CREATE TABLE IF NOT EXISTS `api_docs_config`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `docs_name` varchar(255) NULL DEFAULT NULL COMMENT '文档名称',
    `docs_version` varchar(255) NULL DEFAULT NULL COMMENT '文档版本号',
    `sys_start_parse` int(11) NULL DEFAULT NULL COMMENT '系统启动时是否解析 0-否 1-是',
    `api_run_key` varchar(255) NULL DEFAULT NULL COMMENT '执行解析秘钥',
    `java_file_paths` text NULL COMMENT 'java文件所在目录绝对路径（必须写到java目录，多模块时填写多个）',
    `filter_packages` text NULL COMMENT '仅扫描解析该包集合下的controller类（必须位于javaFilePaths下，若不配置默认扫描javaFilePaths下所有）',
    `filter_class_names` varchar(2048) NULL DEFAULT NULL COMMENT '仅扫描的controller类名集（非类全名）',
    `ignore_class_names` varchar(2048) NULL DEFAULT NULL COMMENT '需要排除的controller类名集（非类全名）',
    `param_valid_func` text NULL COMMENT '参数验证函数',
    `param_default_value_func` text NULL COMMENT '参数默认值函数',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `api_docs_controller_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `docs_config_id` bigint(20) NOT NULL COMMENT '文档配置Id',
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
    `docs_config_id` bigint(20) NOT NULL COMMENT '文档配置Id',
    `parse_timestamp` bigint(20) NULL DEFAULT NULL COMMENT '生成时间',
    `log_msg` text NULL DEFAULT NULL COMMENT '日志消息',
    `create_timestamp` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
);