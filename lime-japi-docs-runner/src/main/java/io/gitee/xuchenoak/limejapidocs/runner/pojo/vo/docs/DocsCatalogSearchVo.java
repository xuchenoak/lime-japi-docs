package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 接口文档目录输出对象
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsCatalogSearchVo {

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
     * 接口列表
     */
    private List<DocsInterfaceSearchVo> interfaceSearchVoList;

}
