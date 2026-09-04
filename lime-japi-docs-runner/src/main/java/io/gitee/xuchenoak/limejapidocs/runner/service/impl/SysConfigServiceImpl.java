package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.bean.SysConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig.SysConfigRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig.SysConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ParamsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.SysConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 系统配置业务实现
 *
 * @author xuchenoak
 **/
@Slf4j
@Service
public class SysConfigServiceImpl implements SysConfigService {

    private static final String SYS_CONFIG_KEY = "sys_config";

    @Resource
    private ParamsConfigService paramsConfigService;

    /**
     * 获取公共配置
     *
     * @param request
     * @return
     */
    @Override
    public CommonConfigVo getCommonConfig(HttpServletRequest request) {
        CommonConfigVo vo = new CommonConfigVo();
        SysConfigVo sysConfig = getSysConfig();
        if (sysConfig != null) {
            vo.setSysName(sysConfig.getSysName());
            vo.setSysSlogan(sysConfig.getSysSlogan());
            vo.setLogoUrl(sysConfig.getLogoUrl());
            vo.setHasInit(true);
            vo.setHasLogin(isLogin(request, sysConfig));
        } else {
            vo.setHasInit(false);
            vo.setHasLogin(false);
        }
        return vo;
    }

    /**
     * 验证是否登录
     *
     * @param request
     * @return
     */
    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, getSysConfig());
    }

    /**
     * 验证是否登录或文档中心邀请码是否正确
     *
     * @param request
     * @param viewKey 邀请码
     * @return
     */
    @Override
    public boolean isLoginOrViewKey(HttpServletRequest request, String viewKey) {
        SysConfigVo sysConfig = getSysConfig();
        if (sysConfig == null) {
            return false;
        }
        if (isLogin(request, sysConfig)) {
            return true;
        }
        if (StrUtil.isBlank(viewKey)) {
            return false;
        }
        return viewKey.equals(sysConfig.getDocsViewKey());
    }

    /**
     * 验证文档中心邀请码是否正确
     *
     * @param viewKey 邀请码
     * @return
     */
    @Override
    public boolean checkViewKey(String viewKey) {
        if (StrUtil.isBlank(viewKey)) {
            return false;
        }
        SysConfigVo sysConfig = getSysConfig();
        if (sysConfig == null) {
            return false;
        }
        return viewKey.equals(sysConfig.getDocsViewKey());
    }

    /**
     * 验证是否登录
     *
     * @param request
     * @param sysConfig
     * @return
     */
    public boolean isLogin(HttpServletRequest request, SysConfigVo sysConfig) {
        if (sysConfig == null) {
            return false;
        }
        String account = Optional.ofNullable(request.getHeader("Uacc")).orElse("");
        String password = Optional.ofNullable(request.getHeader("Upas")).orElse("");
        return sysConfig.getAccount().equals(account) && sysConfig.getPassword().equals(password);
    }

    /**
     * 保存系统配置
     *
     * @param rf
     */
    @Override
    public void saveSysConfig(SysConfigRf rf) {
        SysConfig sysConfig = new SysConfig(
                rf.getSysName(),
                rf.getLogoUrl(),
                rf.getSysSlogan(),
                rf.getDocsViewKey(),
                rf.getAccount(),
                rf.getPassword()
        );
        paramsConfigService.addOrEdit(SYS_CONFIG_KEY, JSONUtil.toJsonStr(sysConfig));
    }

    /**
     * 获取系统配置
     *
     * @return
     */
    @Override
    public SysConfigVo getSysConfig() {
        return BeanUtil.getBeanOrDoing(paramsConfigService.getParamCache(SYS_CONFIG_KEY, SysConfig.class), bean -> new SysConfigVo(
                bean.getSysName(),
                bean.getLogoUrl(),
                bean.getSysSlogan(),
                bean.getDocsViewKey(),
                bean.getAccount(),
                bean.getPassword()
        ));
    }

}
