package io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * 字符串数组反序列化控制（JSON数组格式存储）
 * <p>
 * 注意：继承AbstractJsonTypeHandler而非JacksonTypeHandler——Boot 4使用Jackson 3（tools.jackson），
 * 而JacksonTypeHandler内部引用Jackson 2的ObjectMapper，会在启动链接阶段抛ClassNotFoundException。
 *
 * @author xuchenoak
 **/
@MappedTypes({Object.class})
public class StringListTypeHandler extends AbstractJsonTypeHandler<Object> {

    public StringListTypeHandler(Class<Object> type) {
        super(type);
    }

    @Override
    public Object parse(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONUtil.toList(json, String.class);
    }

    @Override
    public String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }

    public static String toStr(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }


    public static List<String> parseToList(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONUtil.toList(json, String.class);
    }

}
