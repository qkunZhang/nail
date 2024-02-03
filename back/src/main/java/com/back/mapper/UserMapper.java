package com.back.mapper;

import com.back.entity.relationalEntity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    @Select("select * from user_tbl where username = #{name}")
    public UserEntity selectByName(@Param("name") String name);

    @Select("select email from user_tbl where username = #{name}")
    public String selectEmailByName(@Param("name") String name);

    @Insert("insert into user_tbl (user_id,username,password,email) values (#{userId},#{username},#{password},#{email})")
    public Integer insert(@Param("userId") long userId,@Param("username")String username,@Param("password")String password,@Param("email")String email);

    @Select("select * from user_tbl where email = #{email}")
    public UserEntity selectByEmail(@Param("email") String email);

    @Update("update user_tbl set password=#{password} where username = #{username}")
    public Integer updatePassword(String password,String username);


    @Select("select username from user_tbl where user_id = #{userId}")
    public String selectNameById(long userId);
}
