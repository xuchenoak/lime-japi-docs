package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.*;

import java.util.List;

/**
 * 接口文档业务接口
 *
 * @author: xuchenoak
 * @create: 2022-06-06 8:19
 **/
public interface DocsService {

    /**
     * 获取生成时间集
     * @return
     */
    List<String> getCreateTimes();

    /**
     * 获取接口文档目录
     * @param createTime 生成时间
     * @param likeStr 搜索关键字
     * @return
     */
    List<DocsCatalogVo> getDocsCatalog(String createTime, String likeStr);

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
    List<DocsInterfaceVo> getDocsInterface(String createTime, String controllerId, boolean hasComment, boolean hasType, boolean hasValid, boolean addDefaultValue, String likeStr);

    /**
     * 获取接口文档配置
     * @return
     */
    DocsConfigVo getDocsConfig();

    /**
     * 执行文档解析
     * @param password 解析秘钥
     */
    DocsParseVo runDocsParse(String password);

    /**
     * 获取解析消息
     * @param parseTimestamp 解析时间戳
     * @return
     */
    DocsParseMsgVo getParseMsg(Long parseTimestamp);
}
