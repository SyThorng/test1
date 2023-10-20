package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.Post;
import com.kshrd.soccer_date.model.request.PostRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostRepository {
    @Select("""
            select CONCAT(first_name,' ',last_name) from app_user where id=#{id}
            """)
    String userName(Integer id);
    @Select("""
            select count(user_id) from post_like where post_id=#{post_id}
            """)
   Integer getCountLikeById(Integer post_id);
    @Select("""
            select profile_image from app_user where id=#{id}
            """)
    String getProfileImageByUserId(Integer id);

    @Select("""
            select * from post order by create_at desc limit #{pageSize} offset #{pageNo} 
              """)
    @Results(id = "postId", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "user_id", column = "user_id"),
            @Result(property = "username", column = "user_id",many = @Many(select = "userName")),
            @Result(property = "profile_image", column = "user_id",one = @One(select = "getProfileImageByUserId")),
            @Result(property = "caption", column = "caption"),
            @Result(property = "url", column = "image"),
            @Result(property = "likes", column = "id", many = @Many(select = "getCountLikeById")),
    })
    List<Post> getAllPost(Integer pageNo, Integer pageSize);

    @Select("""
            insert into post(caption, image, user_id) values (#{post.caption},#{post.image},#{user_id})
            returning *
            """)
    @ResultMap("postId")
    Post addNewPost(@Param("post") PostRequest postRequest, Integer user_id);

    @Select("""
            select id from post_like where post_id=#{post_id} and user_id=#{user_id}
            """)
    Integer getLikeId(Integer post_id,Integer user_id);
    @Select("""
            update post set caption=#{post.caption} where id=#{postId} and user_id =#{userId} returning *
            """)
    @ResultMap("postId")
    Post editPost(@Param("post") PostRequest request, Integer postId, Integer userId);

    @Delete("""
            delete from post where id=#{postId} and user_id=#{userId}
            """)
    Integer deletePost(Integer postId, Integer userId);

    @Select("""
            select * from post where id=#{id}
            """)
    @ResultMap("postId")
    Post getPostById(Integer id);
}
