package life.haiming.community.mapper;


import life.haiming.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question (title,description,gmt_create,gmt_modified,creator,tag) VALUES (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question>  list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(*) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset}, #{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(*) from question where creator = #{userId}")
    Integer countByUserId(Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Integer id);
    //@Param用法： @Param("xxx") Integer id :将xxx作为key,其值为id，使用#{xxx}即可取到值：id
}