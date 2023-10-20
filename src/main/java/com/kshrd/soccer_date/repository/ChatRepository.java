package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Chat;
import com.kshrd.soccer_date.model.request.ChatRequest;
import com.kshrd.soccer_date.model.response.EditChatRequest;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatRepository {
    @Select("""
            insert into chat(team_id, user_id, message) values(#{chat.team_id},#{user_id},#{chat.message}) returning *
               """)
    @Results(
            id = "chatId", value = {
            @Result(property = "team_id", column = "team_id"),
            @Result(property = "player_id", column = "user_id"),
            @Result(property = "username", column = "user_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName"))
    }
    )
    Chat addNewChat(@Param("chat") ChatRequest request, Integer user_id);

    @Select("""
            select id from team_member where player_id=#{user_id} and team_id=#{team_id }
            """)
    Integer member(Integer user_id, Integer team_id);

    @Select("""
            select c.id,c.team_id,c.user_id,c.message,c.create_at from chat c inner join team_member tm on c.team_id = tm.team_id where player_id=#{user_id} and tm.team_id=#{team_id}
               """)
    @ResultMap("chatId")
    List<Chat> getAllChats(Integer team_id, Integer user_id);

    @Select("""
            update chat set message=#{chat.message} where id=#{id} and user_id=#{user_id} returning *
            """)
    @ResultMap("chatId")
    Chat editChat(@Param("chat") EditChatRequest request, Integer id, Integer user_id);

    @Delete("""
            delete from chat where id=#{id} and user_id=#{user_id}
            """)
    Integer deleteChat(Integer id,Integer user_id);
}
