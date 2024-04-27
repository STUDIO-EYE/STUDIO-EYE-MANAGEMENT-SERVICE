package com.mju.management.domain.post.infrastructure;

import com.mju.management.domain.project.infrastructure.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mju.management.domain.post.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryAndProject(Category getCategory, Project project);

    List<Post> findByCategoryAndProject(Category getCategory, Project project, Pageable pageable);
    List<Post> findByProject(Project project);

    List<Post> findByWriterIdAndProject(Long writerId, Project project);

}
