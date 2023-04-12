package io.gitee.xuchenoak.limejapidocs.runner.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * mapper
 *
 * @author xuchenoak
 **/
@Mapper
public interface ApiDocsControllerDataMapper extends BaseMapper<ApiDocsControllerData> {

    @Select("select distinct create_time from api_docs_controller_data order by create_time desc")
    List<String> listCreateTime();

}
