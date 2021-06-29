package life.haiming.community.controller;

import life.haiming.community.dto.PaginationDTO;
import life.haiming.community.model.User;
import life.haiming.community.service.NotificationService;
import life.haiming.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired(required = false)
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".contains(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");

            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".contains(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
/*          功能移入navigation中
 *          Long unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("unreadCount", unreadCount);*/
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
       /* switch (action){
            case "questions":
                model.addAttribute("section","questions");
                model.addAttribute("sectionName","我的提问");
                break;
            case "replies":
                model.addAttribute("section","replies");
                model.addAttribute("sectionName","最新回复");
                break;
        }*/
        return "profile";
    }

}
