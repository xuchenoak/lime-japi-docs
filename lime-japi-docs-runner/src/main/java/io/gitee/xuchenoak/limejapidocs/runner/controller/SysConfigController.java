package io.gitee.xuchenoak.limejapidocs.runner.controller;

import cn.hutool.core.io.IoUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigIdRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.CommonConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigKeyCheckResultVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.SysConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档配置业务接口
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
     * 验证logo文件是否存在
     * @return
     */
    @GetMapping("/common")
    public AjaxResult<CommonConfigVo> common() {
        Boolean logoExist;
        try {
            ClassPathResource resource = new ClassPathResource("logo.png");
            logoExist = resource.exists();
        } catch (Exception e) {
            logoExist = false;
        }
        return AjaxResult.success(new CommonConfigVo("", logoExist));
    }

    /**
     * 获取logo图标
     * @return
     */
    @GetMapping("/logo")
    public void logo(HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("logo.png");
        try (InputStream inputStream = resource.getInputStream()){
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("获取logo文件异常", e);
        }
    }

}
