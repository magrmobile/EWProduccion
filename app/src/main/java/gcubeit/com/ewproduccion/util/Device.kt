package gcubeit.com.ewproduccion.util

import android.os.Build

object Device {
    val serialNumber: String?
        get() {
            var serialNumber: String?
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)

                serialNumber = get.invoke(c, "gsm.sn1") as String
                if (serialNumber == "")
                    serialNumber = get.invoke(c, "ril.serialnumber") as String
                if (serialNumber == "")
                    serialNumber = get.invoke(c, "ro.serialno") as String
                if (serialNumber == "")
                    serialNumber = get.invoke(c, "sys.serialnumber") as String
                if (serialNumber == "")
                    serialNumber = Build.SERIAL

                if (serialNumber == Build.UNKNOWN) serialNumber = null
            } catch (e: Exception) {
                e.printStackTrace()
                serialNumber = null
            }
            return serialNumber
        }
}