package com.example.musicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.application.MyApplication
import com.example.musicapp.component.AuthComponent
import com.example.musicapp.databinding.ActivityRegisterScreenBinding
import com.example.musicapp.model.RegisterBody
import com.example.musicapp.model.UserResponse
import com.example.musicapp.model.loginBody
import com.example.musicapp.repository.AuthRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.AuthViewModel.AuthViewModel
import com.example.musicapp.viewmodel.AuthViewModel.AuthViewModelFactory
import javax.inject.Inject

class RegisterScreen : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    lateinit var binding : ActivityRegisterScreenBinding
    lateinit var component : AuthComponent

    @Inject
    lateinit var repository : AuthRepository
    lateinit var viewModel: AuthViewModel
    lateinit var viewModelFactory : AuthViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        component = (application as MyApplication).component.getAuthComponent().create()
        component.injectRegisScreen(this)

        viewModelFactory = AuthViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[AuthViewModel::class.java]



        addEvents()
        onObserveData()
    }

    private fun onObserveData() {
        viewModel.registerData.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    it.data.let { UserResponse ->
                        sharePreferencesUtils.saveToken(UserResponse!!.accessToken,this)
                        sharePreferencesUtils.saveUser(UserResponse!!.data,this)
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                is Resource.Error -> {
                    if(it.message.equals("400")){
                        binding.passContainer.apply {
                            isErrorEnabled = true
                            error = "account exists"
                        }
                        Toast.makeText(this,"account exists", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(this,"Loading", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun addEvents() {
        binding.nameEditText.onFocusChangeListener = this
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener = this
        binding.confirmPassEditText.onFocusChangeListener = this
        binding.registerBtn.setOnClickListener {
            onSubmit()
        }
        binding.haveAccountBtn.setOnClickListener{
            val intent = Intent(this,LoginSrcreen::class.java)
            startActivity(intent)
        }
    }

    private fun validateEmail(): Boolean {
        val valueEmail = binding.emailEditText.text.toString()
        if(valueEmail.isNullOrEmpty()){
            binding.emailContainer.apply {
                isErrorEnabled = true
                error = "This field must not empty"
            }
            return true
        }
        return false
    }

    private fun validatePassword(): Boolean {
        val valuePass = binding.passEditText.text.toString()
        if(valuePass.isNullOrEmpty()){
            binding.passContainer.apply {
                isErrorEnabled = true
                error = "This field must not empty"
            }
            return true
        }
        return false
    }

    private fun validateConfirmPass(): Boolean {
        val valueConfirmPass = binding.confirmPassEditText.text.toString()
        val valuePass = binding.passEditText.text.toString()
        if(valuePass.isNullOrEmpty()){
            binding.confirmPassContainer.apply {
                isErrorEnabled = true
                error = "This field must not empty"
            }
            return true
        }
        else if(!valueConfirmPass.equals(valuePass)){
            binding.confirmPassContainer.apply {
                isErrorEnabled = true
                error = "This field is not equal password"
            }
            return true
        }
        return false
    }
    private fun validateFullname(): Boolean {
        val valueFullname = binding.nameEditText.text.toString()
        if(valueFullname.isNullOrEmpty()){
            binding.nameContainer.apply {
                isErrorEnabled = true
                error = "This field must not empty"
            }
            return true
        }
        return false
    }

    private fun onValidate() : Boolean{
        var validateValue : Boolean = false
        if(validateFullname()) validateValue =true
        if(validateEmail()) validateValue = true
        if(validatePassword()) validateValue = true
        if(validateConfirmPass()) validateValue = true
        return validateValue
    }

    override fun onClick(v: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.nameEditText -> {
                    if(hasFocus){
                        if(binding.nameContainer.isErrorEnabled){
                            binding.nameContainer.apply {
                                isErrorEnabled = false
                                error = ""
                            }

                        }
                    }
                }
                R.id.emailEditText -> {
                    if(hasFocus){
                        if(binding.emailContainer.isErrorEnabled){
                            binding.emailContainer.apply {
                                isErrorEnabled = false
                                error = ""
                            }

                        }
                    }
                }
                R.id.passEditText -> {
                    if(hasFocus){
                        if(binding.passContainer.isErrorEnabled){
                            binding.passContainer.apply {
                                isErrorEnabled = false
                                error = ""
                            }
                        }
                    }
                }
                R.id.confirmPassEditText -> {
                    if(hasFocus){
                        if(binding.confirmPassContainer.isErrorEnabled){
                            binding.confirmPassContainer.apply {
                                isErrorEnabled = false
                                error = ""
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP){
            onSubmit()
        }
        return false
    }

    private fun onSubmit() {
        if(!onValidate()){
            val username : String = binding.nameEditText.text.toString()
            val email : String = binding.emailEditText.text.toString()
            val password : String = binding.passEditText.text.toString()
            val registerBody = RegisterBody(username,password,email)
            viewModel.registerUser(registerBody)
        }
    }
}