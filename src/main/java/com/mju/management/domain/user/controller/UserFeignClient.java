package com.mju.management.domain.user.controller;

import com.mju.management.domain.user.dto.GetUserResponseDto;
import com.mju.management.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignConfig.class)
public interface UserFeignClient {
    @GetMapping("/response_userById/{userId}")
    ResponseEntity<GetUserResponseDto> getUser(@PathVariable("userId") Long userId);
}
