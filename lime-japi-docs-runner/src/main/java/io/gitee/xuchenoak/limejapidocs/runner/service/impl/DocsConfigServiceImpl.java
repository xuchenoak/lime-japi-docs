package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsInterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsInterfaceDataSearch;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsParseLog;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigAddRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docsconfig.DocsConfigEditRf;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigListVo;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig.DocsConfigVo;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsControllerDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsInterfaceDataSearchService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsInterfaceDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsParseLogService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 文档配置业务实现
 *
 * @author xuchenoak
 **/
@Service
public class DocsConfigServiceImpl implements DocsConfigService {

    @Resource
    private ApiDocsConfigService apiDocsConfigService;

    @Resource
    private ApiDocsControllerDataService apiDocsControllerDataService;

    @Resource
    private ApiDocsParseLogService apiDocsParseLogService;

    @Resource
    private ApiDocsInterfaceDataService apiDocsInterfaceDataService;

    @Resource
    private ApiDocsInterfaceDataSearchService apiDocsInterfaceDataSearchService;

    /**
     * 获取文档列表
     *
     * @param docsName 文档名称
     * @param ids      Ids
     * @return
     */
    @Override
    public List<DocsConfigListVo> list(String docsName, List<Long> ids) {
        return ListUtils.listOrMapDoing(apiDocsConfigService.list(new LambdaQueryWrapper<ApiDocsConfig>()
                        .like(StrUtil.isNotBlank(docsName), ApiDocsConfig::getDocsName, docsName)
                        .in(ListUtils.isNotBlank(ids), ApiDocsConfig::getId, ids)
                        .orderByDesc(ApiDocsConfig::getSort)),
                bean -> new DocsConfigListVo(
                        IdUtils.encryptId(bean.getId()),
                        bean.getDocsName(),
                        bean.getDocsVersion(),
                        bean.getSort(),
                        bean.getCreateTime(),
                        bean.getUpdateTime()
                ));
    }

    /**
     * 获取单个文档配置（简单版）
     *
     * @param id
     * @return
     */
    @Override
    public DocsConfigListVo getDocsConfigSimple(Long id) {
        ApiDocsConfig bean = apiDocsConfigService.getById(id);
        if (bean == null) {
            return null;
        }
        return new DocsConfigListVo(
                IdUtils.encryptId(bean.getId()),
                bean.getDocsName(),
                bean.getDocsVersion(),
                bean.getSort(),
                bean.getCreateTime(),
                bean.getUpdateTime()
        );
    }

    /**
     * 获取单个文档配置
     *
     * @param id
     * @return
     */
    @Override
    public DocsConfigVo getDocsConfig(Long id) {
        ApiDocsConfig bean = apiDocsConfigService.getById(id);
        if (bean == null) {
            return null;
        }
        return new DocsConfigVo(
                IdUtils.encryptId(bean.getId()),
                bean.getDocsName(),
                bean.getDocsVersion(),
                bean.getSysStartParse(),
                bean.getApiRunKey(),
                bean.getJavaFilePaths(),
                bean.getFilterPackages(),
                bean.getFilterClassNames(),
                bean.getIgnoreClassNames(),
                bean.getParamValidFunc(),
                bean.getParamDefaultValueFunc(),
                bean.getSort(),
                bean.getDocsKey(),
                bean.getCodeSource(),
                bean.getGitUrl(),
                bean.getGitBranch(),
                bean.getGitUsername(),
                bean.getGitPassword(),
                bean.getCreateTime(),
                bean.getUpdateTime()
        );
    }

    /**
     * 新增文档
     *
     * @param rf
     * @return
     */
    @Override
    public DocsConfigVo add(DocsConfigAddRf rf) {
        DateTime now = DateUtil.date();
        ApiDocsConfig bean = new ApiDocsConfig(
                rf.getDocsName(),
                rf.getDocsVersion(),
                rf.getSysStartParse(),
                rf.getApiRunKey(),
                rf.getJavaFilePaths(),
                rf.getFilterPackages(),
                rf.getFilterClassNames(),
                rf.getIgnoreClassNames(),
                rf.getParamValidFunc(),
                rf.getParamDefaultValueFunc(),
                Optional.ofNullable(rf.getSort()).orElse(0),
                rf.getDocsKey(),
                rf.getCodeSource(),
                rf.getGitUrl(),
                rf.getGitBranch(),
                rf.getGitUsername(),
                rf.getGitPassword(),
                now,
                now
        );
        apiDocsConfigService.add(bean);
        return getDocsConfig(bean.getId());
    }

    /**
     * 编辑文档
     *
     * @param rf
     * @return
     */
    @Override
    public DocsConfigVo edit(DocsConfigEditRf rf) {
        Long id = IdUtils.decryptIdOrExc(rf.getId());
        ApiDocsConfig bean = apiDocsConfigService.getById(id);
        if (bean == null) {
            CusExc.e("文档配置不存在");
        }
        bean.buildEdit(
                rf.getDocsName(),
                rf.getDocsVersion(),
                rf.getSysStartParse(),
                rf.getApiRunKey(),
                rf.getJavaFilePaths(),
                rf.getFilterPackages(),
                rf.getFilterClassNames(),
                rf.getIgnoreClassNames(),
                rf.getParamValidFunc(),
                rf.getParamDefaultValueFunc(),
                Optional.ofNullable(rf.getSort()).orElse(0),
                rf.getDocsKey(),
                rf.getCodeSource(),
                rf.getGitUrl(),
                rf.getGitBranch(),
                rf.getGitUsername(),
                rf.getGitPassword()
        );
        apiDocsConfigService.edit(bean);
        return getDocsConfig(id);
    }

    /**
     * 删除文档
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(Long id) {
        ApiDocsConfig bean = apiDocsConfigService.getById(id);
        if (bean == null) {
            CusExc.e("文档配置不存在");
        }
        apiDocsConfigService.removeById(id);
        apiDocsControllerDataService.remove(new LambdaUpdateWrapper<ApiDocsControllerData>().eq(ApiDocsControllerData::getDocsConfigId, id));
        apiDocsInterfaceDataService.remove(new LambdaUpdateWrapper<ApiDocsInterfaceData>().eq(ApiDocsInterfaceData::getDocsConfigId, id));
        apiDocsInterfaceDataSearchService.remove(new LambdaUpdateWrapper<ApiDocsInterfaceDataSearch>().eq(ApiDocsInterfaceDataSearch::getDocsConfigId, id));
        apiDocsParseLogService.remove(new LambdaUpdateWrapper<ApiDocsParseLog>().eq(ApiDocsParseLog::getDocsConfigId, id));
        MsgUtil.clear(id);
    }
}
