package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.ForbiddenException;
import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.mapper.UserMapper;
import com.kshrd.soccer_date.model.CommentMatch;
import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.CommentMatchRequest;
import com.kshrd.soccer_date.model.request.CommentMatchRequestUpdate;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import com.kshrd.soccer_date.repository.CommentMatchRepository;
import com.kshrd.soccer_date.repository.MatchRepository;
import com.kshrd.soccer_date.repository.TeamRepository;
import com.kshrd.soccer_date.repository.UserRepository;
import com.kshrd.soccer_date.service.CommentMatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentMatchServiceImp implements CommentMatchService {

    private final CommentMatchRepository commentMatchRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Value("${image.url}")
    private String imageUrl;

    public CommentMatchServiceImp(CommentMatchRepository commentMatchRepository, UserRepository userRepository, MatchRepository matchRepository, TeamRepository teamRepository) {
        this.commentMatchRepository = commentMatchRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }


    @Override
    public CommentMatch insertComment(CommentMatchRequest commentMatchRequest, String email) {
        Match match = matchRepository.searchMatchById(commentMatchRequest.getMatchId());
        if (match == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + commentMatchRequest.getMatchId() + "] is not found...!!"
            );
        }

        if (commentMatchRequest.getParentId() == 0) {
            commentMatchRequest.setParentId(null);
        }
        Integer userId = userRepository.getUserIdByEmail(email);

        CommentMatch commentMatch = commentMatchRepository.insertComment(commentMatchRequest, true, userId);


        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());
        commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

        Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
        Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

        String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
        commentMatch.setMention(userName);

        if (userMentionId == null) {
            commentMatch.setMention(null);
        }

        return commentMatch;
    }

    @Override
    public List<CommentMatch> getALlCommentMatch() {
        List<CommentMatch> commentMatches = commentMatchRepository.getALlCommentMatch();
        for (CommentMatch commentMatch : commentMatches) {
            Integer userId = commentMatchRepository.getUserIdByCommentId(commentMatch.getCommentId());
            UserApp userApp = userRepository.searchUserById(userId);
            userApp.setProfile(imageUrl + userApp.getProfile());

            commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

            Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
            Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

            String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
            commentMatch.setMention(userName);

            if (userMentionId == null) {
                commentMatch.setMention(null);
            }

        }

        return commentMatches;
    }

    @Override
    public List<CommentMatch> searchCommentByMatchId(Integer matchId) {
        Match match = matchRepository.searchMatchById(matchId);
        if (match == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + matchId + "] is not found...!!"
            );
        }

        List<CommentMatch> commentMatches = commentMatchRepository.searchCommentByMatchId(matchId);
        for (CommentMatch commentMatch : commentMatches) {
            Integer userId = commentMatchRepository.getUserIdByCommentId(commentMatch.getCommentId());
            UserApp userApp = userRepository.searchUserById(userId);

            userApp.setProfile(imageUrl + userApp.getProfile());
            commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

            Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
            Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

            String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
            commentMatch.setMention(userName);

            if (userMentionId == null) {
                commentMatch.setMention(null);
            }

        }

        List<CommentMatch> commentMatchPrivate = commentMatchRepository.getAllCommentPrivateByMatchId(matchId);

        if (match.getAwayTeam() != null) {
            for (CommentMatch commentMatch : commentMatchPrivate) {
                Integer userId = commentMatchRepository.getUserIdByCommentId(commentMatch.getCommentId());
                UserApp userApp = userRepository.searchUserById(userId);

                userApp.setProfile(imageUrl + userApp.getProfile());
                commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

                Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
                Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

                String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
                commentMatch.setMention(userName);

                if (userMentionId == null) {
                    commentMatch.setMention(null);
                }
            }
            return commentMatchPrivate;
        }
        return commentMatches;
    }

    @Override
    public CommentMatch searchCommentMatchById(Integer commentId) {
        CommentMatch commentMatch = commentMatchRepository.searchCommentMatchById(commentId);
        if (commentMatch == null) {
            throw new FieldNotFoundException(
                    "Not Found",
                    "Comment id [" + commentId + "] is not found..!!"
            );
        }

        Integer userId = commentMatchRepository.getUserIdByCommentId(commentMatch.getCommentId());
        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());

        commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));
        Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
        Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

        String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
        commentMatch.setMention(userName);

        if (userMentionId == null) {
            commentMatch.setMention(null);
        }
        return commentMatch;
    }

    @Override
    public CommentMatch updateCommentMatchById(Integer commentId, CommentMatchRequestUpdate commentMatchRequest, String email) {
//        Match match=matchRepository.searchMatchById(commentMatchRequest.getMatchId());
        Integer userId = userRepository.getUserIdByEmail(email);
//        if (match == null) {
//            throw new FieldNotFoundException(
//                    "Match Not Found",
//                    "Match Id [" + commentMatchRequest.getMatchId() + "] is not found...!!"
//            );
//        }
        if (commentMatchRepository.searchCommentMatchById(commentId) == null) {
            throw new FieldNotFoundException(
                    "Comment Not Found",
                    "Comment match Id [" + commentId + "] is not found...!!"
            );
        }

        if (commentMatchRequest.getParentId() == 0) {
            commentMatchRequest.setParentId(null);
        }

        if (commentMatchRepository.getUserIdByCommentId(commentId) != userId) {
            throw new ForbiddenException(
                    "Comment invalid",
                    "Comment match Id [" + commentId + "] is not yours "
            );
        }

        CommentMatch commentMatch = commentMatchRepository.updateCommentById(commentMatchRequest, userId, commentId);

        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());

        commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

        Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
        Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

        String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
        commentMatch.setMention(userName);

        if (userMentionId == null) {
            commentMatch.setMention(null);
        }

        return commentMatch;
    }

    @Override
    public CommentMatch deleteCommentMatchById(Integer commentId, String email) {
        Integer userId = userRepository.getUserIdByEmail(email);

        if (commentMatchRepository.searchCommentMatchById(commentId) == null) {
            throw new FieldNotFoundException(
                    "Comment Not Found",
                    "Comment match Id [" + commentId + "] is not found...!!"
            );
        }

        if (commentMatchRepository.getUserIdByCommentId(commentId) != userId) {
            throw new InvalidFieldException(
                    "Comment invalid",
                    "Comment match Id [" + commentId + "] is not yours "
            );
        }
        CommentMatch commentMatch = commentMatchRepository.deleteCommentMatchById(commentId);
        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());

        commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

        Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
        Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

        String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
        commentMatch.setMention(userName);

        if (userMentionId == null) {
            commentMatch.setMention(null);
        }

        return commentMatch;
    }

    @Override
    public CommentMatch insertCommentDiscuss(CommentMatchRequest commentMatchRequest, String email) {


        if (matchRepository.searchMatchById(commentMatchRequest.getMatchId()) == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + commentMatchRequest.getMatchId() + "] is not found...!!"
            );
        }

