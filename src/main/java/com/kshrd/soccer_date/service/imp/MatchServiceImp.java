package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.ForbiddenException;
import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.MatchRequest;
import com.kshrd.soccer_date.model.request.MatchUpdateScoreRequest;
import com.kshrd.soccer_date.repository.*;
import com.kshrd.soccer_date.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MatchServiceImp implements MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final CommentMatchRepository commentMatchRepository;
    private final CommentMatchService commentMatchService;
   private final MatchLikeService matchLikeService;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }


    public MatchServiceImp(MatchRepository matchRepository, TeamRepository teamRepository, VenueRepository venueRepository, UserRepository userRepository, CommentMatchRepository commentMatchRepository, CommentMatchService commentMatchService, MatchLikeService matchLikeService, AttendeeService attendeeService) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
        this.commentMatchRepository = commentMatchRepository;
        this.commentMatchService = commentMatchService;
        this.matchLikeService = matchLikeService;

    }

    @Value("${image.url}")
    private String imageUrl;

    @Override
    public Match createMatch(MatchRequest matchRequest, String email, Integer teamId) {

        Integer userId = userRepository.getUserIdByEmail(email);


        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

//        if (!matchRequest.getGameType().toLowerCase().equals("special") && !matchRequest.getGameType().toLowerCase().equals("friendly")) {
//            throw new FieldNotFoundException(
//                    "gametype invalid",
//                    " gametype [" + matchRequest.getGameType() + "] don't have  , we have type of game [special] & [friendly]"
//            );
//        }

        if (venueRepository.searchVenueById(matchRequest.getLocation()) == null) {
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    " venue name [" + matchRequest.getLocation() + "] don't have Venue "
            );
        }

        Boolean isOwner = teamRepository.getIsOwnerByUserId(userId, teamId);

        if (isOwner == false) {
            throw new InvalidFieldException(
                    "Invalid Create match",
                    "You Are Not Leader of team ,so you cannot create match"
            );
        }

        Match match = matchRepository.createMatch(matchRequest, email, teamId);
        match.getVenue().setVenueLogo(imageUrl + match.getVenue().getVenueLogo());
        match.getHomeTeam().setLogo(imageUrl + match.getHomeTeam().getLogo());
        if (match.getAwayTeam() != null) {
            match.getAwayTeam().setLogo(imageUrl + match.getAwayTeam().getLogo());
        }
        return match;
    }

    @Override
    public List<Match> getAllMatch(Integer pageSize,String email) {
        List<Match> matchs = matchRepository.getAllMatch(pageSize);
        Integer userId=userRepository.getUserIdByEmail(email);
        if (matchs.size() == 0) {
            throw new FieldNotFoundException(
                    "Data not found",
                    "No Match post...."
            );
        }

        for (Match match : matchs) {
            Integer homeOwnerId=teamRepository.getTeamOwnerIdByTeamId(match.getHomeTeam().getTeamId());
            if(!Objects.equals(homeOwnerId, userId)){
                match.setIs_owner(false);
            }else {
                match.setIs_owner(true);
            }
        }



        for (Match match : matchs) {
            Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
            if (atten!=null){
                match.setAttend(true);
            }else {
                match.setAttend(false);
            }

            match.setLike_id(matchLikeService.searchIdLikeMatch(match.getMatchId()));

            String logo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + logo);

            String logoVenue = venueRepository.getVenueLogoById(match.getVenue().getVenueId());
            match.getVenue().setVenueLogo(imageUrl + logoVenue);

            if (match.getAwayTeam() != null) {
                String logo1 = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());

                match.getAwayTeam().setLogo(imageUrl + logo1);
            }
        }
        return matchs;
    }


    @Override
    public Match deleteMatchById(Integer matchId, String email) {
        if (matchRepository.searchMatchById(matchId) == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + matchId + "] is not found...!!"
            );
        }
        Integer userId = userRepository.getUserIdByEmail(email);
        Integer teamId = matchRepository.getHomeTeamIdByMatchId(matchId);

        if (teamRepository.getTeamOwnerIdByTeamId(teamId) != userId) {
            throw new InvalidFieldException(
                    "Invalid",
                    "you Can't delete this match because isn't yours"
            );
        }
