package io.gitee.xuchenoak.limejapidocs.runner.controller;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigIdRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档配置业务接口
 * @author xuchenoak
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/docs_config")
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
    public AjaxResult<List<DocsConfigListVo>> list(String viewKey,
                                                   @RequestParam("views") List<String> views,
                                                   String docsName,
                                                   HttpServletRequest request) {
        List<Long> ids = null;
        if (!docsParserConfig.checkDocsConfigKey(request) && !docsParserConfig.checkDocsViewKey(viewKey)) {
            // 查看秘钥验证不通过则降级为查询指定文档（若也未传指定文档Id则返回空）
            if (ListUtils.isBlank(views)) {
                return AjaxResult.success(new ArrayList<>());
            }
            ids = new ArrayList<>();
            for (String view : views) {
                ids.add(IdUtils.decryptIdOrExc(view));
            }
        }
        return AjaxResult.success(docsConfigService.list(docsName, ids));
    }

    /**
     * 获取单个文档配置（简单版）
     * @param id 文档Id
     * @return
     */
    @GetMapping("/get_docs_config_simple")
    public AjaxResult<DocsConfigListVo> getDocsConfigSimple(@NotNull(message = "Id不能为空") String id) {
        return AjaxResult.success(docsConfigService.getDocsConfigSimple(IdUtils.decryptIdOrExc(id)));
    }

    /**
     * 获取单个文档配置
     * @param id 文档Id
     * @return
     */
    @GetMapping("/get_docs_config")
    public AjaxResult<DocsConfigVo> getDocsConfig(@NotNull(message = "Id不能为空") String id) {
        return AjaxResult.success(docsConfigService.getDocsConfig(IdUtils.decryptIdOrExc(id)));
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
        docsConfigService.del(IdUtils.decryptIdOrExc(rf.getId()));
        return AjaxResult.success();
    }

}
