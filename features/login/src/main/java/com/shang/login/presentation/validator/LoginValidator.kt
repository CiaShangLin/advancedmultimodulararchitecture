package com.shang.login.presentation.validator

import com.shang.login.presentation.error.LoginError


const val USERNAME_LENGTH = 5
const val PASSWORD_MAX_LENGTH = 11
const val PASSWORD_MIN_LENGTH = 6

object LoginValidator {

    fun userNameError(username: String): LoginError {
        return when {
            username.isEmpty() -> LoginError.NoEntry
            !isValidUserNameLength(username) -> LoginError.InCorrectUsernameLength
            !username.isAlphanumeric() -> LoginError.InCorrectUserName
            else -> LoginError.NoError
        }
    }

    fun passwordError(password: String): LoginError {
        return when {
            password.isEmpty() -> LoginError.NoEntry
            !isValidPasswordLength(password) -> LoginError.InCorrectPasswordLength
            !password.isAlphaNumericWithSpecialCharacters() -> LoginError.InCorrectPassword
            else -> LoginError.NoError
        }
    }


    private fun String.isAlphaNumericWithSpecialCharacters(): Boolean {
        val containsLowerCase = any { it.isLowerCase() }
        val containsUpperCase = any { it.isUpperCase() }
        val containsSpecialCharacters = any { !it.isLetterOrDigit() }
        val containsDigits = any { it.isDigit() }
        return containsDigits && containsLowerCase && containsUpperCase && containsSpecialCharacters
    }

    private fun isValidPasswordLength(password: String) =
        password.count() in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH

    private fun isValidUserNameLength(username: String) =
        username.count() > USERNAME_LENGTH

    private fun String.isAlphanumeric() = matches(Regex("^[a-zA-Z0-9]*$"))
}