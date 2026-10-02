package com.example.kurirapp.model

data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val company: CompanyInfo
)
