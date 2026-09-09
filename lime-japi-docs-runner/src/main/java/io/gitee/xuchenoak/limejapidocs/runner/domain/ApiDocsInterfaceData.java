package io.gitee.xuchenoak.limejapidocs.runner.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * interface数据
 *
 * @author: xuchenoak
 * @create: 2022-06-05 22:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("api_docs_interface_data")
public class ApiDocsInterfaceData {

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
     * 接口方法集
     */
    private String interfaceDataList;

    public ApiDocsInterfaceData(Long docsConfigId, String controllerId, String controllerComment, Date controllerCreateTime, String interfaceDataList) {
        this.docsConfigId = docsConfigId;
        this.controllerId = controllerId;
        this.controllerComment = controllerComment;
        this.controllerCreateTime = controllerCreateTime;
        this.interfaceDataList = interfaceDataList;
    }
}
