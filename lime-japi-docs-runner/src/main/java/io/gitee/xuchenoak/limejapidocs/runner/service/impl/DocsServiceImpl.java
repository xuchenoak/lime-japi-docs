package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldDataNode;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.SearchFromEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.domain.*;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.*;
import io.gitee.xuchenoak.limejapidocs.runner.runner.DocsParseService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.*;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsService;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口文档业务实现
 *
 * @author xuchenoak
 **/
@Service
public class DocsServiceImpl implements DocsService {
    @Resource
    private ApiDocsInterfaceDataSearchService apiDocsInterfaceDataSearchService;


    private static final Logger logger = LoggerFactory.getLogger(DocsServiceImpl.class);

    @Resource
    private ApiDocsInterfaceDataService apiDocsInterfaceDataService;

    @Resource
    private DocsParseService docsParseService;

    @Resource
    private ApiDocsParseLogService apiDocsParseLogService;

    @Resource
    private ApiDocsControllerDataService apiDocsControllerDataService;

    @Resource
    private ApiDocsConfigService apiDocsConfigService;

    /**
     * 获取生成时间集
     *
     * @param docsConfigId 文档配置Id
     * @return
     */
    @Override
    public List<String> getCreateTimes(Long docsConfigId) {
        List<String> createTimeList = ListUtils.listOrEmpty(apiDocsControllerDataService.getBaseMapper().listCreateTime(docsConfigId));
        try {
            if (createTimeList.size() > 0) {
                DateTime beforeTime = DateUtil.parse(createTimeList.get(createTimeList.size() - 1));
                apiDocsControllerDataService.remove(new LambdaQueryWrapper<ApiDocsControllerData>()
                        .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                        .lt(ApiDocsControllerData::getCreateTime, beforeTime));
                apiDocsParseLogService.remove(new LambdaQueryWrapper<ApiDocsParseLog>()
                        .eq(ApiDocsParseLog::getDocsConfigId, docsConfigId)
                        .lt(ApiDocsParseLog::getCreateTimestamp, beforeTime.getTime()));
            }
        } catch (Exception e) {
            logger.error("清除旧数据异常", e);
        }
        return createTimeList;
    }

