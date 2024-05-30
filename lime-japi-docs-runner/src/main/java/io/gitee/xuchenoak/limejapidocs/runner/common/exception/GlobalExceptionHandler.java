package io.gitee.xuchenoak.limejapidocs.runner.common.exception;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.AjaxResult;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ResCodeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CusExc.class)
    @ResponseBody
    public AjaxResult bizExceptionHandler(CusExc e) {
        return AjaxResult.error(e.getCode(), e.getMsg());
    }

    /**
     * 不合法参数
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public AjaxResult exceptionHandler(IllegalArgumentException e) {
        logger.error("不合法参数异常", e);
        return AjaxResult.error("参数不合法");
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public AjaxResult handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (ListUtils.isBlank(fieldErrors)) {
            allErrors.forEach((fieldError) -> {
                errorMap.put("error", fieldError.getDefaultMessage());
            });
        }
        fieldErrors.forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return AjaxResult.error(ResCodeEnum.ParamError, errorMap);
    }

    /**
     * 空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public AjaxResult exceptionHandler(NullPointerException e) {
        logger.error("空指针异常", e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 数据库的异常
     */
    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public AjaxResult exceptionHandler(SQLException e) {
        logger.error("sql执行异常", e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 请求方式错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public AjaxResult exceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("接口请示方式错误", e);
        return AjaxResult.error("接口请示方式错误");
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResult exceptionHandler(Exception e) {
        logger.error("未知异常", e);
        return AjaxResult.error(e.getMessage());
    }


}
