package com.rootbr.network.controller;

import com.rootbr.network.dto.PostCreateRequest;
import com.rootbr.network.dto.PostCreateResponse;
import com.rootbr.network.dto.PostUpdateRequest;
import com.rootbr.network.model.Post;
import com.rootbr.network.service.AuthService;
import com.rootbr.network.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
  private final PostService postService;
  private final AuthService authService;

  @Autowired
  public PostController(PostService postService, AuthService authService) {
    this.postService = postService;
    this.authService = authService;
  }

  @PostMapping("/create")
  public ResponseEntity<PostCreateResponse> createPost(
      @RequestBody PostCreateRequest request,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Post post = new Post();
    post.setText(request.getText());
    post.setAuthorUserId(userId);

    String postId = postService.createPost(post);

    PostCreateResponse response = new PostCreateResponse();
    response.setId(postId);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updatePost(
      @RequestBody PostUpdateRequest request,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Post> existingPostOpt = postService.getPostById(request.getId());

    if (existingPostOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Post existingPost = existingPostOpt.get();

    if (!existingPost.getAuthorUserId().equals(userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    existingPost.setText(request.getText());
    postService.updatePost(existingPost);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/delete/{id}")
  public ResponseEntity<Void> deletePost(
      @PathVariable String id,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Post> existingPostOpt = postService.getPostById(id);

    if (existingPostOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Post existingPost = existingPostOpt.get();

    if (!existingPost.getAuthorUserId().equals(userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    postService.deletePost(id);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<Post> getPost(@PathVariable String id) {
    Optional<Post> post = postService.getPostById(id);
    return post.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/feed")
  public ResponseEntity<List<Post>> getFeed(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    List<Post> feed = postService.getFeed(userId, offset, limit);
    return ResponseEntity.ok(feed);
  }
}
