package io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "Id不能为空")
    private String id;

}
