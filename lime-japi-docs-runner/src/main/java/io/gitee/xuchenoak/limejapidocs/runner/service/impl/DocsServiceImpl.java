package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldDataNode;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.bean.DocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.handler.DocsParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.*;
import io.gitee.xuchenoak.limejapidocs.runner.runner.DocsParseService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsParseLogService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsService;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口文档业务实现
 *
 * @author: xuchenoak
 * @create: 2022-06-06 8:20
 **/
@Service
public class DocsServiceImpl implements DocsService {

    private static final Logger logger = LoggerFactory.getLogger(DocsServiceImpl.class);

    @Autowired
    private DocsParserConfigHandler docsParserConfigHandler;

    @Autowired
    private DocsParserConfig docsParserConfig;

    @Autowired
    private DocsParseService docsParseService;

    @Autowired
    private ApiDocsParseLogService apiDocsParseLogService;

    /**
     * 执行文档解析
     * @param password 解析秘钥
     */
    @Override
    public DocsParseVo runDocsParse(String password) {
        // 验证解析秘钥
        if (!docsParserConfig.checkedApiRun(password)) {
            return new DocsParseVo(false, null, "生成秘钥错误！请输入正确解析秘钥再重试");
        }
        // 验证是否还在进行解析
        if (MsgUtil.isParseRun()) {
            return new DocsParseVo(true, MsgUtil.getParseTimestamp(), "正在生成");
        }
        // 执行解析
        MsgUtil.statusParseRun();
        docsParseService.runParse();
        return new DocsParseVo(true, MsgUtil.getParseTimestamp(), "开始生成");
    }

    /**
     * 获取解析消息
     * @param parseTimestamp 解析时间戳
     * @return
     */
    @Override
    public DocsParseMsgVo getParseMsg(Long parseTimestamp) {
        // 验证是否还在进行解析
        if (parseTimestamp == null) {
            return new DocsParseMsgVo(MsgUtil.isParseRun(), MsgUtil.getParseTimestamp(), new ArrayList<>());
        }
        return new DocsParseMsgVo(MsgUtil.isParseRun(), parseTimestamp, apiDocsParseLogService.listMsg(parseTimestamp));
    }

    /**
     * 获取生成时间集
     * @return
     */
    @Override
    public List<String> getCreateTimes() {
        List<String> set = docsParserConfigHandler.getCreateTimeList();
        if (set == null) {
            return new ArrayList<>();
        }
        return set;
    }

