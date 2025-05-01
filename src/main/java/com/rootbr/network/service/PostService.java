package com.rootbr.network.service;

import com.rootbr.network.model.Post;
import com.rootbr.network.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
  private final PostRepository postRepository;

  @Autowired
  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public String createPost(Post post) {
    post.setId(UUID.randomUUID().toString());
    return postRepository.createPost(post);
  }

  @CacheEvict(value = "posts", key = "#post.id")
  public void updatePost(Post post) {
    postRepository.updatePost(post);
  }

  @CacheEvict(value = "posts", key = "#id")
  public void deletePost(String id) {
    postRepository.deletePost(id);
  }

  @Cacheable(value = "posts", key = "#id")
  public Optional<Post> getPostById(String id) {
    return postRepository.getPostById(id);
  }

  public List<Post> getFeed(String userId, int offset, int limit) {
    return postRepository.getFeed(userId, offset, limit);
  }
}
