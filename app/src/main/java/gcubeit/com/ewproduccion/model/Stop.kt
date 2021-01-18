package gcubeit.com.ewproduccion.model

import com.google.gson.annotations.SerializedName

/*
"id": 2,
        "meters": 100,
        "comment": null,
        "stop_date_start": "2020-12-22",
        "stop_date_end": "2020-12-22",
        "stop_time_start_12": "2:45 PM",
        "stop_time_end_12": "2:47 PM",
        "code": {
            "id": 1,
            "description": "Máquina trabajando normal",
            "type": "Programado"
        },
        "machine": {
            "id": 3,
            "machine_name": "Cab Disco 400 #2"
        },
        "product": {
            "id": 7,
            "product_name": "RELLENO TGP 2X10 AWG"
        },
        "color": {
            "id": 6,
            "name": "V",
            "hex_code": "#00FF00"
        }
 */
data class Stop (
    val id: Int,
    val meters: Int,
    val comment: String,
    @SerializedName("stop_date_start") val stopDateStart: String,
    @SerializedName("stop_time_start_12") val stopTimeStart: String,
    @SerializedName("stop_date_end") val stopDateEnd: String,
    @SerializedName("stop_time_end_12") val stopTimeEnd: String,

    val code: Code,
    val machine: Machine,
    val product: Product,
    val color: Color
)