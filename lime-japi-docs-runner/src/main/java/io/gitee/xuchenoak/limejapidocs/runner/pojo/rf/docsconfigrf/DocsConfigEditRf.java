package io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf;

import io.gitee.xuchenoak.limejapidocs.runner.common.enums.TrueOrFalseEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.CollectionValid;
import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 文档配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigEditRf {

    /** Id */
    @NotBlank(message = "Id不能为空")
    private String id;

    /** 文档名称 */
    @NotBlank(message = "文档名称不能为空")
    private String docsName;

    /** 文档版本号 */
    @NotBlank(message = "版本号不能为空")
    private String docsVersion;

    /** 系统启动时是否解析 0-否 1-是 */
    @EnumValid(enumClass = TrueOrFalseEnum.class)
    private Integer sysStartParse;

    /** 执行解析秘钥 */
    private String apiRunKey;

    /** java文件所在目录绝对路径（必须写到java目录，多模块时填写多个） */
    @CollectionValid(message = "java文件所在目录绝对路径不能为空")
    private List<String> javaFilePaths;

    /** 仅扫描解析该包集合下的controller类（必须位于javaFilePaths下，若不配置默认扫描javaFilePaths下所有） */
    private List<String> filterPackages;

    /** 仅扫描的controller类名集（非类全名） */
    private List<String> filterClassNames;

    /** 需要排除的controller类名集（非类全名） */
    private List<String> ignoreClassNames;

    /** 参数验证函数 */
    private String paramValidFunc;

    /** 参数默认值函数 */
    private String paramDefaultValueFunc;

    /** 排序号 */
    private Integer sort;

}
