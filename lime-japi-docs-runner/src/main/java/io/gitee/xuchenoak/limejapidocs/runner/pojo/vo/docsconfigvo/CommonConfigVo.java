package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共配置输出
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonConfigVo {

    /** 标语 */
    private String slogan;

    /** 自定义logo是否存在 */
    private Boolean logoExist;

}
