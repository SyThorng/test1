package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.NotOwnerException;

import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.Post;
import com.kshrd.soccer_date.model.request.PostRequest;
import com.kshrd.soccer_date.repository.PostRepository;
import com.kshrd.soccer_date.service.PostService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImp implements PostService {
    private final PostRepository repository;
    @Value("${image.url}")
    private String imageUrl;
    public PostServiceImp(PostRepository repository) {
        this.repository = repository;
    }
    @Override
    public Post addNewPost(PostRequest request,Integer user_id) {
        if (request.getImage()==null){
            request.setImage(" ");
        }

        Post post=repository.addNewPost(request,user_id);
        Integer like_id=repository.getLikeId(post.getId(),user_id);
        post.setLike_id(like_id);
        return post;
    }

    @Override
    public List<Post> getAllPosts(Integer pageNo,Integer pageSize,Integer user_id) {
        pageNo=(pageNo-1)*pageSize;
        List<Post> posts=repository.getAllPost(pageNo,pageSize);
        for (Post post:posts){
            Integer like_id=repository.getLikeId(post.getId(),user_id);
            post.setLike_id(like_id);
        }
        return posts;
    }

    @Override
    public Post editPost(PostRequest request, Integer postId, Integer user_id) {
        return repository.editPost(request, postId, user_id);
    }

    @Override
    public Post getPostById(Integer id) {
        Post post=repository.getPostById(id);
        if (post==null){
            throw new FieldNotFoundException("Post Not Found!","This Post id "+id+" cannot found!");
        }
        return repository.getPostById(id);
    }

    @Override
    public void deletePost(Integer postId, Integer userId) {
        Integer delete = repository.deletePost(postId, userId);
        if (delete != 1) {
            throw new NotOwnerException("This not your post owner!","Not Post owner");
        }
    }

}
