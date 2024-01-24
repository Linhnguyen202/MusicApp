package com.example.musicapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.component.AuthComponent
import com.example.musicapp.component.MainComponent
import com.example.musicapp.databinding.ActivityLoginSrcreenBinding
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.model.UserResponse
import com.example.musicapp.model.loginBody
import com.example.musicapp.repository.AuthRepository
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.AuthViewModel.AuthViewModel
import com.example.musicapp.viewmodel.AuthViewModel.AuthViewModelFactory
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import javax.inject.Inject

class LoginSrcreen : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    lateinit var binding : ActivityLoginSrcreenBinding
    lateinit var component : AuthComponent



    @Inject lateinit var repository : AuthRepository
    lateinit var viewModel: AuthViewModel
    lateinit var viewModelFactory : AuthViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSrcreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        component = (application as MyApplication).component.getAuthComponent().create()
        component.injectLoginScreen(this)

        viewModelFactory = AuthViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[AuthViewModel::class.java]

        onEvents()
        onObserveData()

    }

    private fun onObserveData() {
        viewModel.loginData.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    it.data.let  { UserResponse ->
                        sharePreferencesUtils.saveToken(UserResponse!!.accessToken,this)
                        sharePreferencesUtils.saveUser(UserResponse!!.data,this)
                        binding.progessBar.visibility = View.GONE
                        binding.signinTile.visibility = View.VISIBLE
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                is Resource.Error -> {
                    if(it.message.equals("400")){
                        binding.passContainer.apply {
                            isErrorEnabled = true
                            error = "Incorrect password"
                        }
                        Toast.makeText(this,"Incorrect password",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                   binding.progessBar.visibility = View.VISIBLE
                    binding.signInTitle.visibility = View.GONE
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onEvents() {
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener = this
        binding.emailContainer.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
        binding.passContainer.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
        binding.loginBtn.setOnClickListener {
            onSubmit()
        }
        binding.signup.setOnClickListener {
            val intent = Intent(this,RegisterScreen::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(v: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
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

            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP){
            onSubmit()
        }
        return false
    }

    private fun onSubmit()  {
        if(!onValidate()){
            val email : String = binding.emailEditText.text.toString()
            val password : String = binding.passEditText.text.toString()
            val loginBody = loginBody(email,password)
            viewModel.loginUser(loginBody)
        }
    }
    private fun onValidate() : Boolean{
        var validateValue : Boolean = false
        if(validateEmail()) validateValue = true
        if(validatePassword()) validateValue = true
        return validateValue
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
}



