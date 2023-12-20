package kr.galaxyhub.sc.auth.application.dto

data class LoginResponse(
    val accessToken: String,
    val nickname: String,
    val imageUrl: String?,
)
