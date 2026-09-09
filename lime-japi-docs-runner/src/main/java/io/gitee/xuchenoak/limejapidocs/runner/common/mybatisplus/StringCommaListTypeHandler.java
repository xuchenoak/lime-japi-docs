package io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * 英文逗号分隔字符串与数组互转控制
 *
 * @author xuchenoak
 **/
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringCommaListTypeHandler extends AbstractJsonTypeHandler<List<String>> {

    public StringCommaListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<String> parse(String json) {
        return parseToList(json);
    }

    @Override
    public String toJson(List<String> obj) {
        return toStr(obj);
    }

    public static String toStr(List<String> obj) {
        return String.join(",", obj);
    }

    public static List<String> parseToList(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        //StrSplitter.splitTrim(str-被切分的字符串, separator-分隔符字符, limit-限制分片数(-1不限制), ignoreEmpty-是否忽略空串)
        return StrSplitter.splitTrim(json, ',', -1, true);
    }

}
