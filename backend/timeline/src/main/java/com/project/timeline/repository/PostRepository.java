package com.project.timeline.repository;

import com.project.timeline.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Post save(Post post);

    @Override
    Optional<Post> findById(Integer postId);
}
