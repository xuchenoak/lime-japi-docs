package io.gitee.xuchenoak.limejapidocs.runner.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * controller数据
 *
 * @author: xuchenoak
 * @create: 2022-06-05 22:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("api_docs_parse_log")
public class ApiDocsParseLog {

    /** 自增Id */
    @TableId(value = "id", type= IdType.AUTO)
    private Long id;

    /** 文档配置Id */
    private Long docsConfigId;

    /** 生成时间戳（毫秒） */
    private Long parseTimestamp;

    /** 日志消息 */
    private String logMsg;

    /** 创建时间戳（毫秒） */
    private Long createTimestamp;

}
