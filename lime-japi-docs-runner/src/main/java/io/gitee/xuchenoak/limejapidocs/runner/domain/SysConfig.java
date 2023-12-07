package io.gitee.xuchenoak.limejapidocs.runner.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_config")
public class SysConfig {

    /** Id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 配置标识 */
    private String key;

    /** 配置值 */
    private String value;

}
