package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.CodeSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文档配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigVo {

    /**
     * Id
     */
    private String id;

    /**
     * 文档名称
     */
    private String docsName;

    /**
     * 文档版本号
     */
    private String docsVersion;

    /**
     * 系统启动时是否解析 0-否 1-是
     */
    private Integer sysStartParse;

    /**
     * 执行解析秘钥
     */
    private String apiRunKey;

    /**
     * java文件所在目录绝对路径（必须写到java目录，多模块时填写多个）
     */
    private List<String> javaFilePaths;

    /**
     * 仅扫描解析该包集合下的controller类（必须位于javaFilePaths下，若不配置默认扫描javaFilePaths下所有）
     */
    private List<String> filterPackages;

    /**
     * 仅扫描的controller类名集（非类全名）
     */
    private List<String> filterClassNames;

    /**
     * 需要排除的controller类名集（非类全名）
     */
    private List<String> ignoreClassNames;

    /**
     * 参数验证函数
     */
    private String paramValidFunc;

    /**
     * 参数默认值函数
     */
    private String paramDefaultValueFunc;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 文档标识码
     */
    private String docsKey;

    /**
     * 代码来源 本地/git仓库
     */
    private String codeSource;

    /**
     * git仓库地址
     */
    private String gitUrl;

     /**
     * git仓库分支
     */
    private String gitBranch;

    /**
     * git仓库用户名
     */
    private String gitUsername;

    /**
     * git仓库密码
     */
    private String gitPassword;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String getCodeSource() {
        return Optional.ofNullable(codeSource).orElse(CodeSourceEnum.LOCAL.value());
    }
}
