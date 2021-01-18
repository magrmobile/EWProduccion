package gcubeit.com.ewproduccion.model

data class Code(val id: Int, val code: Int, val description: String, val type: String) {
    override fun toString(): String {
        return "$code - $description"
    }
}