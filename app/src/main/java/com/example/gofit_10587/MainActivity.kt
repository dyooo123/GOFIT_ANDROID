package com.example.gofit_10587

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_10587.api.LoginApi
import com.example.gofit_10587.databinding.ActivityMainBinding
import com.example.gofit_10587.models.Auth
import com.example.gofit_10587.models.umumActivity
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    var etEmail: String = ""
    var etPassword: String = ""
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var sharedPreferences: SharedPreferences
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)


        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)
        if (isFirstRun) {
            startActivity(Intent(this@MainActivity, SplashScreen::class.java))
            finish()
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)

        binding.btnUmum.setOnClickListener(View.OnClickListener {
            val moveUmum = Intent(this@MainActivity, umumActivity::class.java)
            startActivity(moveUmum)
        })

        binding.btnGantiPassword.setOnClickListener(View.OnClickListener  {
            val moveDaftar = Intent(this@MainActivity, GantiPasswordActivity::class.java)
            startActivity(moveDaftar)
        })

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            etEmail = binding.inputLayoutUsername.editText?.text.toString()
            etPassword = binding.inputLayoutPassword.editText?.text.toString()

            val auth = Auth(
                etEmail, etPassword
            )

            val stringRequest: StringRequest =
                object : StringRequest(
                    Request.Method.POST,
                    LoginApi.LOGIN_URL,
                    Response.Listener { response ->
                        val gson = Gson()
                        var jsonObj = JSONObject(response.toString())
                        var userObjectData = jsonObj.getJSONObject("user")

                        if (userObjectData.has("ID_MEMBER")) {
                            val token = jsonObj.getString("access_token")
                            val move = Intent(this@MainActivity, HomeActivity::class.java)
                            FancyToast.makeText(
                                this,
                                "Berhasil Login Member",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            sharedPreferences.edit()
                                .putString("id", userObjectData.getString("ID_MEMBER"))
                                .putString("role", "member")
                                .putString("token", token)
                                .apply()
                            startActivity(move)
                        } else if (userObjectData.has("ID_PEGAWAI")) {
                            val token = jsonObj.getString("access_token")
                            sharedPreferences.edit()
                                .putInt("id", userObjectData.getInt("ID_PEGAWAI"))
                                .putString("role", "Manajer Operasional")
                                .putString("token", token)
                                .apply()
                            FancyToast.makeText(
                                this,
                                sharedPreferences.getString("role", null),
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            val move = Intent(this@MainActivity, HomeActivity::class.java)
//                        move.putExtra("nama", etNama)
                            startActivity(move)
                        } else if (userObjectData.has("ID_INSTRUKTUR")) {
                            val token = jsonObj.getString("access_token")
                            sharedPreferences.edit()
                                .putInt("id", userObjectData.getInt("ID_INSTRUKTUR"))
                                .putString("role", "instruktur")
                                .putString("token", token)
                                .apply()
                            FancyToast.makeText(
                                this,
                                "Berhasil Login Instruktur",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            val move = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(move)
                        }
                    }, Response.ErrorListener { error ->
                        // setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@MainActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        }

                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(auth)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }

                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["Email"] = inputUsername.getEditText()?.getText().toString()
                        params["password"] = inputPassword.getEditText()?.getText().toString()
                        return params

                    }
                }
            queue!!.add(stringRequest)

        })

    }
}