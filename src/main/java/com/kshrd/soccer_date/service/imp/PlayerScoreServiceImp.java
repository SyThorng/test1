package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.PlayerScore;
import com.kshrd.soccer_date.model.request.PlayerScoreRequest;
import com.kshrd.soccer_date.repository.PlayerScoreRepository;
import com.kshrd.soccer_date.service.PlayerScoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerScoreServiceImp implements PlayerScoreService {
    private final PlayerScoreRepository repository;

    public PlayerScoreServiceImp(PlayerScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public PlayerScore addScore(PlayerScoreRequest request) {
        if (repository.checkPlayerIntTeam(request.getTeam_id(),request.getPlayer_id())==null){
            throw new NotOwnerException("Player is not in this team..!","Not Found!");
        }else {
            repository.addScore(request);
        }
        return repository.getPlayerByPlayerIdAndMatchId(request.getPlayer_id(),request.getMatch_id());
    }

    @Override
    public List<PlayerScore> getScore(Integer match_id,Integer team_id) {
        return repository.getScore(match_id,team_id);
    }
}
