package gcubeit.com.ewproduccion.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import gcubeit.com.ewproduccion.R
import gcubeit.com.ewproduccion.io.ApiService
import gcubeit.com.ewproduccion.io.response.SimpleResponse
import gcubeit.com.ewproduccion.model.Code
import gcubeit.com.ewproduccion.model.Color
import gcubeit.com.ewproduccion.model.Machine
import gcubeit.com.ewproduccion.model.Product
import gcubeit.com.ewproduccion.util.Device
import gcubeit.com.ewproduccion.util.PreferenceHelper
import gcubeit.com.ewproduccion.util.PreferenceHelper.get
import gcubeit.com.ewproduccion.util.snackbar
import gcubeit.com.ewproduccion.util.toast
import kotlinx.android.synthetic.main.activity_create_stop.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CreateStopActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stop)

        //setSupportActionBar(findViewById(R.id.create_stop_toolbar))

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnNext.setOnClickListener {
            when (spinnerCode.selectedItem) {
                null -> {
                    snackbar(createStopLinearLayout, getString(R.string.require_code_text))
                }
                else -> {
                    val code = spinnerCode.selectedItem as Code

                    when(code.code) {
                        0 -> {
                            when {
                                spinnerMachine.selectedItem == null -> {
                                    snackbar(
                                        createStopLinearLayout,
                                        getString(R.string.require_machine_text)
                                    )
                                }
                                spinnerProduct.selectedItem == null -> {
                                    snackbar(
                                        createStopLinearLayout,
                                        getString(R.string.require_product_text)
                                    )
                                }
                                spinnerColor.selectedItem == null -> {
                                    snackbar(
                                        createStopLinearLayout,
                                        getString(R.string.require_color_text)
                                    )
                                }
                                else -> {
                                    showStopDataToConfirm()
                                    cvStep1.visibility = View.GONE
                                    cvStep2.visibility = View.VISIBLE
                                }
                            }
                        }
                        1, 2, 3, 4, 5 -> {
                            if (spinnerMachine.selectedItem == null) {
                                snackbar(
                                    createStopLinearLayout,
                                    getString(R.string.require_machine_text)
                                )
                            } else {
                                showStopDataToConfirm()
                                cvStep1.visibility = View.GONE
                                cvStep2.visibility = View.VISIBLE
                            }
                        }
                        6, 7, 8, 9, 10, 11 -> {
                            when {
                                spinnerMachine.selectedItem == null -> {
                                    snackbar(
                                        createStopLinearLayout,
                                        getString(R.string.require_machine_text)
                                    )
                                }
                                (etComments.text.isEmpty()) -> {
                                    snackbar(
                                        createStopLinearLayout,
                                        getString(R.string.require_comment_text)
                                    )
                                }
                                else -> {
                                    showStopDataToConfirm()
                                    cvStep1.visibility = View.GONE
                                    cvStep2.visibility = View.VISIBLE
                                }
                            }
                        }
                        else -> {
                            if(spinnerMachine.selectedItem == null) {
                                snackbar(
                                    createStopLinearLayout,
                                    getString(R.string.require_machine_text)
                                )
                            } else {
                                showStopDataToConfirm()
                                cvStep1.visibility = View.GONE
                                cvStep2.visibility = View.VISIBLE
                            }
                        }
                    }
                    // continue to step 2

                }
            }
        }

        btnConfirmStop.setOnClickListener {
            performStoreStop()
        }

        loadCodes()
        listenCodeChanges()
        loadMachines()
        loadProducts()
        loadColors()
    }

    private fun performStoreStop() {
        //toast("Perform Storage")
        btnConfirmStop.isClickable = false

        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        val codeId: Int
        val codeVal: Int
        val machineId: Int
        val productId: Int?
        val colorId: Int?

        val code = spinnerCode.selectedItem as Code
        codeId = code.id
        codeVal = code.code

        val machine = spinnerMachine.selectedItem as Machine
        machineId = machine.id

        if(codeVal == 0) {
            val product = spinnerProduct.selectedItem as Product
            productId = product.id

            val color = spinnerColor?.selectedItem as Color
            colorId = color.id
        } else {
            productId = null
            colorId = null
        }

        val meters = tvMeters.text.toString().toIntOrNull()?: 0

        val comment = etComments.text.toString()

        val call = apiService.storeStop(
            authHeader, machineId,
            productId, colorId,
            codeId, meters,
            comment
        )

        call.enqueue(object: Callback<SimpleResponse> {
            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                toast(t.localizedMessage)
                btnConfirmStop.isClickable = true
            }
            override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>) {
                if(response.isSuccessful) {
                    toast(getString(R.string.create_stop_success))
                    finish()
                } else {
                    toast(getString(R.string.create_stop_error))
                    btnConfirmStop.isClickable = true
                }
            }
        })
    }

    private fun listenCodeChanges() {
        spinnerCode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val code = adapter?.getItemAtPosition(position) as Code
                etStopType.text = code.type
                when (code.code) {
                    0 -> {
                        tvProduct.visibility = View.VISIBLE
                        spinnerProduct.visibility = View.VISIBLE
                        spinnerProduct.clearSelection()
                        tvColor.visibility = View.VISIBLE
                        spinnerColor.visibility = View.VISIBLE
                        spinnerColor.clearSelection()
                        tvMeters.visibility = View.VISIBLE
                        etMeters.visibility = View.VISIBLE
                    }
                    else -> {
                        tvMachine.visibility = View.VISIBLE
                        spinnerMachine.visibility = View.VISIBLE
                        spinnerMachine.clearSelection()
                        tvProduct.visibility = View.GONE
                        spinnerProduct.visibility = View.GONE
                        spinnerProduct.clearSelection()
                        tvColor.visibility = View.GONE
                        spinnerColor.visibility = View.GONE
                        spinnerColor.clearSelection()
                        tvMeters.visibility = View.GONE
                        etMeters.visibility = View.GONE
                    }

                }
            }
        }
    }

    private fun loadCodes() {
        val call = apiService.getCodes()
        call.enqueue(object : Callback<ArrayList<Code>> {
            override fun onFailure(call: Call<ArrayList<Code>>, t: Throwable) {
                Toast.makeText(
                    this@CreateStopActivity,
                    getString(R.string.error_loading_codes),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Code>>,
                response: Response<ArrayList<Code>>
            ) {
                if (response.isSuccessful) { // 200 ... 300
                    val codes = response.body() as ArrayList<Code>
                    spinnerCode.adapter = ArrayAdapter<Code>(
                        this@CreateStopActivity,
                        android.R.layout.simple_list_item_1,
                        codes
                    )
                }
            }
        })
    }

    private fun loadMachines() {
        val call = apiService.getMachines()
        call.enqueue(object : Callback<ArrayList<Machine>> {
            override fun onFailure(call: Call<ArrayList<Machine>>, t: Throwable) {
                Toast.makeText(
                    this@CreateStopActivity,
                    getString(R.string.error_loading_machines),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Machine>>,
                response: Response<ArrayList<Machine>>
            ) {
                if (response.isSuccessful) { // 200 ... 300
                    val machines = response.body() as ArrayList<Machine>
                    spinnerMachine.adapter = ArrayAdapter<Machine>(
                        this@CreateStopActivity,
                        android.R.layout.simple_list_item_1,
                        machines
                    )
                }
            }
        })
    }

    private fun loadProducts() {
        val call = apiService.getProducts()
        call.enqueue(object : Callback<ArrayList<Product>> {
            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(
                    this@CreateStopActivity,
                    getString(R.string.error_loading_products),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.isSuccessful) { // 200 ... 300
                    val products = response.body() as ArrayList<Product>
                    spinnerProduct.adapter = ArrayAdapter<Product>(
                        this@CreateStopActivity,
                        android.R.layout.simple_list_item_1,
                        products
                    )
                }
            }
        })
    }

    private fun loadColors() {
        val call = apiService.getColors()
        call.enqueue(object : Callback<ArrayList<Color>> {
            override fun onFailure(call: Call<ArrayList<Color>>, t: Throwable) {
                Toast.makeText(
                    this@CreateStopActivity,
                    getString(R.string.error_loading_colors),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Color>>,
                response: Response<ArrayList<Color>>
            ) {
                if (response.isSuccessful) { // 200 ... 300
                    val colors = response.body() as ArrayList<Color>
                    spinnerColor.adapter = ArrayAdapter<Color>(
                        this@CreateStopActivity,
                        android.R.layout.simple_list_item_1,
                        colors
                    )
                }
            }
        })
    }

    private fun showHideItems () {
        val selectCode = spinnerCode.selectedItem.toString()
        Toast.makeText(this, selectCode, Toast.LENGTH_LONG).show()
    }

    private fun showStopDataToConfirm() {
        tvConfirmCode.text = spinnerCode.selectedItem.toString()
        tvConfirmStopType.text = etStopType.text.toString()
        tvConfirmMachine.text = spinnerMachine?.selectedItem.toString()
        if(spinnerProduct.selectedItem != null) {
            tvConfirmProduct.text = spinnerProduct?.selectedItem.toString()
            layout_product.visibility = View.VISIBLE
        } else {
            tvConfirmProduct.text = ""
            layout_product.visibility = View.GONE
        }

        if(spinnerColor.selectedItem != null) {
            tvConfirmColor.text = spinnerColor?.selectedItem.toString()
            layout_color.visibility = View.VISIBLE
        } else {
            tvConfirmColor.text = ""
            layout_color.visibility = View.GONE
        }

        tvConfirmMeters.text = etMeters?.text.toString()
        tvConfirmComments.text = etComments?.text.toString()
        tvConfirmStartDateTimeStop.text = preferences["lastStopDateTimeStart", ""]
        val currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(Date())
        tvConfirmEndDateTimeStop.text = currentDateTimeString
    }

    override fun onBackPressed() {
        when {
            cvStep2.visibility == View.VISIBLE -> {
                cvStep2.visibility = View.GONE
                cvStep1.visibility = View.VISIBLE
            }
            cvStep1.visibility == View.VISIBLE -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_create_stop_exit_title))
                builder.setMessage(getString(R.string.dialog_create_stop_exit_message))
                builder.setPositiveButton(getString(R.string.dialog_create_stop_exit_positive_btn)) { _, _ ->
                    finish()
                }
                builder.setNegativeButton(getString(R.string.dialog_create_stop_exit_negative_btn)) { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}