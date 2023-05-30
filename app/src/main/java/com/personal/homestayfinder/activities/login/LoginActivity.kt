package com.personal.homestayfinder.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.personal.homestayfinder.activities.main.MainActivity
import com.personal.homestayfinder.base.activities.BaseActivity
import com.personal.homestayfinder.common.Constant
import com.personal.homestayfinder.common.EventObserver
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    @Inject
    lateinit var gsc: GoogleSignInClient
    @Inject
    lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding : ActivityLoginBinding
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.let {
            updateUI()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }
    private fun setListeners(){
        binding.btnSignInGoogle.setOnClickListener {
            signIn()
        }
        binding.btnSignInFacebook.setOnClickListener {
            showNotifyDialog(
                message = "Vì lí do bảo mật nên chức năng này không còn hoạt động.",
                title = "Thông báo",
                "OK"
            )
        }
    }
    private fun signIn() {
        showScreenLoading(true)
        val signInIntent: Intent = gsc.signInIntent
        mStartForResult.launch(signInIntent)
    }
    private var mStartForResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            showScreenLoading(false)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Check signIn", "signInWithCredential:success")
                            val user = auth.currentUser
                            updateUser(user)
                        }
                    }
                    .addOnFailureListener(this){
                        when(it){
                            is FirebaseNetworkException ->{
                                showErrorDialog(Constant.NETWORK_ERROR)
                            }
                            else ->{
                                showErrorDialog(Constant.UNKNOWN_ERROR)
                            }
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d("Check signIn", "No ID token!")
            }
        }
    }
    private fun updateUser(user : FirebaseUser?){
        if(user != null){
            try {
                val photoUrl = if (user.photoUrl != null) user.photoUrl.toString() else null
                val newUser = User(
                    user.uid,
                    user.email!!,
                    user.displayName!!,
                    photoUrl,
                    online = true
                )
                viewModel.updateUser(newUser)
                viewModel.isScreenLoading.observe(this, EventObserver{
                        isShow ->
                    showScreenLoading(isShow)
                })
                viewModel.errorUpdate.observe(this){ isError ->
                    if(isError){
                        showErrorDialog(Constant.UPDATE_USER_ERROR)
                    }
                    else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            catch (e : Exception){
                Log.d("LoginActivity",e.toString())
                showErrorDialog(Constant.UPDATE_USER_ERROR)
            }
        }
        else{
            showErrorDialog(Constant.LOGIN_ERROR)
        }
    }
    private fun updateUI(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}