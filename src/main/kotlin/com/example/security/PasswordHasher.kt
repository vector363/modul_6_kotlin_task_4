package com.example.security

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {

    /**
     * Хэширует пароль с использованием BCrypt
     * @param password пароль в открытом виде
     * @return хэшированный пароль
     */
    fun hash(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    /**
     * Проверяет, соответствует ли пароль хэшу
     * @param password пароль в открытом виде
     * @param hash хэш из базы данных
     * @return true если пароль верный
     */
    fun verify(password: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}