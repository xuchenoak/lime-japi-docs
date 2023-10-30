package io.gitee.xuchenoak.limejapidocs.runner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 路由处理
 *
 * @author xuchenoak
 **/
@Controller
public class RouterController {

    @GetMapping("/docs/**")
    public String forward() {
        return "index";
    }

}
