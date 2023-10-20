package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.request.LikeRequest;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LikeRepository {
    @Select("""
            insert into post_like(user_id, post_id) VALUES(#{userId},#{like.post_id}) returning *
            """)
    @Result(property = "id",column = "id")
    @Result(property = "user_id",column = "user_id")
    @Result(property = "post_id",column = "post_id")
    Like addLike(@Param("like")LikeRequest request,Integer userId);
    @Select("""
           select * from post_like where post_id=#{post_id} and user_id=#{userId}
            """)
    Like likedPost(Integer post_id,Integer userId);
    @Select("""
            select * from post_like where id=#{id}
            """)
    Like searchLike(Integer id);
    @Delete("delete from post_like where id=#{id} and user_id=#{user_id}")
    Integer deleteLike(Integer id,Integer user_id);
}
