package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.MatchLike;
import com.kshrd.soccer_date.model.request.MatchLikeRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchLikeRepository {
    @Select("""
            insert into match_like(user_id, match_id) values(#{userId},#{like.match_id}) returning *
            """)
    MatchLike addLike(@Param("like") MatchLikeRequest request, Integer userId);
    @Select("""
          select * from match_like where match_id=#{match_id} and user_id=#{userId}
            """)
    MatchLike likedMatch(Integer match_id, Integer userId);
    @Select("""
            select * from post_like where id=#{id}
            """)
    MatchLike searchLike(Integer id);
      @Delete("delete from match_like where id=#{id} and user_id=#{user_id}")
    Integer deleteLike(Integer id,Integer user_id);

      @Select("""
              select id from match_like where match_id=#{matchId} and user_id=#{userId}
              """)
    Integer serchIdLike(Integer matchId, Integer userId);
}
