package gcubeit.com.ewproduccion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gcubeit.com.ewproduccion.R
import gcubeit.com.ewproduccion.io.ApiService
import gcubeit.com.ewproduccion.util.PreferenceHelper
import gcubeit.com.ewproduccion.util.PreferenceHelper.set
import gcubeit.com.ewproduccion.util.PreferenceHelper.get
import gcubeit.com.ewproduccion.util.toast
import kotlinx.android.synthetic.main.activity_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnCreateStop.setOnClickListener {
            val intent = Intent(this, CreateStopActivity::class.java)
            startActivity(intent)
        }

        btnMyStops.setOnClickListener {
            val intent = Intent(this, StopsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()

                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun clearSessionPreference() {
        preferences["jwt"] = ""
    }


}