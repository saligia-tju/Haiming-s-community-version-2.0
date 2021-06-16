package life.haiming.community.mapper;

import life.haiming.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface  UserMapper {
    @Insert("INSERT INTO user (name, account_id, token, gmt_create, gmt_modified,avatar_url) VALUES (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token} ")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id} ")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    //对于基本类型的object，需要用@Param进行标识
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name},token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where id = #{id}")
    //对于对象型object，SpringBoot会自动寻找user中的get方法，来获取 #{xxx}中的对应的值
    void update(User user);
}
