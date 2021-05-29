package com.natife.streaming.usecase

import android.content.Context
import com.google.gson.Gson
import com.natife.streaming.API_REGISTER
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.exception.ApiException
import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.dto.register.RequestRegister
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.utils.Result

interface RegisterUseCase {
    suspend fun execute(
        email: String,
        password: String,
        onComplete: (Result<String>) -> Unit
    )
}

class RegisterUseCaseImpl(
    private val api: MainApi,
    private val context: Context
) : RegisterUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        onComplete: (Result<String>) -> Unit
    ) {
        val request = BaseRequest(
            procedure = API_REGISTER,
            params = RequestRegister(
                email,
                password
            )
        )
        try {
            val register = api.makeRegister(request)
            if (register.status == 1) {
                onComplete(Result.success("Success"))
            }
        } catch (e: ApiException) {
            val body = Gson().fromJson(e.body, LoginDTO::class.java)
            when (body.status) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


