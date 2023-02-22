package com.example.withoutspringsecurityproject.controller

import com.example.withoutspringsecurityproject.config.LoginUser
import com.example.withoutspringsecurityproject.dto.UserDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@LoginUser
class TestController {

    @GetMapping("/test1")
    suspend fun test1(@LoginUser userDto: UserDto): UserDto {
        return userDto
    }

    @GetMapping("/test2")
    suspend fun test2(): String {
        return "인증이 필요하지 않음"
    }

}