//
        Match match = matchRepository.deleteMatchById(matchId);
        return match;
    }

    @Override
    public Match updateMatchById(MatchRequest matchRequest, String email, Integer matchId) {

        Integer userId = userRepository.getUserIdByEmail(email);
        Integer teamId = matchRepository.getHomeTeamIdByMatchId(matchId);

        if (teamRepository.getTeamOwnerIdByTeamId(teamId) != userId) {
            throw new InvalidFieldException(
                    "Invalid",
                    "you Can't update this match because isn't yours"
            );
        }

        if (!matchRequest.getGameType().toLowerCase().equals("special") && !matchRequest.getGameType().toLowerCase().equals("friendly")) {
            throw new FieldNotFoundException(
                    "gametype invalid",
                    " gametype [" + matchRequest.getGameType() + "] don't have  , we have type of game [special] & [friendly]"
            );
        }

        if (matchRepository.searchMatchById(matchId) == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + matchId + "] is not found...!!"
            );
        }

        if (venueRepository.searchVenueById(matchRequest.getLocation()) == null) {
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    " venue name [" + matchRequest.getLocation() + "] don't have Venue , we have 3 venueId [1] : Roy 7 stadium],[2] : DownTown stadium],[3] : Tsoccer stadium]"
            );
        }

        Match match = matchRepository.updateMatchById(matchRequest, email, matchId);
        match.setLike_id(matchLikeService.searchIdLikeMatch(matchId));
        match.getVenue().setVenueLogo(imageUrl + match.getVenue().getVenueLogo());
        match.getHomeTeam().setLogo(imageUrl + match.getHomeTeam().getLogo());
        if (match.getAwayTeam() != null) {
            match.getAwayTeam().setLogo(imageUrl + match.getAwayTeam().getLogo());
        }

        return match;
    }

    @Override
    public Match searchMatchById(Integer matchId) {
        Match match = matchRepository.searchMatchById(matchId);
        Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
        if (atten!=null){
            match.setAttend(true);
        }else {
            match.setAttend(false);
        }
        match.setLike_id(matchId);
        if (match == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + matchId + "] is not found...!!"
            );
        }

        if (match.getAwayTeam() != null) {
            match.getAwayTeam().setLogo(imageUrl + match.getAwayTeam().getLogo());
        }

        match.getVenue().setVenueLogo(imageUrl + match.getVenue().getVenueLogo());
        match.getHomeTeam().setLogo(imageUrl + match.getHomeTeam().getLogo());

        return match;
    }

    @Override
    public Match updateScoreMatch(MatchUpdateScoreRequest matchUpdateScoreRequest, String email, Integer matchId) {

        Integer HomeTeamId = matchRepository.getHomeTeamIdByMatchId(matchId);

        int userId1 = userRepository.getUserIdByEmail(email);
        Match match1 = searchMatchById(matchId);


        if (matchRepository.searchMatchById(matchId) == null) {
            throw new FieldNotFoundException(
                    "Match Not Found",
                    "Match Id [" + matchId + "] is not found...!!"
            );
        }
        if (match1.getAwayTeam() == null) {
            throw new InvalidFieldException(
                    "Invalid",
                    "can't update score this match because this match don't other team play with"
            );
        }

        Integer AwayTeamId = matchRepository.getawayTeamIdByMatchId(matchId);
        if (teamRepository.getTeamOwnerIdByTeamId(HomeTeamId).equals(userId1) ||
                teamRepository.getTeamOwnerIdByTeamId(AwayTeamId).equals(userId1)
        ) {
        } else {
            throw new ForbiddenException(
                    "Invalid",
                    "you Can't update this match because you aren't leader of both team "
            );
        }

        Match match = matchRepository.updateScoreMatchByMatchId(matchUpdateScoreRequest, matchId);
        match.getVenue().setVenueLogo(imageUrl + match.getVenue().getVenueLogo());
        match.getHomeTeam().setLogo(imageUrl + match.getHomeTeam().getLogo());
        if (match.getAwayTeam() != null) {
            match.getAwayTeam().setLogo(imageUrl + match.getAwayTeam().getLogo());
        }


        return match;
    }

    @Override
    public List<Match> getApprovedTeamByTeamId(Integer team_id) {
        List<Match> matches = matchRepository.getApprovedTeamByTeamId(team_id);
        for (Match match : matches) {
        Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
        if (atten!=null){
            match.setAttend(true);
        }else {
            match.setAttend(false);
        }
            String homeLogo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + homeLogo);
            String away = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());
            match.getAwayTeam().setLogo(imageUrl + away);

        }
        return matches;
    }


    @Override
    public List<Match> searchMatchByGameType(String gametype, Integer teamId) {
        List<Match> matches = matchRepository.searchMatchByGameType(gametype, teamId);
        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        if (matches.size() == 0) {
            throw new FieldNotFoundException(
                    "Data not found",
                    "No Match post...."
            );
        }
//        matches.forEach(match -> match.setComments(null));
        for (Match match : matches) {
            Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
            if (atten!=null){
                match.setAttend(true);
            }else {
                match.setAttend(false);
            }
            String homeLogo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + homeLogo);

            String VenueLogo = venueRepository.getVenueLogoById(match.getVenue().getVenueId());
            match.getVenue().setVenueLogo(imageUrl + VenueLogo);

            if (match.getAwayTeam() != null) {

                String awayLogo = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());
                match.getAwayTeam().setLogo(imageUrl + awayLogo);
            }
        }
        return matches;
    }

    @Override
    public List<Match> searchMatchByTeamId(Integer teamId) {
        List<Match> matches = matchRepository.searchMatchByTeamId(teamId);

        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        if (matches.size() == 0) {
            throw new FieldNotFoundException(
                    "Data not found",
                    "No Match post...."
            );
        }
//        matches.forEach(match -> match.setComments(null));
        for (Match match : matches) {
            Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
            if (atten!=null){
                match.setAttend(true);
            }else {
                match.setAttend(false);
            }
            String homeLogo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + homeLogo);

            String VenueLogo = venueRepository.getVenueLogoById(match.getVenue().getVenueId());
            match.getVenue().setVenueLogo(imageUrl + VenueLogo);

            if (match.getAwayTeam() != null) {
                String awayLogo = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());
                match.getAwayTeam().setLogo(imageUrl + awayLogo);
            }
        }

        return matches;
    }


    @Override
    public List<Match> searchMatchByLocationName(String locationName) {

        if (venueRepository.searchVenueByName(locationName, 3, 0).size() == 0) {
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    " venue name [" + locationName + "] don't have Venue "
            );
        }

        List<Match> matches = matchRepository.searchMatchByLocationName(locationName);
        if (matches.size() == 0) {
            throw new FieldNotFoundException(
                    "Data not found",
                    "don't have match at this location"
            );
        }
