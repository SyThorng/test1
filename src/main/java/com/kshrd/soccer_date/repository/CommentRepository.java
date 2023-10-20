package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.request.CommentRequest;
import com.kshrd.soccer_date.model.request.UpdateCommentRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentRepository {
    @Select("""
            select * from comment where post_id=#{post_id}
            """)
    @Results(id = "cmtId", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "post_id", column = "post_id"),
            @Result(property = "user_id", column = "user_id"),
            @Result(property = "username", column = "user_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName")),
            @Result(property = "comment", column = "comment"),
            @Result(property = "profile", column = "user_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.getProfileImageByUserId")),
            @Result(property = "create_at", column = "create_at")
    })
    List<Comment> getComment(Integer post_id);

    @Select("""
            insert into  comment(comment, user_id, post_id) values (#{cmt.comment},#{user_id},#{cmt.post_id}) returning *
              """)
    @ResultMap("cmtId")
    Comment commentOnPost(@Param("cmt") CommentRequest comment, Integer user_id);

    @Select("""
            update comment set comment=#{cmt.comment} where id =#{cmt.comment_id}  and user_id=#{user_id} returning *
             """)
    @ResultMap("cmtId")
    Comment editComment(@Param("cmt") UpdateCommentRequest cmt, Integer user_id);

    @Select("""
            select * from comment where id=#{id}
            """)
    @ResultMap("cmtId")
    Comment searchComment(Integer id);

    @Delete("""
            delete from comment where id=#{id} and user_id=#{user_id}
            """)
    Integer deleteComment(Integer id, Integer user_id);

}
