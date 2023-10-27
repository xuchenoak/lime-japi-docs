package io.gitee.xuchenoak.limejapidocs.runner.controller;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigIdRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigKeyCheckResultVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 文档配置业务接口
 * @author xuchenoak
 **/
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/config")
public class DocsConfigController {

    @Resource
    private DocsConfigService docsConfigService;

    @Resource
    private DocsParserConfig docsParserConfig;

    /**
     * 获取文档列表
     * @param docsName 文档名称（模糊搜索）
     * @return
     */
    @GetMapping("/list")
    public AjaxResult<List<DocsConfigListVo>> list(String docsName) {
        return AjaxResult.success(docsConfigService.list(docsName));
    }

    /**
     * 获取单个文档配置（简单版）
     * @param id 文档Id
     * @return
     */
    @GetMapping("/get_docs_config_simple")
    public AjaxResult<DocsConfigListVo> getDocsConfigSimple(@NotNull(message = "Id不能为空") Long id) {
        return AjaxResult.success(docsConfigService.getDocsConfigSimple(id));
    }

    /**
     * 获取单个文档配置
     * @param id 文档Id
     * @return
     */
    @GetMapping("/get_docs_config")
    public AjaxResult<DocsConfigVo> getDocsConfig(@NotNull(message = "Id不能为空") Long id) {
        return AjaxResult.success(docsConfigService.getDocsConfig(id));
    }

    /**
     * 验证文档管理秘钥是否正确
     * @param docsConfigKey 文档管理秘钥
     * @return
     */
    @GetMapping("/check_config_key")
    public AjaxResult<DocsConfigKeyCheckResultVo> checkConfigKey(@NotBlank(message = "文档管理秘钥不能为空") String docsConfigKey) {
        return AjaxResult.success(new DocsConfigKeyCheckResultVo(docsConfigKey, docsParserConfig.checkDocsConfigKey(docsConfigKey)));
    }

    /**
     * 新增文档
     * @param rf
     * @return
     */
    @PostMapping("/add")
    public AjaxResult<DocsConfigVo> add(@Validated @RequestBody DocsConfigAddRf rf) {
        return AjaxResult.success(docsConfigService.add(rf));
    }

    /**
     * 编辑文档
     * @param rf
     * @return
     */
    @PostMapping("/edit")
    public AjaxResult<DocsConfigVo> edit(@Validated @RequestBody DocsConfigEditRf rf) {
        return AjaxResult.success(docsConfigService.edit(rf));
    }

    /**
     * 删除文档
     * @param rf
     */
    @PostMapping("/del")
    public AjaxResult del(@Validated @RequestBody DocsConfigIdRf rf) {
        docsConfigService.del(rf.getId());
        return AjaxResult.success();
    }

}
