package com.mju.management.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mju.management.domain.project.dto.response.GetProjectUserResponseDto;
import com.mju.management.domain.project.infrastructure.ProjectUser;
import com.mju.management.domain.user.controller.UserFeignClient;
import com.mju.management.domain.user.dto.GetUserResponseDto;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.UserNotFindException;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

//    private final UserFeignClient userFeignClient;

    public GetUserResponseDto getUser(Long userId){
//        try {return userFeignClient.getUser(userId).getBody();}
//        catch (Exception e){return null;}
        return null;
    }

    public String getUsername(Long userId){
//        GetUserResponseDto getUserResponseDto = null;
//        try{
//            getUserResponseDto = userFeignClient.getUser(userId).getBody();
//        }catch (FeignException.InternalServerError e){
//            e.printStackTrace();
//            return "(내부 서버 오류)";
//        }catch (RetryableException e){
//            e.printStackTrace();
//            return "(응답 시간 초과)";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "(알 수 없음)";
//        }
//        return getUserResponseDto.getName();
        return null;
    }
}
