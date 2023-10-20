package com.kshrd.soccer_date.service.imp;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.RequestMatch;
import com.kshrd.soccer_date.model.request.RequestMatchRequest;
import com.kshrd.soccer_date.repository.RequestMatchRepository;
import com.kshrd.soccer_date.service.RequestMatchService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RequestMatchServiceImp implements RequestMatchService {
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private final RequestMatchRepository repository;

    public RequestMatchServiceImp(RequestMatchRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestMatch addNewRequest(RequestMatchRequest request,Integer user_id) {
        Boolean is_owner=repository.isOwner(request.getTeam_id(),user_id );
        if (!is_owner){
            throw new NotOwnerException("Not team owner!","You can not request match!!");
        }
        return repository.addRequestMatch(request);
    }
    @Override
    public List<RequestMatch> getAllRequestMatchByUserId(Integer id,Integer home_id) {
        return repository.getAllRequestMatchByCurrentId(id,home_id);
    }

    @Override
    public RequestMatch updateMatchRequest(Integer status,Integer match_id,Integer id) {
        RequestMatch requestMatch=repository.searchRequestById(id);
        if(requestMatch==null){
            throw new FieldNotFoundException("Not Found!","Request not found!");
        }
        Integer team_id=repository.AwayId(id);
        if(status==1){
           repository.updateStatusMatch(ACCEPT,id);
           repository.updateMatch(team_id,match_id);
           repository.updateStatusRejectMatch(match_id);
        }else {
            repository.updateStatusMatch(REJECT,id);
        }
        return repository.searchRequestById(id);
    }
}
