package life.haiming.community.dto;

import lombok.Data;

/**
 * 这是评论时，传递到数据库的CommentDTO
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
