package com.example.withoutspringsecurityproject.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import javax.naming.AuthenticationException

@Component
class AuthzWebHandlerAdapter : RequestMappingHandlerAdapter() {

    override fun supports(handler: Any): Boolean {
        val bean = (handler as HandlerMethod).bean
        val isSupported = bean.javaClass.declaredAnnotations.any {
            it.annotationClass.java == LoginUser::class.java
        }
        return isSupported
    }

    override fun handle(exchange: ServerWebExchange, handler: Any): Mono<HandlerResult> {

        try {
            val jwtToken = exchange.request.headers.getFirst("Authorization")?.split(" ")?.get(1)
            if (!jwtToken.isNullOrEmpty()) {

                //todo 추가 인증이 필요하면 추가한다.

            } else {
               throw AuthenticationException("인증필요")
            }
        } catch (e: Exception) {
            throw AuthenticationException("인증필요")
        }
        return super.handle(exchange, handler)
    }

}