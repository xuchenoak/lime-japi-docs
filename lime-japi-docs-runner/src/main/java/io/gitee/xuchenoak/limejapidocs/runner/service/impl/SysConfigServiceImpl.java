package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.bean.SysConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig.SysConfigRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig.SysConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ParamsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
     * @param account 账号
     * @param password 密码
     * @return
     */
    @Override
    public CommonConfigVo getCommonConfig(String account, String password) {
        CommonConfigVo vo = new CommonConfigVo();
        SysConfigVo sysConfig = getSysConfig();
        if (sysConfig != null) {
            vo.setSysName(sysConfig.getSysName());
            vo.setSysSlogan(sysConfig.getSysSlogan());
            vo.setLogoUrl(sysConfig.getLogoUrl());
            vo.setHasInit(true);
            vo.setHasLogin(sysConfig.getAccount().equals(account) && sysConfig.getPassword().equals(password));
        } else {
            vo.setHasInit(false);
            vo.setHasLogin(false);
        }
        return vo;
    }

    /**
     * 验证是否登录
     * @param account 账号
     * @param password 密码
     * @return
     */
    @Override
    public boolean isLogin(String account, String password) {
        SysConfigVo sysConfig = getSysConfig();
        if (sysConfig != null) {
            return sysConfig.getAccount().equals(account) && sysConfig.getPassword().equals(password);
        }
        return false;
    }

    /**
     * 保存系统配置
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
     * @return
     */
    @Override
    public SysConfigVo getSysConfig() {
        SysConfig bean = paramsConfigService.getParamCache(SYS_CONFIG_KEY, SysConfig.class);
        if (bean != null) {
            return new SysConfigVo(
                    bean.getSysName(),
                    bean.getLogoUrl(),
                    bean.getSysSlogan(),
                    bean.getDocsViewKey(),
                    bean.getAccount(),
                    bean.getPassword()
            );
        }
        return new SysConfigVo();
    }

}
