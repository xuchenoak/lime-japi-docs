package io.gitee.xuchenoak.limejapidocs.runner.controller;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.sysconfig.SysConfigRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig.SysConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 系统配置业务接口
 *
 * @author xuchenoak
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/sys_config")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取公共配置
     *
     * @return
     */
    @GetMapping("/common")
    public AjaxResult<CommonConfigVo> common(HttpServletRequest request) {
        return AjaxResult.success(sysConfigService.getCommonConfig(request));
    }

    /**
     * 系统初始化
     *
     * @param rf
     */
    @PostMapping("/sys_init")
    public AjaxResult sysInit(@Validated @RequestBody SysConfigRf rf) {
        SysConfigVo sysConfig = sysConfigService.getSysConfig();
        if (sysConfig != null) {
            CusExc.e("系统已初始化");
        }
        sysConfigService.saveSysConfig(rf);
        return AjaxResult.success();
    }

    /**
     * 保存系统配置
     *
     * @param rf
     */
    @PostMapping("/save_sys_config")
    public AjaxResult saveSysConfig(@Validated @RequestBody SysConfigRf rf) {
        sysConfigService.saveSysConfig(rf);
        return AjaxResult.success();
    }

    /**
     * 获取系统配置
     *
     * @return
     */
    @GetMapping("/get_sys_config")
    public AjaxResult<SysConfigVo> getSysConfig() {
        return AjaxResult.success(Optional.ofNullable(sysConfigService.getSysConfig()).orElse(new SysConfigVo()));
    }

}
