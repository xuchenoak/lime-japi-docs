package io.gitee.xuchenoak.limejapidocs.runner.controller;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.SearchFromEnum;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.*;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档业务接口
 *
 * @author xuchenoak
 **/
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/docs")
public class DocsController {

    @Autowired
    private DocsService docsService;

    @Resource
    private ApiDocsConfigService apiDocsConfigService;

    /**
     * 获取生成时间集
     *
     * @param docsConfigId 文档配置Id
     * @return
     */
    @GetMapping("/list_create_time")
    public AjaxResult<List<String>> listCreateTime(@NotNull(message = "文档配置Id不能为空") String docsConfigId) {
        return AjaxResult.success(docsService.getCreateTimes(IdUtils.decryptIdOrExc(docsConfigId)));
    }

    /**
     * 获取接口文档目录
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @param likeStr      搜索关键字
     * @return
     */
    @GetMapping("/list_catalog")
    public AjaxResult<List<DocsCatalogVo>> listCatalog(@NotNull(message = "文档配置Id不能为空") String docsConfigId,
                                                       String createTime,
                                                       String likeStr) {
        if (StringUtil.isBlank(createTime)) {
            return AjaxResult.success(new ArrayList<>());
        }
        return AjaxResult.success(docsService.getDocsCatalog(IdUtils.decryptIdOrExc(docsConfigId), createTime, likeStr));
    }

    /**
     * 获取文档搜索列表
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @param searchFrom   搜索来源 1-所有 2-目录 3-接口
     * @param likeStr      搜索关键字
     * @return
     */
    @GetMapping("/list_docs_search")
    public AjaxResult<List<DocsCatalogSearchVo>> listDocsSearch(@NotNull(message = "文档配置Id不能为空") String docsConfigId,
                                                                String createTime,
                                                                Integer searchFrom,
                                                                String likeStr) {
        SearchFromEnum e = SearchFromEnum.getEnumByValue(searchFrom);
        if (StringUtil.isBlank(createTime) || e == null || StringUtil.isBlank(likeStr)) {
            return AjaxResult.success(new ArrayList<>());
        }
        return AjaxResult.success(docsService.listDocsSearch(IdUtils.decryptIdOrExc(docsConfigId), createTime, e, likeStr));
    }


    /**
     * 获取接口文档列表
     *
     * @param docsConfigId    文档配置Id
     * @param createTime      生成时间
     * @param controllerId    controller标识
     * @param hasComment      是否有注释
     * @param hasType         是否有类型
     * @param hasValid        是否有验证
     * @param addDefaultValue 是否有默认值
     * @param likeStr         搜索关键字
     * @return
     */
    @GetMapping("/list_interface")
    public AjaxResult<List<DocsInterfaceVo>> listInterface(String docsConfigId,
                                                           String createTime,
                                                           String controllerId,
                                                           Boolean hasComment,
                                                           Boolean hasType,
                                                           Boolean hasValid,
                                                           Boolean addDefaultValue,
                                                           String likeStr) {
        if (StringUtil.isBlank(docsConfigId) || StringUtil.isBlank(createTime) || StringUtil.isBlank(controllerId)) {
            return AjaxResult.success(new ArrayList<>());
        }
        hasComment = hasComment == null ? true : hasComment;
        hasType = hasType == null ? true : hasType;
        hasValid = hasValid == null ? true : hasValid;
        addDefaultValue = addDefaultValue == null ? true : addDefaultValue;
        return AjaxResult.success(docsService.getDocsInterface(IdUtils.decryptIdOrExc(docsConfigId), createTime, controllerId, hasComment, hasType, hasValid, addDefaultValue, likeStr));
    }

    /**
     * 执行文档解析
     *
     * @param docsConfigId 文档配置Id
     * @param password     解析秘钥
     * @return
     */
    @GetMapping("/run_docs_parse")
    public AjaxResult<DocsParseVo> runDocsParse(@NotNull(message = "文档配置Id不能为空") String docsConfigId, String password) {
        return AjaxResult.success(docsService.runDocsParse(IdUtils.decryptIdOrExc(docsConfigId), password));
    }

    /**
     * 执行文档解析
     *
     * @param docsKey  文档标识码
     * @param password 解析秘钥
     * @return
     */
    @RequestMapping(value = "/run_docs_parsing", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxResult<String> runDocsParsing(@NotBlank(message = "文档标识码不能为空") String docsKey, String password) {
        List<ApiDocsConfig> list = apiDocsConfigService.list(q -> q.eq(ApiDocsConfig::getDocsKey, docsKey));
        if (ListUtils.isBlank(list)) {
            return AjaxResult.error(StrUtil.format("根据文档标识码[{}]未查询到文档", docsKey));
        }
        List<String> runningNames = new ArrayList<>();
        List<String> failMsgList = new ArrayList<>();
        for (ApiDocsConfig apiDocsConfig : list) {
            DocsParseVo parseVo = docsService.runDocsParse(apiDocsConfig.getId(), password);
            if (parseVo.isRunning()) {
                runningNames.add(apiDocsConfig.getDocsName());
            } else if (StrUtil.isNotBlank(parseVo.getMsg())) {
                failMsgList.add(StrUtil.format("{}：{}", apiDocsConfig.getDocsName(), parseVo.getMsg()));
            }
        }
        String successMsg = runningNames.isEmpty() ? "" : StrUtil.format("已触发生成文档：{}", String.join("；", runningNames));
        if (ListUtils.isNotBlank(failMsgList)) {
            String prefix = runningNames.isEmpty() ? "未成功触发生成文档" : successMsg;
            return AjaxResult.error(StrUtil.format("{}，失败：{}", prefix, String.join("；", failMsgList)));
        }
        return AjaxResult.success(successMsg);
    }

    /**
     * 获取解析消息
     *
     * @param docsConfigId   文档配置Id
     * @param parseTimestamp 解析时间戳
     * @return
     */
    @GetMapping("/get_parse_msg")
    public AjaxResult<DocsParseMsgVo> getParseMsg(@NotNull(message = "文档配置Id不能为空") String docsConfigId, Long parseTimestamp) {
        return AjaxResult.success(docsService.getParseMsg(IdUtils.decryptIdOrExc(docsConfigId), parseTimestamp));
    }


}
