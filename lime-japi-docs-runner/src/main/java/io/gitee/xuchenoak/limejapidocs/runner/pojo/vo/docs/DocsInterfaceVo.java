package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs;

import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 接口文档数据输出对象
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsInterfaceVo {

    /**
     * 接口唯一标识
     */
    private String interfaceId;

    /**
     * controller唯一标识
     */
    private String controllerId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口方法类全名
     */
    private String interfaceFullName;

    /**
     * 请求地址
     */
    private List<String> uriList;

    /**
     * 请求方式
     */
    private List<String> requestTypeList;

    /**
     * 请求参数类型
     */
    private String requestContentType;

    /**
     * 请求参数字段数据（表单）
     */
    private List<FieldInfo> formData;

    /**
     * 请求参数字段数据（json）
     */
    private String formDataJson;

    /**
     * 请求参数字段数据（js object）
     */
    private String formDataJsObj;

    /**
     * 请求参数字段数据（json）
     */
    private String bodyData;

    /**
     * 请求参数字段数据（js object）
     */
    private String bodyDataJsObj;

    /**
     * 响应信息字段数据（json）
     */
    private String resData;

    /**
     * 响应信息字段数据（js object）
     */
    private String resDataJsObj;

    /**
     * 排序
     */
    private Integer sort;

    public DocsInterfaceVo(String interfaceId, String controllerId, String interfaceName, String interfaceFullName, List<String> uriList, List<String> requestTypeList, String requestContentType, List<FieldInfo> formData, Integer sort) {
        this.interfaceId = interfaceId;
        this.controllerId = controllerId;
        this.interfaceName = interfaceName;
        this.interfaceFullName = interfaceFullName;
        this.uriList = uriList;
        this.requestTypeList = requestTypeList;
        this.requestContentType = requestContentType;
        this.formData = formData;
        this.sort = sort;
    }
}
