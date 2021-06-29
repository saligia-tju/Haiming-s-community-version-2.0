package life.haiming.community.controller;

import life.haiming.community.dto.NotificationDTO;
import life.haiming.community.dto.PaginationDTO;
import life.haiming.community.enums.NotificationTypeEnum;
import life.haiming.community.mapper.NotificationMapper;
import life.haiming.community.model.User;
import life.haiming.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired(required = false)
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        //未登录跳转主页
        if (user == null) {
            return "redirect:/";
        }

        //校验是否是自己回复的
        NotificationDTO notificationDTO = notificationService.read(id,user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        return "profile";
    }
}