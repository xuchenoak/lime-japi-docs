package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig.SysConfigRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig.SysConfigVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统配置业务接口
 *
 * @author xuchenoak
 **/
public interface SysConfigService {

    /**
     * 获取公共配置
     * @param request
     * @return
     */
    CommonConfigVo getCommonConfig(HttpServletRequest request);

    /**
     * 验证是否登录
     * @param request
     * @return
     */
    boolean isLogin(HttpServletRequest request);

    /**
     * 验证是否登录或文档中心邀请码是否正确
     * @param request
     * @param viewKey 邀请码
     * @return
     */
    boolean isLoginOrViewKey(HttpServletRequest request, String viewKey);

    /**
     * 验证文档中心邀请码是否正确
     * @param viewKey 邀请码
     * @return
     */
    boolean checkViewKey(String viewKey);

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
