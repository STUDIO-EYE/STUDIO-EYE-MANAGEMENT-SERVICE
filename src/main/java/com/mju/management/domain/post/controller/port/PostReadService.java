package com.mju.management.domain.post.controller.port;

import com.mju.management.domain.post.controller.response.PostResponse;

import java.util.List;

public interface PostReadService {

    List<PostResponse> readAll(Long projectId, Long userId, String category);

    List<PostResponse> readThree(Long projectId, Long userId, String category);

}
