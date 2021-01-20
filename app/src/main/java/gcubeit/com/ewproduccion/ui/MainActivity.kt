package gcubeit.com.ewproduccion.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import gcubeit.com.ewproduccion.R
import gcubeit.com.ewproduccion.io.ApiService
import gcubeit.com.ewproduccion.io.response.LoginResponse
import gcubeit.com.ewproduccion.util.PreferenceHelper
import gcubeit.com.ewproduccion.util.PreferenceHelper.get
import gcubeit.com.ewproduccion.util.PreferenceHelper.set
import gcubeit.com.ewproduccion.util.toast
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val snackBar by lazy {
        Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["jwt", ""].contains("."))
            goToMenuActivity()

        btnLogin.setOnClickListener {
            // validates
            performLogin()
        }
    }

    private fun performLogin() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if(username.trim().isEmpty() || password.trim().isEmpty()) {
            toast(getString(R.string.error_empty_credentials))
            return
        }

        val call = apiService.postLogin(username, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast(getString(R.string.error_login_response))
                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(loginResponse.jwt)
                        createLastStopDateTimeStartPreference(loginResponse.lastStopDateTimeStart)
                        toast(getString(R.string.welcome_name, loginResponse.user.name))
                        //toast(loginResponse.lastLoginTime)
                        goToMenuActivity()
                    } else {
                        toast(getString(R.string.error_invalid_credentials))
                    }
                } else {
                    toast(getString(R.string.error_login_response))
                }
            }
        })
    }

    private fun createSessionPreference(jwt: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun createLastStopDateTimeStartPreference(datetime: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["lastStopDateTimeStart"] = datetime
    }

    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if(snackBar.isShown)
            super.onBackPressed()
        else
            snackBar.show()
    }
}