    /**
     * 获取接口文档目录
     * @param createTime 生成时间
     * @param likeStr 搜索关键字
     * @return
     */
    @Override
    public List<DocsCatalogVo> getDocsCatalog(String createTime, String likeStr) {
        List<DocsCatalogVo> docsCatalogVoList = new ArrayList<>();
        List<ControllerData> controllerDataList = docsParserConfigHandler.getControllerDataList(createTime);
        if (ListUtil.isBlank(controllerDataList)) {
            return docsCatalogVoList;
        }
        for (ControllerData controllerData : controllerDataList) {
            List<InterfaceData> interfaceDataList = controllerData.getInterfaceDataList();
            if (ListUtil.isBlank(interfaceDataList)) {
                continue;
            }
            if (StringUtil.isBlank(controllerData.getComment())) {
                continue;
            }
            if (StringUtil.isNotBlank(likeStr) && !controllerData.getComment().contains(likeStr)) {
                continue;
            }
            docsCatalogVoList.add(new DocsCatalogVo(
                    controllerData.getControllerId(),
                    controllerData.getComment(),
                    controllerData.getSort()));
        }
        docsCatalogVoList = docsCatalogVoList.stream().sorted(Comparator.comparing(DocsCatalogVo::getSort)).collect(Collectors.toList());
        if (docsCatalogVoList == null) {
            logger.info("获取接口文档目录排序失败 createTime：{}", createTime);
            docsCatalogVoList = new ArrayList<>();
        }
        return docsCatalogVoList;
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
    @Override
    public List<DocsInterfaceVo> getDocsInterface(String createTime, String controllerId, boolean hasComment, boolean hasType, boolean hasValid, boolean addDefaultValue, String likeStr) {
        List<DocsInterfaceVo> docsInterfaceVoList = new ArrayList<>();
        ControllerData controllerData = docsParserConfigHandler.getControllerData(createTime, controllerId);
        if (controllerData == null) {
            return docsInterfaceVoList;
        }
        List<InterfaceData> interfaceDataList = controllerData.getInterfaceDataList();
        if (ListUtil.isBlank(interfaceDataList)) {
            return docsInterfaceVoList;
        }
        for (InterfaceData interfaceData : interfaceDataList) {
            if (StringUtil.isBlank(interfaceData.getComment())) {
                continue;
            }
            if (StringUtil.isNotBlank(likeStr) && !interfaceData.getComment().contains(likeStr)) {
                continue;
            }
            DocsInterfaceVo docsInterfaceVo = new DocsInterfaceVo(
                    interfaceData.getInterfaceId(),
                    controllerId,
                    interfaceData.getComment(),
                    interfaceData.getMethodName(),
                    interfaceData.getUriList(),
                    interfaceData.getRequestTypeList(),
                    interfaceData.getRequestContentType(),
                    interfaceData.getFormData(),
                    interfaceData.getSort()
            );
            if (!constructJson(docsInterfaceVo, interfaceData, hasComment, hasType, hasValid, addDefaultValue)) {
                continue;
            }
            docsInterfaceVoList.add(docsInterfaceVo);
        }
        docsInterfaceVoList = docsInterfaceVoList.stream().sorted(Comparator.comparing(DocsInterfaceVo::getSort)).collect(Collectors.toList());
        if (docsInterfaceVoList == null) {
            logger.info("获取接口文档列表排序失败 controllerId：{}", controllerId);
            docsInterfaceVoList = new ArrayList<>();
        }
        return docsInterfaceVoList;
    }

    /**
     * 获取接口文档配置
     * @return
     */
    @Override
    public DocsConfigVo getDocsConfig() {
        DocsConfig docsConfig = docsParserConfigHandler.getDocsConfig();
        return new DocsConfigVo(docsConfig.getDocName(), docsConfig.getDocVersion());
    }

    /**
     * 构造json字符串
     * @param docsInterfaceVo 接口输出对象
     * @param interfaceData 接口数据
     * @param hasComment 是否有注释
     * @param hasType 是否有类型
     * @param hasValid 是否有验证
     * @param addDefaultValue 是否有默认值
     * @return
     */
    private boolean constructJson(DocsInterfaceVo docsInterfaceVo, InterfaceData interfaceData, boolean hasComment, boolean hasType, boolean hasValid, boolean addDefaultValue) {
        try {
            FieldDataNode fieldDataNode = new FieldDataNode();
            fieldDataNode.setFieldInfoList(interfaceData.getFormData());

            // 请求参数字段数据（json）
            String formDataJson = StringUtil.toFormatJsonStr(fieldDataNode, 0, 2, hasComment, hasType, hasValid, true, addDefaultValue);
            docsInterfaceVo.setFormDataJson(formDataJson);

            // 请求参数字段数据（js object）
            String formDataJsObj = StringUtil.toFormatJsonStr(fieldDataNode, 0, 2, hasComment, hasType, hasValid, false, addDefaultValue);
            docsInterfaceVo.setFormDataJsObj(formDataJsObj);

            // 请求参数字段数据（json）
            String bodyData = StringUtil.toFormatJsonStr(interfaceData.getBodyData(), 0, 2, hasComment, hasType, hasValid, true, addDefaultValue);
            docsInterfaceVo.setBodyData(bodyData);

            // 请求参数字段数据（js object）
            String bodyDataJsObj = StringUtil.toFormatJsonStr(interfaceData.getBodyData(), 0, 2, hasComment, hasType, hasValid, false, addDefaultValue);
            docsInterfaceVo.setBodyDataJsObj(bodyDataJsObj);

            // 响应信息字段数据（json）
            String resData = StringUtil.toFormatJsonStr(interfaceData.getResData(), 0, 2, hasComment, hasType, hasValid, true, addDefaultValue);
            docsInterfaceVo.setResData(resData);

            // 响应信息字段数据（js object）
            String resDataJsObj = StringUtil.toFormatJsonStr(interfaceData.getResData(), 0, 2, hasComment, hasType, hasValid, false, addDefaultValue);
            docsInterfaceVo.setResDataJsObj(resDataJsObj);

            return true;
        } catch (Exception e) {
            logger.error("参数或响应数据转换 异常", e);
            return false;
        }
    }
}