//        matches.forEach(match -> match.setComments(null));
        for (Match match : matches) {
            Integer atten= matchRepository.getAttenId(match.getMatchId(),currentId());
            if (atten!=null){
                match.setAttend(true);
            }else {
                match.setAttend(false);
            }
            String homeLogo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + homeLogo);

            String VenueLogo = venueRepository.getVenueLogoById(match.getVenue().getVenueId());
            match.getVenue().setVenueLogo(imageUrl + VenueLogo);

            if (match.getAwayTeam() != null) {

                String awayLogo = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());
                match.getAwayTeam().setLogo(imageUrl + awayLogo);
            }
        }
        return matches;
    }

    @Override
    public List<Match> upcomingMatch(Integer user_id, Integer limit) {
        List<Match> matches = matchRepository.getUpcomingMatchByTeamId(user_id);
        for (Match match : matches) {
            String homeLogo = teamRepository.searchLogoByTeamId(match.getHomeTeam().getTeamId());
            match.getHomeTeam().setLogo(imageUrl + homeLogo);


            if (match.getAwayTeam() != null) {

                String awayLogo = teamRepository.searchLogoByTeamId(match.getAwayTeam().getTeamId());
                match.getAwayTeam().setLogo(imageUrl + awayLogo);
            }

        }
        LocalDateTime today = LocalDateTime.now();
        List<Match> upcoming = matches.stream().filter(match -> match.getTime().compareTo(today) >= 0).limit(limit).collect(Collectors.toList());
        return upcoming;
    }
}
