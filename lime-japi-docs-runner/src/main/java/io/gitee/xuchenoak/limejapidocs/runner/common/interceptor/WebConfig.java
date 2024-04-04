package io.gitee.xuchenoak.limejapidocs.runner.common.interceptor;

import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.SysConfigService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * web配置
 *
 * @author xuchenoak
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String account = Optional.ofNullable(request.getHeader("Uacc")).orElse("");
                String password = Optional.ofNullable(request.getHeader("Upas")).orElse("");
                if (!sysConfigService.isLogin(account, password)) {
                    CusExc.e("无权访问此接口");
                }
                return true;
            }

        }).addPathPatterns("/lime_japi_docs/api/docs_config/**", "/lime_japi_docs/api/sys_config/**")
                .excludePathPatterns(
                        "/lime_japi_docs/api/docs_config/list",
                        "/lime_japi_docs/api/docs_config/get_docs_config_simple",
                        "/lime_japi_docs/api/sys_config/common"
                );
    }

}
