package io.gitee.xuchenoak.limejapidocs.runner.controller;

import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsvo.DocsCatalogVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsvo.DocsInterfaceVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsvo.DocsParseMsgVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsvo.DocsParseVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档业务接口
 * @author xuchenoak
 **/
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/docs")
public class DocsController {

    @Autowired
    private DocsService docsService;

    /**
     * 获取生成时间集
     * @param docsConfigId 文档配置Id
     * @return
     */
    @GetMapping("/list_create_time")
    public AjaxResult<List<String>> listCreateTime(@NotNull(message = "文档配置Id不能为空") String docsConfigId) {
        return AjaxResult.success(docsService.getCreateTimes(IdUtils.decryptIdOrExc(docsConfigId)));
    }

    /**
     * 获取接口文档目录
     * @param createTime 生成时间
     * @param likeStr 搜索关键字
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
     * 获取接口文档列表
     * @param createTime 生成时间
     * @param controllerId controller标识
     * @param hasComment 是否有注释
     * @param hasType 是否有类型
     * @param hasValid 是否有验证
     * @param addDefaultValue 是否有默认值
     * @param likeStr 搜索关键字
     * @return
     */
    @GetMapping("/list_interface")
    public AjaxResult<List<DocsInterfaceVo>> listInterface(String createTime,
                                                           String controllerId,
                                                           Boolean hasComment,
                                                           Boolean hasType,
                                                           Boolean hasValid,
                                                           Boolean addDefaultValue,
                                                           String likeStr) {
        if (StringUtil.isBlank(createTime) || StringUtil.isBlank(controllerId)) {
            return AjaxResult.success(new ArrayList<>());
        }
        hasComment = hasComment == null ? true : hasComment;
        hasType = hasType == null ? true : hasType;
        hasValid = hasValid == null ? true : hasValid;
        addDefaultValue = addDefaultValue == null ? true : addDefaultValue;
        return AjaxResult.success(docsService.getDocsInterface(createTime, controllerId, hasComment, hasType, hasValid, addDefaultValue, likeStr));
    }

    /**
     * 执行文档解析
     * @param docsConfigId 文档配置Id
     * @param password 解析秘钥
     * @return
     */
    @GetMapping("/run_docs_parse")
    public AjaxResult<DocsParseVo> runDocsParse(@NotNull(message = "文档配置Id不能为空") String docsConfigId, String password) {
        return AjaxResult.success(docsService.runDocsParse(IdUtils.decryptIdOrExc(docsConfigId), password));
    }

    /**
     * 获取解析消息
     * @param docsConfigId 文档配置Id
     * @param parseTimestamp 解析时间戳
     * @return
     */
    @GetMapping("/get_parse_msg")
    public AjaxResult<DocsParseMsgVo> getParseMsg(@NotNull(message = "文档配置Id不能为空") String docsConfigId, Long parseTimestamp) {
        return AjaxResult.success(docsService.getParseMsg(IdUtils.decryptIdOrExc(docsConfigId), parseTimestamp));
    }


}
