package io.gitee.xuchenoak.limejapidocs.runner.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.StringCommaListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * interface数据
 *
 * @author: xuchenoak
 * @create: 2022-06-05 22:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("api_docs_interface_data_search")
public class ApiDocsInterfaceDataSearch {

    /**
     * 自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文档配置Id
     */
    private Long docsConfigId;

    /**
     * controller唯一标识
     */
    private String controllerId;

    /**
     * controller名称注释
     */
    private String controllerComment;

    /**
     * controller生成时间
     */
    private Date controllerCreateTime;

    /**
     * 接口唯一标识
     */
    private String interfaceId;

    /**
     * 接口注释
     */
    private String interfaceComment;

    /**
     * 接口uri列表
     */
    private String uriList;

    /**
     * 接口请求方式列表
     */
    private String requestTypeList;

    public ApiDocsInterfaceDataSearch(Long docsConfigId, String controllerId, String controllerComment, Date controllerCreateTime, String interfaceId, String interfaceComment, List<String> uriList, List<String> requestTypeList) {
        this.docsConfigId = docsConfigId;
        this.controllerId = controllerId;
        this.controllerComment = controllerComment;
        this.controllerCreateTime = controllerCreateTime;
        this.interfaceId = interfaceId;
        this.interfaceComment = interfaceComment;
        this.uriList = StringCommaListTypeHandler.toStr(uriList);
        this.requestTypeList = StringCommaListTypeHandler.toStr(requestTypeList);
    }
}
