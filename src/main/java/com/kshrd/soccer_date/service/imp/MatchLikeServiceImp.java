package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.LikeException;
import com.kshrd.soccer_date.model.MatchLike;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.MatchLikeRequest;
import com.kshrd.soccer_date.repository.MatchLikeRepository;
import com.kshrd.soccer_date.service.MatchLikeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MatchLikeServiceImp implements MatchLikeService {
    private final MatchLikeRepository likeRepository;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    public MatchLikeServiceImp(MatchLikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public MatchLike addLikeMatch(MatchLikeRequest request, Integer user_id) {
        MatchLike liked = likeRepository.likedMatch(request.getMatch_id(), user_id);
        if (liked != null) {
            throw new LikeException("Already Like!", "This match you have been liked already!!");
        }
        return likeRepository.addLike(request, user_id);
    }

    @Override
    public void deleteLikeMatch(Integer id, Integer user_id) {
         likeRepository.deleteLike(id,user_id);
       }

    @Override
    public Integer searchIdLikeMatch(Integer match_id) {
        return likeRepository.serchIdLike(match_id,currentId());
    }
}
