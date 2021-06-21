package life.haiming.community.service;

import life.haiming.community.dto.CommentDTO;
import life.haiming.community.enums.CommentTypeEnum;
import life.haiming.community.exception.CustomizeErrorCode;
import life.haiming.community.exception.CustomizeException;
import life.haiming.community.mapper.CommentMapper;
import life.haiming.community.mapper.QuestionExtMapper;
import life.haiming.community.mapper.QuestionMapper;
import life.haiming.community.mapper.UserMapper;
import life.haiming.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private UserMapper userMapper;


    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            } else {
                commentMapper.insert(comment);
            }
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            //下面三行需要同时成功或同时失败,因此在insert()方法前加上@Transactional注解
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);


        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // java8语法，如果评论人有重复，只得到去重后的所有评论人的id
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //将去重后的commentators的SET，转为List<Long>
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        // 获取评论人，并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        // 默认SQL查询返回的就是List<User>类型
        List<User> users = userMapper.selectByExample(userExample);
/** 暴力算法
 *   for (Comment comment : comments) {
 for (User user : users) {

 }
 }*/

        // 为得到简化的时间复杂度，需要将users转为map
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));//

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
