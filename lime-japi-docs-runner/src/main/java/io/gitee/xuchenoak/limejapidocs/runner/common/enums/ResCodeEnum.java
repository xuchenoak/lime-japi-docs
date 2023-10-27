package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

/**
 * 响应状态码
 *
 * @author xuchenoak
 **/
public enum ResCodeEnum {

    // 公共状态码访问 0-1000
    Succeed(200, "操作成功"),
    Fail(505, "操作失败"),
    Error(500, "服务器错误"),

    ParamError(501, "参数错误"),

    ;

    /** 标识 */
    private int code;

    /** 描述 */
    private String msg;

    ResCodeEnum(int key, String msg) {
        this.code = key;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
