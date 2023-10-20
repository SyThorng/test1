package com.kshrd.soccer_date.service.imp;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.request.CommentRequest;
import com.kshrd.soccer_date.model.request.UpdateCommentRequest;
import com.kshrd.soccer_date.repository.CommentRepository;
import com.kshrd.soccer_date.repository.PostRepository;
import com.kshrd.soccer_date.service.CommentService;
import com.kshrd.soccer_date.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService service;

    public CommentServiceImp(CommentRepository commentRepository, PostRepository postRepository, PostService service) {
        this.commentRepository = commentRepository;
        this.service = service;
    }

    @Override
    public Comment commentOnPost(CommentRequest comment,Integer user_id) {
        service.getPostById(comment.getPost_id());
        return commentRepository.commentOnPost(comment,user_id);
    }
    @Override
    public Comment editComment(UpdateCommentRequest commentRequest, Integer user_id) {
        Comment comment=commentRepository.editComment(commentRequest,user_id);
        if(comment==null){
            throw new NotOwnerException("You are not owner this post!!","Not Post Owner!");
        }
        return comment;
    }

    @Override
    public void removeComment(Integer id, Integer user_id) {
        Comment comment=commentRepository.searchComment(id);
        Integer deleted=commentRepository.deleteComment(id,user_id);
        if(comment==null){
            throw new FieldNotFoundException("Comment Not Found!","This comment id is not found!");
        }else if(deleted!=1){
            throw new NotOwnerException("This comment is not your owner!","Not Your Owner!");
        }
    }

    @Override
    public List<Comment> getCommentByPostId(Integer post_id) {
        return commentRepository.getComment(post_id);
    }
}
