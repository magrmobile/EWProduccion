package gcubeit.com.ewproduccion.model

data class Color (val id: Int, val name: String, val hex_code: String) {
    override fun toString(): String {
        return name
    }
}