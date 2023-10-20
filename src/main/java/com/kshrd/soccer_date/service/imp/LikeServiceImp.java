package com.kshrd.soccer_date.service.imp;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.LikeException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.request.LikeRequest;
import com.kshrd.soccer_date.repository.LikeRepository;
import com.kshrd.soccer_date.service.LikeService;
import org.springframework.stereotype.Service;
@Service
public class LikeServiceImp implements LikeService {
    private final LikeRepository likeRepository;
    public LikeServiceImp(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    @Override
    public Like addLike(LikeRequest request, Integer user_id) {
        Like liked=likeRepository.likedPost(request.getPost_id(),user_id);
        if(liked!=null){
            throw new LikeException("Already Like","This you are already liked!!");
        }
        return likeRepository.addLike(request,user_id);
    }

    @Override
    public void deletePost(Integer id,Integer user_id) {
        Like like=likeRepository.searchLike(id);
        Integer liked=likeRepository.deleteLike(id,user_id);
        if(like==null){
            throw new FieldNotFoundException("Like not found!!","This like is not found!");
        }else if(liked!=1){
          throw new NotOwnerException("You are not owner this like!","Not owner !");
        }
    }

}
