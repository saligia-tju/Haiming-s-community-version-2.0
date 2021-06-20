package life.haiming.community.service;

import life.haiming.community.enums.CommentTypeEnum;
import life.haiming.community.exception.CustomizeErrorCode;
import life.haiming.community.exception.CustomizeException;
import life.haiming.community.mapper.CommentMapper;
import life.haiming.community.mapper.QuestionExtMapper;
import life.haiming.community.mapper.QuestionMapper;
import life.haiming.community.model.Comment;
import life.haiming.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    
    @Autowired(required = false)
    private CommentMapper commentMapper;
    
    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null ||  comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }else {
                commentMapper.insert(comment);
            }
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }

    }
}
