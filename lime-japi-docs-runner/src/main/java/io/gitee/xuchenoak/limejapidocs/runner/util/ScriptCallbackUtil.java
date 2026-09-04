package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.script.ScriptUtil;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JS回调脚本执行工具
 * <p>
 * 同一回调源码只编译一次，后续每次调用仅按唯一别名执行（invokeFunction），
 * 避免逐字段重复eval导致GraalVM JS引擎堆外内存随解析次数累积。
 *
 * @author xuchenoak
 **/
public class ScriptCallbackUtil {

    /**
     * 已成功编译的回调集（函数名 + 源码）
     */
    private static final Set<String> EVALED = ConcurrentHashMap.newKeySet();

    /**
     * 编译失败的回调集，避免对异常脚本反复编译
     */
    private static final Set<String> FAILED = ConcurrentHashMap.newKeySet();

    /**
     * 调用JS回调函数
     *
     * @param func     回调函数源码
     * @param funcName 函数名（valid/defaultValue）
     * @param args     调用参数
     * @return 回调返回值，func为空时返回null
     */
    public static Object invoke(String func, String funcName, Object... args) throws ScriptException, NoSuchMethodException {
        if (StrUtil.isBlank(func)) {
            return null;
        }
        ScriptEngine engine = ScriptUtil.getJsEngine();
        String cacheKey = funcName + "\u0000" + func;
        String alias = aliasOf(func, funcName);
        if (!EVALED.contains(cacheKey)) {
            synchronized (engine) {
                if (!EVALED.contains(cacheKey) && !FAILED.contains(cacheKey)) {
                    try {
                        engine.eval(buildBridge(func, funcName, alias));
                        EVALED.add(cacheKey);
                    } catch (ScriptException e) {
                        FAILED.add(cacheKey);
                        throw e;
                    }
                }
            }
        }
        synchronized (engine) {
            return ((Invocable) engine).invokeFunction(alias, args);
        }
    }

    /**
     * 生成一次性桥接脚本：执行回调源码后，将指定函数注册到唯一全局别名，供后续按别名直接调用
     *
     * @param func     回调函数源码
     * @param funcName 函数名（valid/defaultValue）
     * @param alias    唯一全局别名
     * @return 桥接脚本
     */
    private static String buildBridge(String func, String funcName, String alias) {
        if ("valid".equals(funcName)) {
            // valid入参契约：第一个参数为注解名JSON字符串，桥接层JSON.parse还原为JS数组
            return func + "; this." + alias + " = function(names, fieldName, fieldComment) { return valid(JSON.parse(names), fieldName, fieldComment); };";
        }
        return func + "; this." + alias + " = function(type, fieldName, fieldComment) { return defaultValue(type, fieldName, fieldComment); };";
    }

    /**
     * 生成唯一全局别名（函数名 + 源码MD5）
     */
    private static String aliasOf(String func, String funcName) {
        return "__ljd_fn_" + funcName + "_" + SecureUtil.md5(func);
    }

}