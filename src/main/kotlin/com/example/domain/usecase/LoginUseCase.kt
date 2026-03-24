package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.NobelPrizeRepository
import com.example.security.JwtConfig

class LoginUseCase(
    private val repository: NobelPrizeRepository
) {
    fun execute(username: String, password: String): String? {
        val user = repository.getUserByUsername(username)
        return if (user != null && user.password == password) {
            JwtConfig.generateToken(user.id, user.username)
        } else {
            null
        }
    }

    fun getUser(username: String): User? {
        return repository.getUserByUsername(username)
    }
}