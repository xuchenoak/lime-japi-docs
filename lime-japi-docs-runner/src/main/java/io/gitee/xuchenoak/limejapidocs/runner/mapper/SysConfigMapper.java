package io.gitee.xuchenoak.limejapidocs.runner.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.domain.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * mapper
 *
 * @author xuchenoak
 **/
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

}
