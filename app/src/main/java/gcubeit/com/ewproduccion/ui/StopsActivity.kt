package gcubeit.com.ewproduccion.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import gcubeit.com.ewproduccion.R
import gcubeit.com.ewproduccion.io.ApiService
import gcubeit.com.ewproduccion.model.Stop
import gcubeit.com.ewproduccion.util.PreferenceHelper
import gcubeit.com.ewproduccion.util.PreferenceHelper.get
import gcubeit.com.ewproduccion.util.toast
import kotlinx.android.synthetic.main.activity_stops.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StopsActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val stopAdapter = StopAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)

        loadStops()

        rvStops.layoutManager = LinearLayoutManager(this)
        rvStops.adapter = stopAdapter
    }

    private fun loadStops() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getStops("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Stop>> {
            override fun onFailure(call: Call<ArrayList<Stop>>, t: Throwable) {
                toast(t.localizedMessage)
            }
            override fun onResponse(call: Call<ArrayList<Stop>>, response: Response<ArrayList<Stop>>) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        stopAdapter.stops = it
                        stopAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}