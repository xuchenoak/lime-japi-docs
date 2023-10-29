package io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * 字符串数组反序列化控制
 *
 * @author xuchenoak
 **/
@MappedTypes({Object.class})
public class StringListTypeHandler extends JacksonTypeHandler {

    public StringListTypeHandler(Class<Object> type) {
        super(type);
    }

    @Override
    protected Object parse(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONUtil.toList(json, String.class);
    }

}
