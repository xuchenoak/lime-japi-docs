package io.gitee.xuchenoak.limejapidocs.runner.common.interceptor;

import io.gitee.xuchenoak.limejapidocs.runner.common.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web配置
 *
 * @author xuchenoak
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private DocsParserConfig docsParserConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String docsConfigKey = request.getHeader("Dck");
                if (!docsParserConfig.checkDocsConfigKey(docsConfigKey)) {
                    CusExc.e("无权访问此接口");
                }
                return true;
            }

        }).addPathPatterns("/lime_japi_docs/api/config/**")
                .excludePathPatterns(
                        "/lime_japi_docs/api/config/list",
                        "/lime_japi_docs/api/config/get_docs_config_simple",
                        "/lime_japi_docs/api/config/check_config_key"
                );
    }

}
