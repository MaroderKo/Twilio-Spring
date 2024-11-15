package com.project.twiliospring.domain.dto

data class ErrorMessageDTO(val message: String) {
    constructor(exception: RuntimeException) : this(exception.message ?: "Internal server error")
}