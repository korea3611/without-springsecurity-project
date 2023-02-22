package com.example.withoutspringsecurityproject.config

import com.example.withoutspringsecurityproject.dto.UserDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import javax.naming.AuthenticationException

@Component
class LoginUserArgumentResolver(
    private val objectMapper: ObjectMapper
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        val jwtToken = exchange.request.headers.getFirst("Authorization")?.split(" ")?.get(1)

        //todo 아래에서 검증하고...

        if (!jwtToken.isNullOrEmpty()) {
            //token이 있다면 payload로 부터 정보를 가져오기 위해 jwt의 payload만 따로 추출
            val payload = jwtToken.split(".")[1]

            //todo payload를 인증하고...

            val claims = objectMapper.readValue(String(Base64Utils.decodeFromString(payload)), Map::class.java)

            val email = claims["email"]?.toString()
            val id = claims["id"]?.toString()

            return UserDto(
                email = email!!,
                id = id!!
            ).toMono()
        } else {
            throw AuthenticationException("JWT 토큰 없거나 문제있음")
        }
    }

}