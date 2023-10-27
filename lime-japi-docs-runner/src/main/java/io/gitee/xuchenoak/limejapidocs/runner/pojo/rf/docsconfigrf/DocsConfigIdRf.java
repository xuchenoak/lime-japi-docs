package io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 文档配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigIdRf {

    /** Id */
    @NotNull(message = "Id不能为空")
    private Long id;

}
