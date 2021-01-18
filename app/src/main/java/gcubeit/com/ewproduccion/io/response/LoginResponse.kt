package gcubeit.com.ewproduccion.io.response

import gcubeit.com.ewproduccion.model.User

data class LoginResponse (
    val success: Boolean,
    val user: User,
    val jwt: String,
    val lastLoginDate: String,
    val lastLoginTime: String
)