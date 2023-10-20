package com.kshrd.soccer_date.service;
import com.kshrd.soccer_date.model.Post;
import com.kshrd.soccer_date.model.request.PostRequest;
import java.util.List;

public interface PostService {
    Post addNewPost(PostRequest request,Integer user_id);

    List<Post> getAllPosts(Integer pageNo,Integer pageSize,Integer user_id);

    Post editPost(PostRequest request, Integer postId, Integer user_id);

    Post getPostById(Integer id);

    void deletePost(Integer postId, Integer userId);
}
