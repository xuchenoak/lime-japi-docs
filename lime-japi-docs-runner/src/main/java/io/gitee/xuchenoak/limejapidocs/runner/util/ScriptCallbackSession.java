package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.util.StrUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * JS回调脚本会话（polyglot）
 * <p>
 * 每个解析会话使用一个独立的GraalJS Context：回调源码只编译一次，每字段仅执行（execute），
 * 解析结束后调用{@link #close()}关闭Context，释放GraalVM堆外内存，避免内存随解析次数累积。
 *
 * @author xuchenoak
 **/
public class ScriptCallbackSession implements AutoCloseable {

    private Context context;

    private Map<String, Value> fnCache;

    /**
     * 调用JS回调函数
     *
     * @param func     回调函数源码
     * @param funcName 函数名（valid/defaultValue）
     * @param args     调用参数
     * @return 回调返回值，func为空时返回null
     */
    public Object invoke(String func, String funcName, Object... args) {
        if (StrUtil.isBlank(func)) {
            return null;
        }
        ensureContext();
        Value fn = fnCache.computeIfAbsent(funcName + "\u0000" + func, k -> context.eval("js", buildBridge(func, funcName)));
        Value result = fn.execute(args);
        if (result == null || result.isNull()) {
            return null;
        }
        return result.isString() ? result.asString() : result.toString();
    }

    @Override
    public void close() {
        if (context != null) {
            context.close();
            context = null;
            fnCache = null;
        }
    }

    private void ensureContext() {
        if (context == null) {
            context = Context.newBuilder("js").allowAllAccess(true).build();
            fnCache = new HashMap<>();
        }
    }

    /**
     * 生成一次性桥接脚本：执行回调源码后，返回包一层桥接函数的函数表达式。
     * valid入参契约：第一个参数为注解名JSON字符串，桥接层JSON.parse还原为JS数组
     */
    private static String buildBridge(String func, String funcName) {
        if ("valid".equals(funcName)) {
            return func + "; (function(names, fieldName, fieldComment) { return valid(JSON.parse(names), fieldName, fieldComment); })";
        }
        return func + "; (function(type, fieldName, fieldComment) { return defaultValue(type, fieldName, fieldComment); })";
    }

}