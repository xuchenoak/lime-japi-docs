package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.DocsCatalogVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.DocsInterfaceVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.DocsParseMsgVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs.DocsParseVo;

import java.util.List;

/**
 * 接口文档业务接口
 *
 * @author xuchenoak
 **/
public interface DocsService {

    /**
     * 获取生成时间集
     * @param docsConfigId 文档配置Id
     * @return
     */
    List<String> getCreateTimes(Long docsConfigId);

    /**
     * 获取接口文档目录
     * @param docsConfigId 文档配置Id
     * @param createTime 生成时间
     * @param likeStr 搜索关键字
     * @return
     */
    List<DocsCatalogVo> getDocsCatalog(Long docsConfigId, String createTime, String likeStr);

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
     * 执行文档解析
     * @param docsConfigId 文档配置Id
     * @param password 解析秘钥
     */
    DocsParseVo runDocsParse(Long docsConfigId, String password);

    /**
     * 获取解析消息
     * @param docsConfigId 文档配置Id
     * @param parseTimestamp 解析时间戳
     * @return
     */
    DocsParseMsgVo getParseMsg(Long docsConfigId, Long parseTimestamp);
}
