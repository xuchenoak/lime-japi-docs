package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 接口文档解析消息输出对象
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsParseMsgVo {

    /** 正在解析 */
    private boolean running;

    /** 生成时间戳（毫秒） */
    private Long parseTimestamp;

    /** 消息集 */
    private List<String> msgList;

}
