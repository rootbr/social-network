package com.rootbr.network.repository;

import com.rootbr.network.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
  String createPost(Post post);
  void updatePost(Post post);
  void deletePost(String id);
  Optional<Post> getPostById(String id);
  List<Post> getFeed(String userId, int offset, int limit);
}
