package com.mju.management.domain.post.infrastructure;

import com.mju.management.domain.post.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findByPostId(Long postId);
}
