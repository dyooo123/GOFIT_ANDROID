package com.example.gofit_10587.api

class LoginApi {

    companion object{
        val BASE_URL = "https://gofitku.my.id/api/"

        val LOGIN_URL = BASE_URL + "loginUser"

        val RESET_PASSWORD_URL = BASE_URL + "gantiPassword"
        val LOGOUT_URL = BASE_URL + "logout"
        val tampilanJadwal = BASE_URL + "dataJadwal"
    }
}