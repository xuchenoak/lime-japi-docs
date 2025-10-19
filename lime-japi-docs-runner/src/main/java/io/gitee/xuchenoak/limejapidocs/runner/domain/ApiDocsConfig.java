package io.gitee.xuchenoak.limejapidocs.runner.domain;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.TrueOrFalseEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.StringListTypeHandler;
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
@TableName(value = "api_docs_config", autoResultMap = true)
public class ApiDocsConfig {

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> javaFilePaths;

    /**
     * 仅扫描解析该包集合下的controller类（必须位于javaFilePaths下，若不配置默认扫描javaFilePaths下所有）
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> filterPackages;

    /**
     * 仅扫描的controller类名集（非类全名）
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> filterClassNames;

    /**
     * 需要排除的controller类名集（非类全名）
     */
    @TableField(typeHandler = StringListTypeHandler.class)
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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public ApiDocsConfig(String docsName, String docsVersion, Integer sysStartParse, String apiRunKey, List<String> javaFilePaths, List<String> filterPackages, List<String> filterClassNames, List<String> ignoreClassNames, String paramValidFunc, String paramDefaultValueFunc, Integer sort, String docsKey, String codeSource, String gitUrl, String gitBranch, String gitUsername, String gitPassword, Date createTime, Date updateTime) {
        this.docsName = docsName;
        this.docsVersion = docsVersion;
        this.sysStartParse = sysStartParse;
        this.apiRunKey = apiRunKey;
        this.javaFilePaths = javaFilePaths;
        this.filterPackages = filterPackages;
        this.filterClassNames = filterClassNames;
        this.ignoreClassNames = ignoreClassNames;
        this.paramValidFunc = paramValidFunc;
        this.paramDefaultValueFunc = paramDefaultValueFunc;
        this.sort = sort;
        this.docsKey = docsKey;
        this.codeSource = codeSource;
        this.gitUrl = gitUrl;
        this.gitBranch = gitBranch;
        this.gitUsername = gitUsername;
        this.gitPassword = gitPassword;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Boolean hasSysStartParse() {
        return Optional.ofNullable(sysStartParse).orElse(TrueOrFalseEnum.FALSE.value()).equals(TrueOrFalseEnum.TRUE.value());
    }

    public boolean checkedApiRun(String apiRunKey) {
        if (StringUtil.isBlank(this.apiRunKey)) {
            return true;
        }
        if (StringUtil.isBlank(apiRunKey)) {
            return false;
        }
        return this.apiRunKey.equals(apiRunKey);
    }

    public ApiDocsConfig buildEdit(String docsName, String docsVersion, Integer sysStartParse, String apiRunKey, List<String> javaFilePaths, List<String> filterPackages, List<String> filterClassNames, List<String> ignoreClassNames, String paramValidFunc, String paramDefaultValueFunc, Integer sort, String docsKey, String codeSource, String gitUrl, String gitBranch, String gitUsername, String gitPassword) {
        this.docsName = docsName;
        this.docsVersion = docsVersion;
        this.sysStartParse = sysStartParse;
        this.apiRunKey = apiRunKey;
        this.javaFilePaths = javaFilePaths;
        this.filterPackages = filterPackages;
        this.filterClassNames = filterClassNames;
        this.ignoreClassNames = ignoreClassNames;
        this.paramValidFunc = paramValidFunc;
        this.paramDefaultValueFunc = paramDefaultValueFunc;
        this.sort = sort;
        this.docsKey = docsKey;
        this.codeSource = codeSource;
        this.gitUrl = gitUrl;
        this.gitBranch = gitBranch;
        this.gitUsername = gitUsername;
        this.gitPassword = gitPassword;
        this.updateTime = DateUtil.date();
        return this;
    }
}
