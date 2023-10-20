package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.RequestTeam;
import com.kshrd.soccer_date.model.request.RequestTeamRequest;
import com.kshrd.soccer_date.repository.RequestTeamRepository;
import com.kshrd.soccer_date.service.RequestTeamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestTeamServiceImp implements RequestTeamService {
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private final RequestTeamRepository repository;
    @Value("${image.url}")
    private String imageUrl;


    public RequestTeamServiceImp(RequestTeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestTeam addNewRequest(RequestTeamRequest request, Integer user_id) {
        Integer player = repository.joinedPlay(request.getTeam_id(), user_id);
        if (player != null) {
            throw new InvalidFieldException("Already Join", "You are already joined this team!");
        }
        return repository.addNewRequest(request, user_id);
    }

    @Override
    public RequestTeam updateStatus(Integer id, Integer userId, Integer status) {
        Integer team_id = repository.teamId(id);
        Boolean is_owner = repository.teamOwner(userId, team_id);
        String stat = repository.accept(id);
        if (is_owner == null || !is_owner) {
            throw new NotOwnerException("You can not accept or reject player!", "Not Team Owner!");
        } else if (status == 1) {
                repository.acceptMember(id);
            return repository.updateStatus(id, ACCEPT);
        }

        return repository.updateStatus(id, REJECT);
    }

    @Override
    public List<RequestTeam> getAllPlayerRequest(Integer userId,Integer team_id) {
        List<RequestTeam>teams= repository.getPlayerRequest(userId,team_id);
        for(RequestTeam team:teams){
          String img=team.getPlayer().getProfile();
          team.getPlayer().setProfile(imageUrl+img);
        }
        return teams;
    }
}
