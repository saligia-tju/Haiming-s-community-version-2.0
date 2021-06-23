package life.haiming.community.mapper;

import life.haiming.community.model.Comment;
import life.haiming.community.model.CommentExample;
import life.haiming.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}