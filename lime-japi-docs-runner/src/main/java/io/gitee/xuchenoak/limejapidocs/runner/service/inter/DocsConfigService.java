package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfigrf.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo.DocsConfigVo;

import java.util.List;

/**
 * 文档配置业务接口
 *
 * @author: xuchenoak
 * @create: 2022-06-06 8:19
 **/
public interface DocsConfigService {

    /**
     * 获取文档列表
     * @param docsName 文档名称
     * @return
     */
    List<DocsConfigListVo> list(String docsName);

    /**
     * 获取单个文档配置（简单版）
     * @param id
     * @return
     */
    DocsConfigListVo getDocsConfigSimple(Long id);

    /**
     * 获取单个文档配置
     * @param id
     * @return
     */
    DocsConfigVo getDocsConfig(Long id);

    /**
     * 新增文档
     * @param rf
     * @return
     */
    DocsConfigVo add(DocsConfigAddRf rf);

    /**
     * 编辑文档
     * @param rf
     * @return
     */
    DocsConfigVo edit(DocsConfigEditRf rf);

    /**
     * 删除文档
     * @param id
     */
    void del(Long id);

}
