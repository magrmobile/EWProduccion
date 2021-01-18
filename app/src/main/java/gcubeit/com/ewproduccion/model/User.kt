package gcubeit.com.ewproduccion.model

data class User (
    val id: Int,
    val name: String,
    val username: String,
    val active_user: String,
    val role: String,
    val supervisor_id: Int
)