    /**
     * 获取接口文档目录
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @param likeStr      搜索关键字
     * @return
     */
    @Override
    public List<DocsCatalogVo> getDocsCatalog(Long docsConfigId, String createTime, String likeStr) {
        List<DocsCatalogVo> docsCatalogVoList = new ArrayList<>();
        List<ApiDocsControllerData> controllerDataList = apiDocsControllerDataService.list(q -> q
                .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsControllerData::getCreateTime, createTime)
                .like(StringUtil.isNotBlank(likeStr), ApiDocsControllerData::getComment, likeStr)
                .orderByAsc(ApiDocsControllerData::getComment));
        if (ListUtil.isBlank(controllerDataList)) {
            return docsCatalogVoList;
        }
        for (ApiDocsControllerData controllerData : controllerDataList) {
            if (StringUtil.isBlank(controllerData.getComment())) {
                continue;
            }
            docsCatalogVoList.add(new DocsCatalogVo(
                    controllerData.getControllerId(),
                    controllerData.getComment(),
                    controllerData.getSort()));
        }
        return docsCatalogVoList;
    }

    /**
     * 获取文档搜索列表
     *
     * @param docsConfigId   文档配置Id
     * @param createTime     生成时间
     * @param searchFromEnum 搜索来源
     * @param likeStr        搜索关键字
     * @return
     */
    @Override
    public List<DocsCatalogSearchVo> listDocsSearch(Long docsConfigId, String createTime, SearchFromEnum searchFromEnum, String likeStr) {
        // 只搜索目录
        if (searchFromEnum.equals(SearchFromEnum.CATALOG)) {
            return searchFromCatalog(docsConfigId, createTime, likeStr);
        }
        // 只搜索接口
        if (searchFromEnum.equals(SearchFromEnum.INTERFACE)) {
            return searchFromInterface(docsConfigId, createTime, likeStr);
        }
        // 搜索全部
        if (searchFromEnum.equals(SearchFromEnum.ALL)) {
            List<DocsCatalogSearchVo> catalogSearchVos = searchFromCatalog(docsConfigId, createTime, likeStr);
            List<DocsCatalogSearchVo> interfaceSearchVos = searchFromInterface(docsConfigId, createTime, likeStr);
            if (ListUtil.isBlank(catalogSearchVos)) {
                return interfaceSearchVos;
            }
            if (ListUtil.isBlank(interfaceSearchVos)) {
                return catalogSearchVos;
            }
            List<DocsCatalogSearchVo> voList = new ArrayList<>();
            Set<String> interfaceSearchControllerIds = interfaceSearchVos.stream().map(DocsCatalogSearchVo::getControllerId).collect(Collectors.toSet());
            for (DocsCatalogSearchVo catalogSearchVo : catalogSearchVos) {
                if (!interfaceSearchControllerIds.contains(catalogSearchVo.getControllerId())) {
                    voList.add(catalogSearchVo);
                }
            }
            voList.addAll(interfaceSearchVos);
            return voList.stream().sorted(Comparator.comparing(DocsCatalogSearchVo::getControllerComment)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 根据目录搜索
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @param likeStr      搜索关键字
     * @return
     */
    private List<DocsCatalogSearchVo> searchFromCatalog(Long docsConfigId, String createTime, String likeStr) {
        List<DocsCatalogSearchVo> voList = new ArrayList<>();
        List<ApiDocsControllerData> controllerDataList = apiDocsControllerDataService.list(q -> q
                .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsControllerData::getCreateTime, createTime)
                .like(ApiDocsControllerData::getComment, likeStr)
                .orderByAsc(ApiDocsControllerData::getComment));
        if (ListUtil.isBlank(controllerDataList)) {
            return voList;
        }
        for (ApiDocsControllerData data : controllerDataList) {
            voList.add(new DocsCatalogSearchVo(
                    docsConfigId,
                    data.getControllerId(),
                    data.getComment(),
                    data.getCreateTime(),
                    new ArrayList<>()
            ));
        }
        return voList;
    }

    /**
     * 根据接口搜索
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @param likeStr      搜索关键字
     * @return
     */
    private List<DocsCatalogSearchVo> searchFromInterface(Long docsConfigId, String createTime, String likeStr) {
        List<DocsCatalogSearchVo> voList = new ArrayList<>();
        List<ApiDocsInterfaceDataSearch> interfaceList = apiDocsInterfaceDataSearchService.list(q -> q
                .eq(ApiDocsInterfaceDataSearch::getDocsConfigId, docsConfigId)
                .eq(ApiDocsInterfaceDataSearch::getControllerCreateTime, createTime)
                .and(lq -> lq.like(ApiDocsInterfaceDataSearch::getInterfaceComment, likeStr)
                        .or().like(ApiDocsInterfaceDataSearch::getUriList, likeStr)));
        if (ListUtil.isBlank(interfaceList)) {
            return voList;
        }
        List<ApiDocsControllerData> controllerDataList = apiDocsControllerDataService.list(q -> q
                .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsControllerData::getCreateTime, createTime)
                .in(ApiDocsControllerData::getControllerId, interfaceList.stream().map(ApiDocsInterfaceDataSearch::getControllerId).collect(Collectors.toSet()))
                .orderByAsc(ApiDocsControllerData::getComment));
        if (ListUtil.isBlank(controllerDataList)) {
            return voList;
        }
        Map<String, List<DocsInterfaceSearchVo>> controllerIdInterfaceMap = new HashMap<>();
        for (ApiDocsInterfaceDataSearch interfaceDataSearch : interfaceList) {
            List<DocsInterfaceSearchVo> interfaceSearchVos = controllerIdInterfaceMap.get(interfaceDataSearch.getControllerId());
            if (interfaceSearchVos == null) {
                interfaceSearchVos = new ArrayList<>();
                controllerIdInterfaceMap.put(interfaceDataSearch.getControllerId(), interfaceSearchVos);
            }
            interfaceSearchVos.add(new DocsInterfaceSearchVo(
                    interfaceDataSearch.getDocsConfigId(),
                    interfaceDataSearch.getControllerId(),
                    interfaceDataSearch.getControllerComment(),
                    interfaceDataSearch.getControllerCreateTime(),
                    interfaceDataSearch.getInterfaceId(),
                    interfaceDataSearch.getInterfaceComment(),
                    interfaceDataSearch.getUriList(),
                    interfaceDataSearch.getRequestTypeList()
            ));
        }
        for (ApiDocsControllerData controllerData : controllerDataList) {
            List<DocsInterfaceSearchVo> interfaceSearchVos = controllerIdInterfaceMap.get(controllerData.getControllerId());
            if (ListUtil.isBlank(interfaceSearchVos)) {
                continue;
            }
            voList.add(new DocsCatalogSearchVo(
                    docsConfigId,
                    controllerData.getControllerId(),
                    controllerData.getComment(),
                    controllerData.getCreateTime(),
                    interfaceSearchVos
            ));
        }
        return voList;
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
    @Override
    public List<DocsInterfaceVo> getDocsInterface(Long docsConfigId, String createTime, String controllerId, boolean hasComment, boolean hasType, boolean hasValid, boolean addDefaultValue, String likeStr) {
        List<DocsInterfaceVo> docsInterfaceVoList = new ArrayList<>();
        ApiDocsControllerData controllerData = apiDocsControllerDataService.getOne(q -> q
                .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsControllerData::getCreateTime, createTime)
                .eq(ApiDocsControllerData::getControllerId, controllerId));
        if (controllerData == null) {
            return docsInterfaceVoList;
        }
        ApiDocsInterfaceData apiDocsInterfaceData = apiDocsInterfaceDataService.getOne(q -> q
                .eq(ApiDocsInterfaceData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsInterfaceData::getControllerCreateTime, createTime)
                .eq(ApiDocsInterfaceData::getControllerId, controllerId));
        if (apiDocsInterfaceData == null || StrUtil.isBlank(apiDocsInterfaceData.getInterfaceDataList())) {
            return docsInterfaceVoList;
        }
        List<InterfaceData> interfaceDataList = JSONUtil.toList(apiDocsInterfaceData.getInterfaceDataList(), InterfaceData.class);
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
     * 执行文档解析
     *
     * @param docsConfigId 文档配置Id
     * @param password     解析秘钥
     */
    @Override
    public DocsParseVo runDocsParse(Long docsConfigId, String password) {
        ApiDocsConfig docsConfig = apiDocsConfigService.getById(docsConfigId);
        if (docsConfig == null) {
            CusExc.e("文档不存在");
        }
        // 验证解析秘钥
        if (!docsConfig.checkedApiRun(password)) {
            return new DocsParseVo(false, null, "生成秘钥错误！请输入正确解析秘钥再重试");
        }
        // 验证是否还在进行解析
        if (MsgUtil.isParseRun(docsConfigId)) {
            return new DocsParseVo(true, MsgUtil.getParseTimestamp(docsConfigId), "正在生成");
        }
        // 执行解析
        MsgUtil.statusParseRun(docsConfigId);
        docsParseService.runParseAsync(docsConfig);
        return new DocsParseVo(true, MsgUtil.getParseTimestamp(docsConfigId), "开始生成");
    }

    /**
     * 获取解析消息
     *
     * @param docsConfigId   文档配置Id
     * @param parseTimestamp 解析时间戳
     * @return
     */
    @Override
    public DocsParseMsgVo getParseMsg(Long docsConfigId, Long parseTimestamp) {
        // 验证是否还在进行解析
        if (parseTimestamp == null) {
            return new DocsParseMsgVo(MsgUtil.isParseRun(docsConfigId), MsgUtil.getParseTimestamp(docsConfigId), new ArrayList<>());
        }
        return new DocsParseMsgVo(MsgUtil.isParseRun(docsConfigId), parseTimestamp,
                apiDocsParseLogService.listMsg(docsConfigId, parseTimestamp));
    }

    /**
     * 构造json字符串
     *
     * @param docsInterfaceVo 接口输出对象
     * @param interfaceData   接口数据
     * @param hasComment      是否有注释
     * @param hasType         是否有类型
     * @param hasValid        是否有验证
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
