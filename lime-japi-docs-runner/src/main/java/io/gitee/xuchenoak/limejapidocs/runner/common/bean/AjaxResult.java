package io.gitee.xuchenoak.limejapidocs.runner.common.bean;

import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ResCodeEnum;

/**
 * 响应数据对象
 * @author xuchenoak
 * @create 2022-07-21 20:22
 */
public class AjaxResult<T> {

    /** 状态码 */
    private int code;

    /** 信息 */
    private String msg;

    /** 数据 */
    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private AjaxResult() {
    }

    private AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg =  msg;
        this.data = data;
    }

    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    public static <T> AjaxResult success(T data) {
        return AjaxResult.success("操作成功", data);
    }


    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    public static <T> AjaxResult success(String msg, T data) {
        return new AjaxResult(ResCodeEnum.Succeed.getCode(), msg, data);
    }

    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    public static AjaxResult error(String msg) {
        return AjaxResult.error(ResCodeEnum.Fail.getCode(), msg);
    }

    public static AjaxResult error(Integer code, String msg) {
        return new AjaxResult(code, msg);
    }

    public static <T> AjaxResult error(ResCodeEnum e, T data) {
        return new AjaxResult(e.getCode(), e.getMsg(), data);
    }


}
