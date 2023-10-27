package io.gitee.xuchenoak.limejapidocs.runner.common.exception;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ResCodeEnum;

/**
 * 自定义异常
 */
public class CusExc extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static void e() {
        throw new CusExc();
    }
    public static void e(String msg) {
        throw new CusExc(msg);
    }
    public static void e(String template, Object... v) {
        e(StrUtil.format(template, v));
    }

    public static void e(ResCodeEnum resCodeEnum) {
        throw new CusExc(resCodeEnum.getCode(), resCodeEnum.getMsg());
    }
    public static void e(ResCodeEnum resCodeEnum, String msg) {
        throw new CusExc(resCodeEnum.getCode(), msg);
    }
    public static void e(ResCodeEnum resCodeEnum, String msg, Throwable cause) {
        throw new CusExc(resCodeEnum.getCode(), msg, cause);
    }

    private int code;
    private String msg;

    private CusExc() {
        super(ResCodeEnum.Fail.getMsg());
        this.code = ResCodeEnum.Fail.getCode();
        this.msg = ResCodeEnum.Fail.getMsg();
    }

    private CusExc(String msg) {
        super(msg);
        this.code = ResCodeEnum.Fail.getCode();
        this.msg = msg;
    }

    private CusExc(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    private CusExc(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
