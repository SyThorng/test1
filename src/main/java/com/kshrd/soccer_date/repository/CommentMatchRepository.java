package com.kshrd.soccer_date.repository;


import com.kshrd.soccer_date.model.CommentMatch;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.CommentMatchRequest;
import com.kshrd.soccer_date.model.request.CommentMatchRequestUpdate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMatchRepository {


//    @Select("""
//            Select * from app_user where id=#{userId}
//            """)
//    String getNameUserById(Integer userId);



    @Select("""
            SELECT * FROM match_comment
            """)
    @Results(id = "comment", value = {
            @Result(property = "matchId", column = "match_id"),
            @Result(property = "commentId", column = "id"),
    })
    List<CommentMatch> getALlCommentMatch();

    @Select("""
            INSERT INTO match_comment(text,match_id,user_id,is_public, parent_id)
            VALUES (#{comment.text},#{comment.matchId},#{userId},#{is_public},#{comment.parentId})
            returning *
            """)
    @ResultMap("comment")
    CommentMatch insertComment(@Param("comment") CommentMatchRequest commentMatchRequest,Boolean is_public,Integer userId);


    @Select("""
            SELECT team_id from team_member where id=#{teamMemberId}
            """)
    Integer getTeamIdByTeamMemberId(Integer teamMemberId);

    @Select("""
            SELECT player_id from team_member where id=#{teamMemberId}
            """)
    Integer getUserIdByTeamMemberId(Integer teamMemberId);

    @Select("""
            SELECT team_member_id FROM match_comment where id=#{commentId}
            """)
    Integer getTeamMemberIdByCommentId(Integer commentId);

    @Select("""
            Select parent_id from match_comment where id=#{commentId}
            """)
    Integer getParentIdByCommentId(Integer commentId);

    @Select("""
            SELECT * FROM match_comment where id=#{commentId}
            """)
    @ResultMap("comment")
    CommentMatch searchCommentMatchById(Integer commentId);

    @Select("""
            update match_comment set 
                                text=#{com.text}, 
                                parent_id=#{com.parentId}
                                where id =#{commentId}
                                returning *
            """)
    @ResultMap("comment")
    CommentMatch updateCommentById(
            @Param("com") CommentMatchRequestUpdate commentMatchRequestUpdate, Integer userId, Integer commentId);


    @Select("""
             SELECT * from match_comment where match_id =#{matchId} and is_public=true
            """)
    @ResultMap("comment")
    List<CommentMatch> searchCommentByMatchId(Integer matchId);

    @Select("""
            DELETE FROM match_comment where id =#{commentId} returning *
            """)
    @ResultMap("comment")
    CommentMatch deleteCommentMatchById(Integer commentId);

    @Select("""
            Select * from match_comment where match_id=#{matchId} and is_public=false
            """)
    @ResultMap("comment")
    List<CommentMatch> getAllCommentPrivateByMatchId(Integer matchId);

    @Select("""
            SELECT user_id from match_comment where parent_id=#{parentId}
            """)
    Integer getUserIdByParentId(Integer parentId);

    @Select("""
            SELECT user_id from match_comment where id=#{commentId}
            """)
    Integer getUserIdByCommentId(Integer commentId);
}
