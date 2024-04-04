package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口文档解析输出对象
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsParseVo {

    /** 正在解析 */
    private boolean running;

    /** 生成时间戳（毫秒） */
    private Long parseTimestamp;

    /** 消息 */
    private String msg;

}
