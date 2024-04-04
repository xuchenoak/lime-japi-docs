package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig.SysConfigRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig.SysConfigVo;

/**
 * 系统配置业务接口
 *
 * @author xuchenoak
 **/
public interface SysConfigService {

    /**
     * 获取公共配置
     * @param account 账号
     * @param password 密码
     * @return
     */
    CommonConfigVo getCommonConfig(String account, String password);

    /**
     * 验证是否登录
     * @param account 账号
     * @param password 密码
     * @return
     */
    boolean isLogin(String account, String password);

    /**
     * 保存系统配置
     * @param rf
     */
    void saveSysConfig(SysConfigRf rf);

    /**
     * 获取系统配置
     * @return
     */
    SysConfigVo getSysConfig();

}
