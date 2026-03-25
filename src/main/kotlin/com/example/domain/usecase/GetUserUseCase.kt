package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: Int): User? {
        return userRepository.findById(userId)
    }
}