//        if (teamRepository.searchTeamById(commentMatchRequest.getTeamId()) == null) {
//            throw new FieldNotFoundException(
//                    "Team Not Found...!",
//                    "Team Id [" + commentMatchRequest.getTeamId() + "] is not found..!"
//            );
//        }


        if (commentMatchRequest.getParentId() == 0) {
            commentMatchRequest.setParentId(null);
        }
        Integer userId = userRepository.getUserIdByEmail(email);

        Match match = matchRepository.searchMatchById(commentMatchRequest.getMatchId());

        TeamMemberResponse memberIdHomeTeam = teamRepository.searchMemberByTeamId(userId, match.getHomeTeam().getTeamId());
        TeamMemberResponse memberIdAwayTeam = teamRepository.searchMemberByTeamId(userId, match.getAwayTeam().getTeamId());

        if (memberIdAwayTeam == null && memberIdHomeTeam == null) {
            throw new ForbiddenException(
                    "Comment invalid",
                    "You aren't member of both team,so You can't comment this match "
            );
        }

        CommentMatch commentMatch = commentMatchRepository.insertComment(commentMatchRequest, false, userId);

        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());
        commentMatch.setUser(UserMapper.INSTANCE.toUserAppDTO(userApp));

        Integer parentId = commentMatchRepository.getParentIdByCommentId(commentMatch.getCommentId());
        Integer userMentionId = commentMatchRepository.getUserIdByCommentId(parentId);

        String userName = userRepository.getFirstNameByUserId(userMentionId) + "" + userRepository.getLastNameByUserId(userMentionId);
        commentMatch.setMention(userName);

        if (userMentionId == null) {
            commentMatch.setMention(null);
        }
        return commentMatch;
    }
}
