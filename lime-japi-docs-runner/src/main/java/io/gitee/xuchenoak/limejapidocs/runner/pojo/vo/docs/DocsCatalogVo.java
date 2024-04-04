package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口文档目录输出对象
 *
 * @author: xuchenoak
 * @create: 2022-06-06 8:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsCatalogVo {

    /** 目录标识 */
    private String id;

    /** 目录名称 */
    private String name;

    /** 排序 */
    private Integer sort;

}
