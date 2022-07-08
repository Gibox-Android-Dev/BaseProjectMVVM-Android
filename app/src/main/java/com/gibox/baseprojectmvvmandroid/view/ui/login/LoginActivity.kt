/*
 * Created by Muhamad Riza
 * , 21/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.databinding.ActivityLoginBinding
import com.gibox.baseprojectmvvmandroid.util.hideKeyboard
import com.gibox.baseprojectmvvmandroid.view.ui.home.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {


    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModelEvent()

        binding.run {
            btnLogin.setOnClickListener {
                hideKeyboard()
                if (edtUsername.text!=null && edtUsername.text.toString().isNotEmpty() &&
                    edtPassword.text!=null && edtPassword.text.toString().isNotEmpty()){
                    doLogin(edtUsername.text.toString(),edtPassword.text.toString())
                }else{
                    Toast.makeText(applicationContext, "lengkapi data ", Toast.LENGTH_SHORT).show()
                }
            }
            imBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun doLogin(email:String,password:String){
        viewModel.requestLogin(LoginRequest(email = email, password = password))
    }

    private fun viewModelEvent() {
        viewModel.dataRequestLogin.observe(this) { data ->
            if (data != null) {
                if (data.token != null) {
                    binding.run {
                        txtToken.text = "Token : ${data.token}".plus(data.token.toString())
                        txtEmail.text = "Email : ".plus(edtUsername.text.toString())
                        txtStatus.text = "Status : Login Sukses"
                    }
                }
            }
        }

        viewModel.isErrorRequestLogin.observe(this,{mesage->
            if (mesage!=null){
                binding.run {
                    txtToken.text = "Token : null"
                    txtEmail.text = "Email : null"
                    txtStatus.text = "Status : Login Gagal ".plus(mesage)
                }
            }
        })
    }
}