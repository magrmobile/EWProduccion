package gcubeit.com.ewproduccion.model

data class Machine (val id: Int, val machine_name: String, val warehouse: String) {
    override fun toString(): String {
        return machine_name
    }
}