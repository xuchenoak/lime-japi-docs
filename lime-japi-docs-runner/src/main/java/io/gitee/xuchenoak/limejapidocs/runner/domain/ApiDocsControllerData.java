package io.gitee.xuchenoak.limejapidocs.runner.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * controller数据
 *
 * @author: xuchenoak
 * @create: 2022-06-05 22:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("api_docs_controller_data")
public class ApiDocsControllerData {

    /** 自增Id */
    @TableId(value = "id", type= IdType.AUTO)
    private Long id;

    /** controller唯一标识 */
    private String controllerId;

    /** controller类全名 */
    private String controllerFullName;

    /** controller名称注释 */
    private String comment;

    /** 请求前缀 */
    private String baseUriList;

    /** 接口方法集 */
    private String interfaceDataList;

    /** 排序 */
    private Integer sort;

    /** 生成时间 */
    private Date createTime;

}
