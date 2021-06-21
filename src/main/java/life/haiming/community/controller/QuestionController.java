package life.haiming.community.controller;

import life.haiming.community.dto.CommentCreateDTO;
import life.haiming.community.dto.CommentDTO;
import life.haiming.community.dto.QuestionDTO;
import life.haiming.community.service.CommentService;
import life.haiming.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired(required = false)
    private CommentService commentService;

    @GetMapping("/question/{id}")
    // @PathVariable注解的作用：将上面{xxx}内的xx传递到 Integer id中
    public String question(@PathVariable(name = "id") Long id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);

        //返回List，内容为CommentDTO
        List<CommentDTO> comments = commentService.listByQuestionId(id);


        //累加阅读数
        questionService.incView(id);

        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        return "question";
    }

}
