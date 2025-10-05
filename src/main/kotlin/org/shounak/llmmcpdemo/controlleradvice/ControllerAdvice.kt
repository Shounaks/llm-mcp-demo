package org.shounak.llmmcpdemo.controlleradvice

import org.shounak.llmmcpdemo.service.ExternalServiceException
import org.shounak.llmmcpdemo.service.RateLimitException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitException::class)
    fun handleRateLimit(e: RateLimitException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.message)

    @ExceptionHandler(ExternalServiceException::class)
    fun handleExternal(e: ExternalServiceException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.message)

    @ExceptionHandler(Exception::class)
    fun handleGeneric(e: Exception): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
}
