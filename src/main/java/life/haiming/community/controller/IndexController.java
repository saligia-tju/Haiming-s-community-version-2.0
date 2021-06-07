package life.haiming.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        //会自动到templates目录下找，名为 "index"的html文件,并返回该页面
        return "index";
    }
}
