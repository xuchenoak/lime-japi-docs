package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigVo;

import java.util.List;

/**
 * 文档配置业务接口
 *
 * @author xuchenoak
 **/
public interface DocsConfigService {

    /**
     * 获取文档列表
     *
     * @param docsName 文档名称
     * @param ids      Ids
     * @return
     */
    List<DocsConfigListVo> list(String docsName, List<Long> ids);

    /**
     * 获取单个文档配置（简单版）
     *
     * @param id
     * @return
     */
    DocsConfigListVo getDocsConfigSimple(Long id);

    /**
     * 获取单个文档配置
     *
     * @param id
     * @return
     */
    DocsConfigVo getDocsConfig(Long id);

    /**
     * 新增文档
     *
     * @param rf
     * @return
     */
    DocsConfigVo add(DocsConfigAddRf rf);

    /**
     * 编辑文档
     *
     * @param rf
     * @return
     */
    DocsConfigVo edit(DocsConfigEditRf rf);

    /**
     * 删除文档
     *
     * @param id
     */
    void del(Long id);

}
