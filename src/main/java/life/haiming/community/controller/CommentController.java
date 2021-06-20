package life.haiming.community.controller;

import life.haiming.community.dto.CommentDTO;
import life.haiming.community.mapper.CommentMapper;
import life.haiming.community.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CommentController {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    //JSON网络传输的数据结构，不同语言约定俗成的一种数据结构，实现前后端交互
    //支持数组形式/Object格式/
    @ResponseBody
    // 加上consumes = MediaType.APPLICATION_JSON_VALUE，就不再报
    // org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'text/plain;charset=UTF-8' not supported
    @RequestMapping(value = "/comment", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    //可以将传递过来的JSON自动地将Key与Value赋值到commentDTO
    public Object post(@RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator((long) 1);
        comment.setLikeCount((long) 0);
        commentMapper.insert(comment);
        return null;
    }
}
