package gcubeit.com.ewproduccion.model

data class Product (val id: Int, val product_name: String, val stock: String) {
    override fun toString(): String {
        return product_name
    }
}