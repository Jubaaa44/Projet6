package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.openclassrooms.mddapi.model.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
    List<Post> findBySubjectId(Long subjectId);
    
    @Query("SELECT p FROM Post p WHERE p.subject IN (SELECT s FROM User u JOIN u.subscriptions s WHERE u.id = :userId) ORDER BY p.date DESC")
    List<Post> findFeedByUserId(@Param("userId") Long userId);
}