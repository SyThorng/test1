package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.InvitePlayer;
import com.kshrd.soccer_date.model.request.InvitePlayerRequest;
import com.kshrd.soccer_date.repository.InvitePlayerRepository;
import com.kshrd.soccer_date.repository.RequestTeamRepository;
import com.kshrd.soccer_date.service.InvitePlayerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitePlayerServiceImp implements InvitePlayerService {
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private final RequestTeamRepository teamRepository;
    private final InvitePlayerRepository invitePlayerRepository;

    public InvitePlayerServiceImp(RequestTeamRepository teamRepository, InvitePlayerRepository invitePlayerRepository) {
        this.teamRepository = teamRepository;
        this.invitePlayerRepository = invitePlayerRepository;
    }

    @Override
    public InvitePlayer invitePlayer(InvitePlayerRequest request, Integer userId) {
        Integer player_id = invitePlayerRepository.getPlayerIdByEmail(request.getEmail());
        Integer player = teamRepository.joinedPlay(request.getTeam_id(), player_id);
        Boolean teamOwner = teamRepository.teamOwner(userId, request.getTeam_id());
        if (player_id == null) {
            throw new NotOwnerException("Player is not found", "Not Found!");
        } else if (teamOwner == null || !teamOwner) {
            throw new NotOwnerException("Not Team Owner", "You can not invite player!");
        } else if (player != null) {
            throw new InvalidFieldException("This player has been joined this team already!", "Already Joined!");
        }
        return invitePlayerRepository.invitePlayer(request.getTeam_id(),player_id,userId);
}

    @Override
    public List<InvitePlayer> getInviterByCurrentId(Integer id) {
        return invitePlayerRepository.getInviterByCurrentUser(id);
    }

    @Override
    public InvitePlayer acceptTeamRequest(Integer status, Integer id) {
        if (status == 1) {
            invitePlayerRepository.addPlayerToTeam(id);
            return invitePlayerRepository.acceptTeamInvite(ACCEPT, id);
        }
        return invitePlayerRepository.acceptTeamInvite(REJECT, id);
    }

}
