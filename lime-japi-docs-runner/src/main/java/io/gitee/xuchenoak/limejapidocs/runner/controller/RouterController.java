package io.gitee.xuchenoak.limejapidocs.runner.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 路由处理
 *
 * @author xuchenoak
 **/
@Controller
public class RouterController implements ErrorController {

    @GetMapping({"/docs/**", "/init", "/404"})
    public String forward() {
        return "index";
    }

    @RequestMapping("/error")
    public String handleError() {
        return "index";
    }

}
