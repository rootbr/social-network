package com.rootbr.network.repository.impl;

import com.rootbr.network.model.Post;
import com.rootbr.network.repository.PostRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class PostRepositoryImpl implements PostRepository {
  private final DSLContext dsl;

  @Autowired
  public PostRepositoryImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  @Transactional
  public String createPost(Post post) {
    dsl.insertInto(table("posts"))
        .set(field("id"), post.getId())
        .set(field("text"), post.getText())
        .set(field("author_user_id"), post.getAuthorUserId())
        .execute();

    return post.getId();
  }

  @Override
  @Transactional
  public void updatePost(Post post) {
    dsl.update(table("posts"))
        .set(field("text"), post.getText())
        .where(field("id").eq(post.getId()))
        .execute();
  }

  @Override
  @Transactional
  public void deletePost(String id) {
    dsl.deleteFrom(table("posts"))
        .where(field("id").eq(id))
        .execute();
  }

  @Override
  public Optional<Post> getPostById(String id) {
    Record record = dsl.select()
        .from(table("posts"))
        .where(field("id").eq(id))
        .fetchOne();

    if (record == null) {
      return Optional.empty();
    }

    return Optional.of(mapRecordToPost(record));
  }

  @Override
  public List<Post> getFeed(String userId, int offset, int limit) {
    Result<Record> result = dsl.select(table("posts").asterisk())
        .from(table("posts"))
        .join(table("friends"))
        .on(field("posts.author_user_id").eq(field("friends.friend_id")))
        .where(field("friends.user_id").eq(userId))
        .orderBy(field("posts.created_at").desc())
        .offset(offset)
        .limit(limit)
        .fetch();

    List<Post> posts = new ArrayList<>();
    for (Record record : result) {
      posts.add(mapRecordToPost(record));
    }

    return posts;
  }

  private Post mapRecordToPost(Record record) {
    Post post = new Post();
    post.setId(record.get("id", String.class));
    post.setText(record.get("text", String.class));
    post.setAuthorUserId(record.get("author_user_id", String.class));
    return post;
  }
}
