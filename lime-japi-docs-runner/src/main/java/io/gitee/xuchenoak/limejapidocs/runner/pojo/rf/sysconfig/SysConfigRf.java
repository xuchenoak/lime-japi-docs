package io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfigRf implements Serializable {

    /**
     * 文档中心名称
     */
    @NotBlank(message = "文档中心名称不能为空")
    private String sysName;

    /**
     * 系统LOGO
     */
    private String logoUrl;

    /**
     * 系统标语
     */
    @NotBlank(message = "系统标语不能为空")
    private String sysSlogan;

    /**
     * 文档中心邀请码
     */
    @NotBlank(message = "文档中心邀请码不能为空")
    private String docsViewKey;

    /**
     * 管理员账号
     */
    @NotBlank(message = "管理员账号不能为空")
    private String account;

    /**
     * 管理员密码
     */
    @NotBlank(message = "管理员密码不能为空")
    private String password;

}
