package com.project.twiliospring.domain.dto

data class UserTokensDTO(
    val accessToken: String,
    val refreshToken: String
)
